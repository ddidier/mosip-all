# Test Report

## Testing Scope

The scope of testing is to verify fitment to the specification from the perspective of

* Functionality
* Deployability
* Configurability
* Customizability

Verification is performed not only from the end user perspective but also from the System Integrator (SI) point of view. Hence Configurability and Extensibility of the software is also assessed. This ensures the readiness of software for use in multiple countries. Since MOSIP is an "API First" product platform, Verification scope required comprehensive automation testing for all the MOSIP APIs. An automation Test Rig is created for the same.

The Partner Management System Revamp testing scope revolves around the following flows:

* Features: Authentication Partner, Device Partner, FTM Partner, Partner Admin Certificate Trust Store, Partners, Policies, Partner-Policy Linking, SBI-Device, FTM Chip, Authentication Services, User Profile, User Dashboard, Accept Terms & Conditions/ Consent.
* Multilingual (English/Arabic/French)
* Multi-browser testing: Using Edge, Firefox, and Chrome browsers (Windows/ Mac, Tablet, Extra-large screen)
* Partner Management System Revamp End to End testing
* Regression Testing
* Integration Testing

## Test Approach

Persona based approach has been adopted to perform the IV\&V, by simulating test scenarios that resemble a real-time implementation.

A Persona is a fictional character/user profile created to represent a user type that might use a product/or a service in a similar way. Persona based testing is a software testing technique that puts software testers in the customer's shoes, assesses their needs from the software and thereby determines use cases/scenarios that the customers will execute. The persona needs may be addressed through any of the following.

* Functionality
* Deployability
* Configurability
* Customizability

The verification methods may differ based on how the need was addressed.

For regression check, "MOSIP Test Rig" - an automation testing suite - which is indigenously designed and developed for supporting persona-based testing. MOSIP Test Rig covers the end-to-end test execution and reporting. The end-to-end functional test scenarios are written starting from creation of packet in registration center, processing the packet through the registration processor, generating UIN and authenticating identity using IDA through various permutation and combinations of cases being covered. MOSIP Test Rig will be an open-source artifact which can also be enhanced and used by countries to validate the SI deliveries before going live. Persona classes include positive personas.

## Verified configuration

Verification is performed on various configurations as mentioned below:

* Default configuration with verified configuration for 3 Lang (English/Arabic/French)

### Browser compatibility evaluations

#### Browser versions tested on desktop/laptop

<table><thead><tr><th width="293.4176025390625">Browser</th><th>Versions</th></tr></thead><tbody><tr><td>Chrome</td><td>133.0.6943.121</td></tr><tr><td>Firefox</td><td>135.0.1</td></tr><tr><td>Edge</td><td>133.0.3065.82</td></tr><tr><td>Safari</td><td>18.3</td></tr></tbody></table>

#### Browser versions tested on tablet device

<table><thead><tr><th width="303.55255126953125">Browser</th><th>Versions</th></tr></thead><tbody><tr><td>Chrome</td><td>July 31, 2016</td></tr><tr><td>Firefox</td><td>July 31, 2016</td></tr><tr><td>Edge</td><td>July 31, 2016</td></tr></tbody></table>

#### Browser versions tested on extra-large screens

<table><thead><tr><th width="309.0426025390625">Browser</th><th>Versions</th></tr></thead><tbody><tr><td>Chrome</td><td>133.0.6943.121</td></tr><tr><td>Firefox</td><td>135.0.1</td></tr><tr><td>Edge</td><td>133.0.3065.82</td></tr><tr><td>Safari</td><td>18.3</td></tr></tbody></table>

## Screen sizes used for UI responsiveness validation

* Laptop/Desktop: 1920x1080
* Tablet: 1280X800
* Extra-large screens: 3840x2160
* Mac book: 2560 x 1664

## Feature Health

<figure><img src="../../../.gitbook/assets/pms-1.2.2.0-feature-health.png" alt=""><figcaption></figcaption></figure>



## Test execution statistics

### **Functional test results by modules**

