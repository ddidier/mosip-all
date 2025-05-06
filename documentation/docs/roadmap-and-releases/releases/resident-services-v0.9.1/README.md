# Resident Services v0.9.1

**Release version**: v0.9.1

**Release Date**: 28th January, 2025

## Overview

The v0.9.1 release of Resident Services for MOSIP is the first patch update following v0.9.0. This release primarily addresses bug fixes, security improvements, and performance enhancements, along with minor feature updates. A key highlight of this patch is the transition of certain attributes from being hardcoded to being configurable via mosip-config, making customization more flexible for users.

## Repository Released&#x20;

| Repositories      | Tags Released                                                       |
| ----------------- | ------------------------------------------------------------------- |
| resident-services | [1.2.1.1](https://github.com/mosip/resident-services/tree/v1.2.1.1) |
| resident-ui       | [0.9.1](https://github.com/mosip/resident-ui/tree/v0.9.1)           |
| mosip-config      | [1.2.3.1](https://github.com/mosip/mosip-config/tree/v1.2.3.1)      |
| mosip-data        | [1.2.2.0](https://github.com/mosip/mosip-data/tree/v1.2.2.0)        |
| admin-services    | [1.2.1.1](https://github.com/mosip/admin-services/tree/v1.2.1.1)    |
| postgres-init     | [1.2.0.2](https://github.com/mosip/postgres-init/tree/v1.2.0.2)     |

## Major Highlights

**Bug fixes -** Security, and performance improvements, along with minor feature enhancements.

**Key highlights -** Certain attributes now come from **mosip-config** instead of being hardcoded, enabling users for easier configuration.

## Summary

#### Services

For a detailed description of Resident services, the code, and the design, refer to the [resident services repo](https://github.com/mosip/resident-services/tree/v1.2.1.1).

#### Resident Portal UI

MOSIP provides a reference implementation of the resident portal that can be customized to the country’s needs. The sample implementation is available [here](https://github.com/mosip/resident-ui/releases/tag/v0.9.1).

## Known Issues

#### Complete List

To get the complete list of known bugs please refer [here](https://mosip.atlassian.net/issues/MOSIP-33078?filter=-4\&jql=parent%3Dmosip-20342%20and%20status%20not%20in%20%28closed%2C%20canceled%2C%20fixed%2C%20testing%2C%20%22On%20Hold%20-%20Dev%22%29%20and%20issuetype%3Dbug).

#### Key Issues

| JIRA Issues                                                   | Issue Description                                                                                                                                        |
| ------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------- |
| [MOSIP-38771](https://mosip.atlassian.net/browse/MOSIP-38771) | Update my data feature is not working as expected for a few UINs. The issue is intermittent.                                                             |
| [MOSIP-35282](https://mosip.atlassian.net/browse/MOSIP-35282) | Video policy transactions allowed are not working as per policy.                                                                                         |
| [MOSIP-33065](https://mosip.atlassian.net/browse/MOSIP-33065) | Resident UI - On uploading an Invalid document in update my data the data entered is also getting clear                                                  |
| [MOSIP-33058](https://mosip.atlassian.net/browse/MOSIP-33058) | Resident UI - After updating data and trying to update soon then in the pop if we cancel the update request we see an error message with no record found |
| [MOSIP-32822](https://mosip.atlassian.net/browse/MOSIP-32822) | Resident UI: If the name field is very lengthy, the downloaded card is not getting the complete name                                                     |
| [MOSIP-31136](https://mosip.atlassian.net/browse/MOSIP-31136) | Resident API: VC verification is failing for name, photo, and full address attributes                                                                    |
| [MOSIP-30684](https://mosip.atlassian.net/browse/MOSIP-30684) | Resident UI: Not able to share card when text entered using the virtual keyboard for a few languages                                                     |
| [MOSIP-30682](https://mosip.atlassian.net/browse/MOSIP-30682) | Resident UI: Not able to raise grievance ticket when text entered using the virtual keyboard                                                             |
| [MOSIP-30678](https://mosip.atlassian.net/browse/MOSIP-30678) | Resident UI: When a Personalised UIN card is downloaded, email/sms notifications are not coming in the preferred lang                                    |

## **Compatible Modules** <a href="#id-6.-compatible-modules" id="id-6.-compatible-modules"></a>

The following table outlines the tested and certified compatibility of v0.9.1 with other modules.

| Module                                                          | Versions                                                                                                                                                     |
| --------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| mosipid/authentication-service                                  | [1.2.1.0](https://github.com/mosip/id-authentication/tree/v1.2.1.0/authentication/authentication-service)                                                    |
| mosipid/authentication-internal-service                         | [1.2.1.0](https://github.com/mosip/id-authentication/tree/v1.2.1.0/authentication/authentication-internal-service)                                           |
| mosipid/authentication-otp-service                              | [1.2.1.0](https://github.com/mosip/id-authentication/tree/v1.2.1.0/authentication/authentication-otp-service)                                                |
| mosipid/credential-service                                      | [1.2.1.0](https://github.com/mosip/id-repository/tree/v1.2.1.0/id-repository/credential-service)                                                             |
| mosipid/credential-request-generator                            | [1.2.1.0](https://github.com/mosip/id-repository/tree/v1.2.1.0/id-repository/credential-request-generator)                                                   |
| mosipid/id-repository-identity-service                          | [1.2.1.0](https://github.com/mosip/id-repository/tree/v1.2.1.0/id-repository/id-repository-identity-service)                                                 |
| mosipid/id-repository-vid-service                               | [1.2.1.0](https://github.com/mosip/id-repository/tree/v1.2.1.0/id-repository/id-repository-vid-service)                                                      |
| mosipid/partner-management-service                              | [1.2.1.0](https://github.com/mosip/partner-management-services/tree/v1.2.1.0/partner/partner-management-service)                                             |
| mosipid/policy-management-service                               | [1.2.1.0](https://github.com/mosip/partner-management-services/tree/v1.2.1.0/partner/policy-management-service)                                              |
| mosipid/kernel-notification-service                             | [1.2.0.1](https://github.com/mosip/commons/tree/v1.2.0.1/kernel/kernel-notification-service)                                                                 |
| mosipid/kernel-keymanager-service                               | [1.2.0.1](https://github.com/mosip/keymanager/tree/v1.2.0.1)                                                                                                 |
| mosipid/registration-processor-stage-group-7                    | [1.2.0.1](https://github.com/mosip/registration/tree/v1.2.0.1/registration-processor/stage-groups/registration-processor-stage-group-7)                      |
| mosipid/registration-processor-common-camel-bridge              | [1.2.0.1](https://github.com/mosip/registration/tree/v1.2.0.1/registration-processor/registration-processor-common-camel-bridge)                             |
| mosipid/registration-processor-stage-group-1                    | [1.2.0.1](https://github.com/mosip/registration/tree/v1.2.0.1/registration-processor/stage-groups/registration-processor-stage-group-1)                      |
| mosipid/registration-processor-stage-group-2                    | [1.2.0.1](https://github.com/mosip/registration/tree/v1.2.0.1/registration-processor/stage-groups/registration-processor-stage-group-2)                      |
| mosipid/registration-processor-stage-group-3                    | [1.2.0.1](https://github.com/mosip/registration/tree/v1.2.0.1/registration-processor/stage-groups/registration-processor-stage-group-3)                      |
| mosipid/registration-processor-stage-group-4                    | [1.2.0.1](https://github.com/mosip/registration/tree/v1.2.0.1/registration-processor/stage-groups/registration-processor-stage-group-4)                      |
| mosipid/registration-processor-stage-group-5                    | [1.2.0.1](https://github.com/mosip/registration/tree/v1.2.0.1/registration-processor/stage-groups/registration-processor-stage-group-5)                      |
| mosipid/registration-processor-stage-group-6                    | [1.2.0.1](https://github.com/mosip/registration/tree/v1.2.0.1/registration-processor/stage-groups/registration-processor-stage-group-6)                      |
| mosipid/registration-processor-stage-group-7                    | [1.2.0.1](https://github.com/mosip/registration/tree/v1.2.0.1/registration-processor/stage-groups/registration-processor-stage-group-7)                      |
| mosipid/registration-processor-notification-service             | [1.2.0.1](https://github.com/mosip/registration/tree/v1.2.0.1/registration-processor/registration-processor-notification-service)                            |
| mosipid/registration-processor-dmz-packet-server                | [1.2.0.1](https://github.com/mosip/registration/tree/v1.2.0.1/registration-processor/init/registration-processor-dmz-packet-server)                          |
| mosipid/registration-processor-reprocessor                      | [1.2.0.1](https://github.com/mosip/registration/tree/v1.2.0.1/registration-processor/workflow-engine/registration-processor-reprocessor)                     |
| mosipid/registration-processor-registration-status-service      | [1.2.0.1](https://github.com/mosip/registration/tree/v1.2.0.1/registration-processor/init/registration-processor-registration-status-service)                |
| mosipid/registration-processor-registration-transaction-service | [1.2.0.1](https://github.com/mosip/registration/tree/v1.2.0.1/registration-processor/post-processor/registration-processor-registration-transaction-service) |
| mosipid/registration-processor-workflow-manager-service         | [1.2.0.1](https://github.com/mosip/registration/tree/v1.2.0.1/registration-processor/workflow-engine/registration-processor-workflow-manager-service)        |
| mosipid/kernel-auditmanager-service                             | [1.2.0.1](https://github.com/mosip/audit-manager/tree/v1.2.0.1)                                                                                              |
| mosipid/kernel-masterdata-service                               | [1.2.1.1](https://github.com/mosip/admin-services/tree/v1.2.1.1/admin/kernel-masterdata-service)                                                             |
| mosipid/admin-service                                           | [1.2.1.1](https://github.com/mosip/admin-services/tree/v1.2.1.1/admin/admin-service)                                                                         |
| mosipid/hotlist-service                                         | [1.2.1.1](https://github.com/mosip/admin-services/tree/v1.2.1.1/admin/hotlist-service)                                                                       |
| mosipid/kernel-syncdata-service                                 | [1.2.1.1](https://github.com/mosip/admin-services/tree/v1.2.1.1/admin/kernel-syncdata-service)                                                               |
| mosipid/digital-card-service                                    | [1.2.0.1](https://github.com/mosip/digital-card-service/tree/v1.2.0.1)                                                                                       |
| mosipid/print                                                   | [1.2.0.1](https://github.com/mosip/print/tree/v1.2.0.1)                                                                                                      |
| mosipid/commons-packet-service                                  | [1.2.0.1](https://github.com/mosip/packet-manager/tree/v1.2.0.1/commons-packet/commons-packet-service)                                                       |
| mosipid/mosip-artemis-keycloak                                  | [1.2.0.1](https://github.com/mosip/keycloak/tree/v1.2.0.1/keycloak-artemis)                                                                                  |
| mosipid/websub-service                                          | [1.2.0.1](https://github.com/mosip/websub/tree/v1.2.0.1)                                                                                                     |

## Documentation

* [Resident Services Developers Guide](https://docs.mosip.io/1.2.0/modules/resident-services/resident-services-developer-guide)
* [Resident Services UI Developers Guide](https://docs.mosip.io/1.2.0/modules/resident-services/resident-services-ui-developer-guide)
* [Resident Portal Configuration Guide](https://docs.mosip.io/1.2.0/modules/resident-services/resident-portal-configuration-guide)
* [Resident Services Deployment Guide](https://docs.mosip.io/1.2.0/modules/resident-services/resident-services-deployment-guide)
* [Configuring Resident OIDC Client](https://docs.mosip.io/1.2.0/modules/resident-services/resident-services-configure-resident-oidc-client)
* [Resident Portal User Guide](https://docs.mosip.io/1.2.0/modules/resident-services/resident-portal-user-guide)
* [Functional Overview](https://docs.mosip.io/1.2.0/modules/resident-services/functional-overview)
* [API Documentation](https://mosip.stoplight.io/docs/resident/9a5192571fc51-document)
* [Test Report](test-report.md)
