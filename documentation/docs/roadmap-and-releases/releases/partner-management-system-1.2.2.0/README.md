# Partner Management System 1.2.2.0

## Release : 1.2.2.0

**Release Name**: Partner Management System Revamp

**Release Number**: 1.2.2.0

**Release Date**: 10th March, 2025

## Overview

We are excited to announce the launch of the **new UI of Partner Management System** which is comprehensive release that includes all the features from the legacy PMS UI and brings significant improvement over the earlier one 1.3.0-dp.1. Now out, this also brings substantial upgrades to the UX and the UI which now has enhanced capabilities.

* The new version **1.2.2.0** is a full feature release, serving as a continuation of the earlier developer preview (**1.3.0-dp.1**).
* This release brings **Technology stack upgrade** for improved performance and security.
* This release brings expanded system capabilities to **various Partner Types (Authentication, Device & FTM Chip Provider) and Partner Admin/ Policy Manager Roles**.
* **New functionalities** now spans not only a particular part of the PMS but it is ground up improvement that we pulled off on every section being released this time.
* **Feature enhancements** comes to majority of features which existed and certainly the new ones and this helps you streamline partner management like never before.
* **Improved usability & user experience** for better navigation and efficiency. UX and UI has been worked upon ground up, The user flow has now been structured and it is easy and quick to get familiar with the interface helping users identify a logical/consistent pattern in usage.

This release officially launches **PMS version 1.2.2.0**, focusing on the implementation of **Device Provider** and **FTM Chip Provider** partner types along with an enhanced **Partner Admin/Policy Manager workflow** in the new UI.

This version of PMS is designed to run on 1.2.0.1 version of MOSIP platform.

The comprehensive release of PMS 1.2.2.0 encompasses improvements done on almost all the aspects/components of the PMS and which includes following:

* PMS for Partner User
* PMS for Partner Admin

### PMS for Partner User

The current release has focused on the feature enhancement for Device Provider and FTM Chip Provider which is in addition to the 'Authentication Partner' which we worked upon with last release.

#### The key features of Device Provider

* **Partner Certificate:**
  * **Upload and Re-upload:** Easily upload or re-upload Certificate Authority (CA) signed Partner Certificate.
  * **Download:** Download CA signed Partner Certificate and corresponding MOSIP Signed Certificate.
* **Device Provider Services:**
  * **SBI - Device Creation:** Add SBI, Add Devices associated to an SBI. SBI / Device submissions will go to Partner Admin for approval.
  * **Deactivate:** Deactivate SBI or Deactivate mapped devices
  * **View** SBI and its associated devices

#### The key features of FTM Chip Provider

* **Partner Certificate:**
  * **Upload and Re-upload:** Easily upload or re-upload Certificate Authority (CA) signed Partner Certificate.
  * **Download:** Download CA signed Partner Certificate and corresponding MOSIP Signed Certificate.
* **FTM Chip Services:**
  * **FTM Chip details:** Add FTM details, deactivate FTM details.
  * **FTM Chip Certificate:** Upload, Re-upload or download certificate.

### PMS for Partner Admin

Partner Admin has gone under a whole sum overhaul and here we enlist what all is transformed.

#### Admin dashboard

The release brings a refreshed new user experience to the Admin dashboard which now has structured the high level functionalities with help of cards and which are ordered logically on the dashboard.

* The dashboard displays the cards for **Certificate Trust Store**, **Partners, Policies**, **Parter Policy-Linking**, **SBI-Device**, **FTM Chip** and **Authentications Services**.
* It also displays pending requests count for Partner Policy-Linking, SBI-Device and FTM Chip.

#### Certificate Trust Store

Upload, Download or View (List View and Detailed view) of Root and Intermediate Certificates.

#### Partners

Download (Original & MOSIP Signed Certificate), Deactivate Partner or View (List and Detailed View)

#### Policies

You can use this section for Policy Group and Policy related operations enlisted here:

* Create **Policy Group**
* Create **Policy**
  * Authentication Policy
  * Datashare Policy
* **Clone Policy** in different Policy Group
* **Publish** Policy in draft status
* **Edit** Policy in draft status
* **View**
  * **List View**
  * **Detail View**
* **Deactivate** Policy Group (if no active policy is linked)
* **Deactivate** Policy (if no active partner policy mappings)

#### Partner Policy Linking

Approve / Reject or View (List View and Details View)

#### Authentication Services

OIDC Client: View (List and Details View) or Deactivate

API Key: View (List and Details View) or Deactivate

#### SBI-Device