Below are the test metrics by performing functional testing. The process followed was black box testing which based its test cases on the specifications of the software component under test. The functional test was performed in combination with individual module testing as well as integration testing. Test data were prepared in line with the user stories. Expected results were monitored by examining the user interface. The coverage includes GUI testing, System testing, End-To-End flows across multiple languages and configurations. The testing cycle included simulation of multiple identity schema and respective UI schema configurations.

Manual Verification (UI):

| **Total** | **Passed** | **Unsuccessful** | **Skipped (N/A)** |
| --------- | ---------- | ---------------- | ----------------- |
| 4103      | 4033       | 39               | 31                |

Test Rate: 99% with Pass Rate: 99%

Note: NA - 31 Test Cases which are descoped scenarios/not developed feature



Manual Verification (API):

| **Total** | **Passed** | **Failed** | **Skipped (N/A)** |
| --------- | ---------- | ---------- | ----------------- |
| 542       | 532        | 2          | 8                 |

Test Rate: 98% Pass rate: 98%

Note: 8 Test Cases are not developed features.

Note: NA - 8 Test Cases which are descoped scenarios/not developed feature



API Test Rig:

| **Total** | **Passed** | **Failed** | **Skipped (N/A)** | **Ignored** | **Known issues** |
| --------- | ---------- | ---------- | ----------------- | ----------- | ---------------- |
| 509       | 497        | 0          | 0                 | 0           | 12               |

Test Rate: 98% with Pass Rate: 98%

**Note-** API flow is tested through automation for both positive and negative scenarios, while test cases that are not automated are tested manually.

### Detailed Test metrics

Below are the detailed test metrics by performing Manual/automation testing. The project metrics are derived from Defect density, Test coverage, Test execution coverage, test tracking and efficiency.

The various metrics that assist in test tracking and efficiency are as follows:

* Passed Test Cases Coverage: It measures the percentage of passed test cases. (Number of passed tests / Total number of tests executed) x 100
* Failed Test Case Coverage: It measures the percentage of all failed test cases. (Number of failed tests / Total number of test cases executed) x 100

### **Tested with components:**

<table><thead><tr><th width="325.10443115234375">Module/Repo</th><th>Compatible Version</th></tr></thead><tbody><tr><td>partner-management-portal</td><td>mosipqa/pmp-revamp-ui:1.2.2.x</td></tr><tr><td>partner-management-services</td><td>mosipqa/partner-management-service:1.2.2.x</td></tr><tr><td>Policy Management service</td><td>mosipqa/policy-management-service:1.2.2.x</td></tr><tr><td>Key-manager</td><td>mosipqa/kernel-keymanager-service:1.3.x</td></tr><tr><td>IDA Auth</td><td>mosipid/authentication-service:1.2.1.0</td></tr><tr><td>Artifactory</td><td>mosipid/artifactory-server:1.2.0.2</td></tr><tr><td>eSignet</td><td>mosipid/esignet:1.4.1</td></tr><tr><td>Notifier (Kernel)</td><td>mosipid/kernel-notification-service:1.2.0.1</td></tr><tr><td>Audit manager</td><td>mosipid/kernel-auditmanager-service:1.2.0.1</td></tr><tr><td>ID Repro</td><td>mosipid/id-repository-identity-service:1.2.1.0</td></tr><tr><td>datashare</td><td>mosipid/data-share-service:1.2.0.1</td></tr><tr><td>Keycloak</td><td>mosipid/mosip-artemis-keycloak:1.2.0.1</td></tr><tr><td>config-server</td><td>mosipid/config-server:1.1.2</td></tr><tr><td>Websub</td><td>mosipid/websub-service:1.2.0.1</td></tr><tr><td>postgres</td><td>bitnami/postgresql:15.4.0-debian-11-r10</td></tr></tbody></table>

### **Sonar Report**

* **Partner-Management-Service:**

<figure><img src="../../../.gitbook/assets/pms-1.2.2.0-partner-management-service.png" alt=""><figcaption></figcaption></figure>

* **Partner-Management-Portal:**

<figure><img src="../../../.gitbook/assets/pms-1.2.2.0-partner-management-portal.png" alt=""><figcaption></figcaption></figure>

For details on the test results, refer [here](https://github.com/mosip/test-management/tree/master/PMS%20Revamp/1.2.2.0).
