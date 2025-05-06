// Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import ballerina/log;
import ballerina/websubhub;
import ballerina/http;
import ballerinax/kafka;
import ballerina/lang.value;
import kafkaHub.util;
import kafkaHub.connections as conn;
import ballerina/mime;
import kafkaHub.config;

isolated map<websubhub:TopicRegistration> registeredTopicsCache = {};
isolated map<websubhub:VerifiedSubscription> subscribersCache = {};

public function main() returns error? {
    // Initialize the Hub
    _ = @strand {thread: "any"} start syncRegsisteredTopicsCache();
    _ = @strand {thread: "any"} start syncSubscribersCache();

    // Start the Hub
    http:Listener httpListener = check new (config:HUB_PORT);
    check httpListener.attach(healthCheckService, "hub/actuator/health");
    websubhub:Listener hubListener = check new (httpListener);
    check hubListener.attach(hubService, "hub");
    check hubListener.'start();
}

function syncRegsisteredTopicsCache() {
    do {
        while true {
            websubhub:TopicRegistration[]? persistedTopics = check getPersistedTopics();
            if persistedTopics is websubhub:TopicRegistration[] {
                refreshTopicCache(persistedTopics);
            }
        }
    } on fail var e {
        log:printError("Error occurred while syncing registered-topics-cache ", err = e.message());

        kafka:Error? result = conn:registeredTopicsConsumer->close(config:GRACEFUL_CLOSE_PERIOD);
        if result is kafka:Error {
            log:printError("Error occurred while gracefully closing kafka-consumer", err = result.message());
        }
    }
}

function getPersistedTopics() returns websubhub:TopicRegistration[]|error? {
    kafka:ConsumerRecord[] records = check conn:registeredTopicsConsumer->poll(config:POLLING_INTERVAL);
    if records.length() > 0 {
        kafka:ConsumerRecord lastRecord = records.pop();
        string|error lastPersistedData = string:fromBytes(lastRecord.value);
        if lastPersistedData is string {
            return deSerializeTopicsMessage(lastPersistedData);
        } else {
            log:printError("Error occurred while retrieving topic-details ", err = lastPersistedData.message());
            return lastPersistedData;
        }
    }
}

function deSerializeTopicsMessage(string lastPersistedData) returns websubhub:TopicRegistration[]|error {
    websubhub:TopicRegistration[] currentTopics = [];
    json[] payload = <json[]>check value:fromJsonString(lastPersistedData);
    foreach var data in payload {
        websubhub:TopicRegistration topic = check data.cloneWithType(websubhub:TopicRegistration);
        currentTopics.push(topic);
    }
    return currentTopics;
}

function refreshTopicCache(websubhub:TopicRegistration[] persistedTopics) {
    lock {
        registeredTopicsCache.removeAll();
    }
    foreach var topic in persistedTopics.cloneReadOnly() {
        string topicName = util:sanitizeTopicName(topic.topic);
        lock {
            registeredTopicsCache[topicName] = topic.cloneReadOnly();
        }
    }
}

function syncSubscribersCache() {
    do {
        while true {
            websubhub:VerifiedSubscription[]? persistedSubscribers = check getPersistedSubscribers();
            if persistedSubscribers is websubhub:VerifiedSubscription[] {
                refreshSubscribersCache(persistedSubscribers);
                check startMissingSubscribers(persistedSubscribers);
            }
        }
    } on fail var e {
        log:printError("Error occurred while syncing subscribers-cache ", err = e.message());

        kafka:Error? result = conn:subscribersConsumer->close(config:GRACEFUL_CLOSE_PERIOD);
        if result is kafka:Error {
            log:printError("Error occurred while gracefully closing kafka-consumer", err = result.message());
        }
    }
}

function getPersistedSubscribers() returns websubhub:VerifiedSubscription[]|error? {
    kafka:ConsumerRecord[] records = check conn:subscribersConsumer->poll(config:POLLING_INTERVAL);
    if records.length() > 0 {
        kafka:ConsumerRecord lastRecord = records.pop();
        string|error lastPersistedData = string:fromBytes(lastRecord.value);
        if lastPersistedData is string {
            return deSerializeSubscribersMessage(lastPersistedData);
        } else {
            log:printError("Error occurred while retrieving subscriber-data ", err = lastPersistedData.message());
            return lastPersistedData;
        }
    }
}

function deSerializeSubscribersMessage(string lastPersistedData) returns websubhub:VerifiedSubscription[]|error {
    websubhub:VerifiedSubscription[] currentSubscriptions = [];
    json[] payload = <json[]>check value:fromJsonString(lastPersistedData);
    foreach var data in payload {
        websubhub:VerifiedSubscription subscription = check data.cloneWithType(websubhub:VerifiedSubscription);
        currentSubscriptions.push(subscription);
    }
    return currentSubscriptions;
}

function refreshSubscribersCache(websubhub:VerifiedSubscription[] persistedSubscribers) {
    string[] groupNames = persistedSubscribers.'map(sub => util:generateGroupName(sub.hubTopic, sub.hubCallback));
    lock {
        string[] unsubscribedSubscribers = subscribersCache.keys().filter('key => groupNames.indexOf('key) is ());
        foreach var sub in unsubscribedSubscribers {
            _ = subscribersCache.removeIfHasKey(sub);
        }
    }
}