SBI: View (List and Details View), **Approve / Reject** or **Deactivate**

Device: View (List and Details View), **Approve / Reject** or **Deactivate**

#### FTM Chip

View (List and Details View), **Approve / Reject** or **Deactivate**.

### Browser Support

* Complete support on **Chrome, Firefox, Edge and Safari** ensures a seamless user experience across all these popular browsers.

### Language Support

* The system offers multilingual support, with resource bundles available in three languages: English, Arabic, and French. Additional languages can be easily integrated by following the guidelines provided in the '[New Language Support](../../../id-lifecycle-management/support-systems/partner-management-services/develop/new-language-support.md)' documentation.

### Compatibility

* Responsive UI design for laptop/desktop views, optimized for standard browser sizes (laptop/desktop/tablet/larger screens).

For a comprehensive and detailed description of all the features, 'refer to [Feature Documentation](https://docs.mosip.io/1.2.0/modules/partner-management-services/pms-revamp/functional-overview/auth-partner/features)'.

To know more about the upcoming features planned as part of PMS Revamp for this year, please check out [Roadmap 2025](https://docs.mosip.io/1.2.0/roadmap-and-releases/roadmap/roadmap-2025#inji-stack-1).

### Known Issues

Below is the list of key [known issues](https://mosip.atlassian.net/issues/?filter=12233), For more details on all the known issues, please refer [here](https://mosip.atlassian.net/issues/?filter=12233\&jql=project%20%3D%20MOSIP%20AND%20%22Epic%20Link%22%20%3D%20MOSIP-32075%20and%20labels%3Dknown_issue%20and%20%22Severity%5BDropdown%5D%22%20in%20%28Blocker%2C%20critical%2C%20major%2C%20minor%29%20and%20status%20not%20in%20%28Closed%29).

| **Jira Issue**                                                | **Issue Description**                                                                                                                                                                                                                                                                                                                                       |
| ------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| [MOSIP-38890](https://mosip.atlassian.net/browse/MOSIP-38890) | Partner domain dropdown items (AUTH/ DEVICE/ FTM) are in English and do not support multi-language.                                                                                                                                                                                                                                                         |
| [MOSIP-34427](https://mosip.atlassian.net/browse/MOSIP-34427) | <p>On deactivating an API key from one browser , the status still remains 'Activated' on viewing the same API Key details in another browser.</p><p>This is occurring due to caching. Hence user is expected to reload the tabular page of API Keys to see the latest status in View API Key screen.</p>                                                    |
| [MOSIP-34109](https://mosip.atlassian.net/browse/MOSIP-34109) | <p>Length validation of OIDC Client name is not functioning as expected for lengthy names within the given range. This has a dependency with eSignet, where the column size needs to be increased.</p><p>Its suggested that meaningful and reasonable length be utilised for OIDC Client name.</p>                                                          |
| [MOSIP-39623](https://mosip.atlassian.net/browse/MOSIP-39623) | <p>Unable to select policy group in tablet and mac devices.</p><p>This issue is found when more than 3000 Policy Groups are getting loaded into the Dropdown for selection. As a workaround, suggested to keep the create policy groups less than 3000.</p>                                                                                                 |
| [MOSIP-38393](https://mosip.atlassian.net/browse/MOSIP-38393) | In UI, both PARTNER\_ADMIN and POLICYMANAGER roles are necessary to access Policy features. But in API, either of the two roles are sufficient to perform any policy related functionalities.                                                                                                                                                               |
| [MOSIP-35085](https://mosip.atlassian.net/browse/MOSIP-35085) | <p>An error is thrown when public key in JWK format is entered, due to which unable to submit the details. This is faced only in Safari browser of Macbook.</p><p>As a workaround, the create OIDC Client functionality can be performed across Chrome/ Firefox/ Edge/ Safari in Windows OS or Chrome/ Firefox/ Edge in mac OS, until this is resolved.</p> |
| [MOSIP-35421](https://mosip.atlassian.net/browse/MOSIP-35421) | Add Installation scripts for each module-wise apitest.                                                                                                                                                                                                                                                                                                      |

### Repositories Released

| **Repository Released**     | **Tags**                                                                       |
| --------------------------- | ------------------------------------------------------------------------------ |
| partner-management-services | [v1.2.2.0](https://github.com/mosip/partner-management-services/tree/v1.2.2.0) |
| partner-management-portal   | [v1.2.2.0](https://github.com/mosip/partner-management-portal/tree/v1.2.2.0)   |

### Compatible Modules

The following table outlines the tested and certified compatibility of PMS 1.2.2.0 with other modules.

| **Key Manager**       | [v1.3.0-beta.2](https://github.com/mosip/keymanager/tree/v1.3.0-beta.2) |
| --------------------- | ----------------------------------------------------------------------- |
| **auth manager**      | [v1.2.0.1](https://github.com/mosip/mosip-openid-bridge/tree/v1.2.0.1)  |
| **artifactory**       | [v1.2.0.2](https://github.com/mosip/artifactory-ref-impl/tree/v1.2.0.2) |
| **IDA**               | [v1.2.1.0](https://github.com/mosip/id-authentication/tree/v1.2.1.0)    |
| **eSignet**           | [v1.4.1](https://github.com/mosip/esignet/tree/v1.4.1)                  |
| **Reg Proc**          | [v1.2.0.1](https://github.com/mosip/registration/tree/v1.2.0.1)         |
| **Notifier (Kernel)** | [v1.2.0.1](https://github.com/mosip/commons/tree/v1.2.0.1/kernel)       |
| **Audit manager**     | [v1.2.0.1](https://github.com/mosip/audit-manager/tree/v1.2.0.1)        |
| **ID Repo**           | [v1.2.1.0](https://github.com/mosip/id-repository/tree/v1.2.1.0)        |
| **datashare**         | [v1.2.0.1](https://github.com/mosip/durian/tree/v1.2.0.1)               |
| **Keycloak**          | [v1.2.0.1](https://github.com/mosip/keycloak/tree/v1.2.0.1)             |
| **config-server**     | [v1.1.2](https://github.com/mosip/mosip-config/tree/v1.1.2)             |
| **Websub**            | [v1.2.0.1](https://github.com/mosip/websub/tree/v1.2.0.1)               |



### Documentation

| **Feature**                                                                                                                                                               | **Description**                                                                                                                                                                                                                                                     |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| [Partner Management Services](https://github.com/mosip/partner-management-services/tree/release-1.2.2.x)                                                                  | For code and implementation of Partner Management Services                                                                                                                                                                                                          |
| [Partner Management System](https://github.com/mosip/partner-management-portal/tree/release-1.2.2.x)                                                                      | For code and implementation of Partner Management Portal/ UI (revamp)                                                                                                                                                                                               |
| [Partner Management System End User Guide](https://docs.mosip.io/1.2.0/modules/partner-management-services/pms-revamp/functional-overview)                                | To get started with the new interface of Partner Management System                                                                                                                                                                                                  |
| [Partner Management Services Deployment Guide](https://docs.mosip.io/1.2.0/modules/partner-management-services/pms-revamp/technical-overview/build-and-development-guide) | To access the build and read through the deployment instructions                                                                                                                                                                                                    |
| [Test Rig Documentation](https://github.com/mosip/mosip-infra/tree/v1.2.0.2/deployment/v3/testrig)                                                                        | **Note**: The deployment script for the Partner Management System (PMS) module-wise test rig will be addressed in the next release. Meanwhile, users who wish to run automation tests can refer to the guide and deploy using the image mosipid/apitest-pms:1.2.2.0 |
| [PMS Revamp Configuration Guide](https://docs.mosip.io/1.2.0/modules/partner-management-services/pms-revamp/technical-overview/pms-configuration-guide)                   | For details related to partner management services revamp configurations                                                                                                                                                                                            |
| [UI Developer's Guide](https://docs.mosip.io/1.2.0/modules/partner-management-services/pms-revamp/technical-overview/ui-developers-guide)                                 | UI Developer's Guide                                                                                                                                                                                                                                                |
| [Backend Developer's Guide](https://docs.mosip.io/1.2.0/modules/partner-management-services/pms-revamp/technical-overview/backend-developers-guide)                       | Backend Developer's Guide                                                                                                                                                                                                                                           |
| [API Documentation](https://mosip.stoplight.io/docs/partner-management-portal-revamp)                                                                                     | API Documentation                                                                                                                                                                                                                                                   |
| [API Docs Changes](file:///wiki/spaces/PMS/pages/1577451541/PMS+Revamp1.2.2.0+-+Changes+in+API+NEW)                                                                       | This topic captures all the changes that have been made to the existing API endpoints during the PMS Revamp Release 1.2.2.0. These changes include addition of new endpoints, deprecation of a few endpoints, and also some other changes.                          |

### Test Report

For details on the test results, refer [here](https://docs.mosip.io/1.2.0/roadmap-and-releases/releases/partner-management-system-1.2.2.0/test-report).
