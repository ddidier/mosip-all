# Resident Portal

### Overview

Resident services are self-service tools utilized by residents through an online portal. The [Resident Portal](https://docs.mosip.io/1.2.0/modules/resident-services/resident-portal-user-guide) is a web-based user interface application that offers services related to the residents' Unique Identification Number (UIN). Through this portal, residents can perform various operations related to their UIN or VID and can also raise any concerns they may have.

The relationship of Resident services with other services is listed below.

{% hint style="info" %}
**Note**: The numbers do not signify the sequence of operations or the control flow.
{% endhint %}



<figure><img src="../../../.gitbook/assets/rp-2.png" alt="" width="563"><figcaption></figcaption></figure>

1. **Audit Manager**: Resident services sends all the audit logs to the Audit Manager.
2. **Digital card service**: Resident services use this service to download the PDF of the UIN card or VID card.
3. **Credential Request Generator Service**: This service is used to share credentials with various partners, such as print, authentication, and digital card partners.
4. **ID Repository Identity Service**: Resident services use this service to retrieve a credential's identity information and lock/unlock authentication types.
5. **ID Repository VID service**: This service is used to generate/revoke various types of VIDs.
6. **ID Authentication**: This service is used by Resident services to authenticate users.
7. **MOSIP e-Signet**: This is used to authenticate and authorize the users in the event of login using UIN/ VID.
8. **Resident UI**: This is the interface through which users can interact with the Resident Services.
9. **WebSub**: This is used to get asynchronous notifications from IDA for acknowledgment purposes.
10. **Registration Processor**: This is used to sync and upload packets for features about changes in identity data.
11. **Packet Manager**: Resident services use this service to create packets.
12. **Partner Management Service**: Resident services use this service to get information about various partners and policies.
13. **Keycloak**: Resident services use this to authenticate to access the MOSIP internal APIs. The Resident Services communicates with endpoints of other MOSIP modules via a token obtained from Keycloak.
14. **Master data service**: Resident services invoke the Master Data services to get various templates and machine details.
15. **Notification service**: Resident services use this service to send various notifications through email or SMS.
16. **Key Manager**: Resident services use Key Manager to encrypt or decrypt the data used across features.

### Design Principles

The design of the Resident portal embodies the following principles:

* **One-stop solution**: The Resident portal is designed to have components that aim to solve all the queries, issues, or discrepancies of the residents and act as a one-stop solution for all the requirements.
* **Self-Sovereign**: Once the ID is issued by an authority, the user/resident/citizen chooses to control and manage their data in their choice of devices.
* **Inclusive**: The Resident portal aims to be available in all browsers while also catering to the needs of visually impaired, dyslexic, and color-blind folks.
* **Presence assurance**: This web-based UI application would put in all its efforts to ensure easy access to all the residents with high availability.
* **Works Remote**: The Resident portal should be able to share credentials when data needs to be shared remotely without physical presence.
* **Trusted**: The identity verification process on the device should be trusted so that it can be used in service delivery without any concerns.
* **Grievance redressal**: The Resident portal ensures that in case of any concerns or grievances, the issue is raised and resolved through the portal itself.

### Documentation

* [Release Notes](https://docs.mosip.io/1.2.0/releases/resident-services-0.9.0)
* [Resident Services Developers Guide](https://docs.mosip.io/1.2.0/modules/resident-services/resident-services-developer-guide)
* [Resident Services UI Developers Guide](https://docs.mosip.io/1.2.0/modules/resident-services/resident-services-ui-developer-guide)
* [Resident Portal Configuration Guide](https://docs.mosip.io/1.2.0/modules/resident-services/resident-portal-configuration-guide)
* [Resident Services Deployment Guide](https://docs.mosip.io/1.2.0/modules/resident-services/resident-services-deployment-guide)
* [Configuring Resident OIDC Client](https://docs.mosip.io/1.2.0/modules/resident-services/resident-services-configure-resident-oidc-client)
* [Resident Portal User Guide](https://docs.mosip.io/1.2.0/modules/resident-services/resident-portal-user-guide)
* [Functional Overview](https://docs.mosip.io/1.2.0/modules/resident-services/functional-overview)