function startMissingSubscribers(websubhub:VerifiedSubscription[] persistedSubscribers) returns error? {
    foreach var subscriber in persistedSubscribers {
        string topicName = util:sanitizeTopicName(subscriber.hubTopic);
        string groupName = util:generateGroupName(subscriber.hubTopic, subscriber.hubCallback);
        log:printDebug("Started Missing subscribers operation", groupName = groupName);
        boolean subscriberNotAvailable = true;
        lock {
            subscriberNotAvailable = !subscribersCache.hasKey(groupName);
            subscribersCache[groupName] = subscriber.cloneReadOnly();
        }
        if subscriberNotAvailable {
            log:printInfo("Started Missing subscribers operation - new subscriber added to cache", topic = subscriber.hubTopic, callback = subscriber.hubCallback);
            kafka:Consumer consumerEp = check conn:createMessageConsumer(subscriber);
            websubhub:HubClient hubClientEp = check new (subscriber, {
                retryConfig: {
                    interval: config:MESSAGE_DELIVERY_RETRY_INTERVAL,
                    count: config:MESSAGE_DELIVERY_COUNT,
                    backOffFactor: 2.0,
                    maxWaitInterval: 20
                },
                timeout: config:MESSAGE_DELIVERY_TIMEOUT
            });
            _ = @strand {thread: "any"} start pollForNewUpdates(hubClientEp, consumerEp, topicName, groupName, subscriber.hubCallback);
        }
    }
}

isolated function pollForNewUpdates(websubhub:HubClient clientEp, kafka:Consumer consumerEp, string topicName, string groupName, string callback) {
    do {
        log:printInfo("pollForNewUpdates operation - Thread started ", topic = topicName, callback = callback);
        while true {
            kafka:ConsumerRecord[] records = check consumerEp->poll(config:POLLING_INTERVAL);
            log:printDebug("pollForNewUpdates operation - records pull ", length = records.length(), groupName = groupName);
            if !isValidConsumer(topicName, groupName, callback) {
                fail error(string `Consumer with group name ${groupName} or topic ${topicName} or ${callback} is invalid`);
            }
            error? resp = check notifySubscribers(records, clientEp, consumerEp, topicName, callback);
            if (resp is error) {
                log:printError("Error occurred while sending notification to subscriber ", errorResponse = resp.message(), topic = topicName, groupName = groupName, callback = callback);
            }
        }
    } on fail var e {
        lock {
            _ = subscribersCache.remove(groupName);
        }
        log:printError("Error occurred while sending notification to subscriber", err = e.message());

        kafka:Error? result = consumerEp->close(config:GRACEFUL_CLOSE_PERIOD);
        if result is kafka:Error {
            log:printError("Error occurred while gracefully closing kafka-consumer", topic = topicName, callback = callback, err = result.message());
        }
    }
}

isolated function isValidConsumer(string topicName, string groupName, string callback) returns boolean {
    boolean topicAvailable = true;
    lock {
        topicAvailable = registeredTopicsCache.hasKey(topicName);
        log:printDebug("pollForNewUpdates operation - topicAvailable ", topicAvailable = topicAvailable, groupName = groupName, topic = topicName, callback = callback);
    }
    boolean subscriberAvailable = true;
    lock {
        subscriberAvailable = subscribersCache.hasKey(groupName);
        log:printDebug("pollForNewUpdates operation - subscriberAvailable ", subscriberAvailable = subscriberAvailable, groupName = groupName, topic = topicName, callback = callback);
    }
    return topicAvailable && subscriberAvailable;
}

isolated function notifySubscribers(kafka:ConsumerRecord[] records, websubhub:HubClient clientEp, kafka:Consumer consumerEp, string topic, string callback) returns error? {
    foreach kafka:ConsumerRecord kafkaRecord in records {
        websubhub:ContentDistributionMessage|error message = deSerializeKafkaRecord(kafkaRecord);

        if (message is websubhub:ContentDistributionMessage) {
            log:printDebug("notifying subscriber with message", message = message.cloneReadOnly(), topic = topic, callback = callback, offset = kafkaRecord.offset);
            websubhub:ContentDistributionSuccess|websubhub:SubscriptionDeletedError|websubhub:Error response = clientEp->notifyContentDistribution(message);
            if (response is websubhub:SubscriptionDeletedError) {
                log:printDebug("Subscription Deletion Error occurred while sending notification to subscriber ", topic = topic, callback = callback, offset = kafkaRecord.offset);
                return response;
            } else if (response is websubhub:Error) {
                log:printDebug("Error occurred while sending notification to subscriber ", topic = topic, callback = callback, offset = kafkaRecord.offset);
                return response;
            }
            else if (response is websubhub:ContentDistributionSuccess) {
                log:printDebug("Notification sent to subscriber", topic = topic, callback = callback, offset = kafkaRecord.offset);
                kafka:Error? commitRes = check consumerEp->commit();
                if (commitRes is kafka:Error) {
                    log:printError("Error occurred while commiting to kafka", err = commitRes.message(), topic = topic, callback = callback, offset = kafkaRecord.offset);
                    return commitRes;
                } else {
                    log:printDebug("commited to kafka successfully", topic = topic, callback = callback, offset = kafkaRecord.offset);
                }

            }
        } else {
            log:printError("Error occurred while retrieving message data", err = message.message(), topic = topic, callback = callback, offset = kafkaRecord.offset);
        }
    }
}

isolated function deSerializeKafkaRecord(kafka:ConsumerRecord kafkaRecord) returns websubhub:ContentDistributionMessage|error {
    byte[] content = kafkaRecord.value;
    string|error message = check string:fromBytes(content);
    if (message is string) {
        json|error payload = check value:fromJsonString(message);
        if (payload is json) {
            websubhub:ContentDistributionMessage distributionMsg = {
                content: payload,
                contentType: mime:APPLICATION_JSON
            };
            return distributionMsg;
        } else {
            log:printError("error converting string message to json", err = payload.message());
            return payload;
        }
    } else {
        log:printError("error converting byte content to string", err = message.message());
        return message;
    }

}
