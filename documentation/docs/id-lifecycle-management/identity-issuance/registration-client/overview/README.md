# Overview

The Registration Client is a thick Java-based client where the resident's demographic and biometric details are captured along with the supporting documents in online or offline mode. Data is captured in the form of registration packets and is cryptographically secured to ensure that there is no tampering. The captured information is packaged and sent to the server for further processing.

MOSIP provides a reference implementation of a Java-based Registration Client. The code, and build files for the Registration Client are available in the [Registration Client repo](https://github.com/mosip/registration-client).

## Multiple language support

* Registration Client is featured to allow an operator to choose the operation language. The option to select their preferred language is provided on the login screen.
* Data collection during registration client supports more than one language at a time.
* Before starting any registration process, the operator can choose the languages amongst the configured ones.

To know more about setting up the reference registration client, refer to the [Registration Client Installation Guide](https://docs.mosip.io/1.2.0/modules/registration-client/registration-client-installation-guide).

To know more about the features present in the Registration Client, refer to the [Registration Client User Guide](https://docs.mosip.io/1.2.0/modules/registration-client/registration-client-user-guide).

## Who operates the Registration Client?

The Registration Client can be operated by an operator who can be either a **Supervisor** or an **Officer**. They can login to the client application and perform various activities. The Supervisor and the Officer can perform tasks like Onboarding, Synchronize Data, Upgrade software, Export packets, Upload packets, View Re-registration packets, Correction process, Exception authentication, etc. In addition to this, the Supervisor has exclusive authority to Approve/reject registrations.

To know more about the onboarding process of an operator, refer to [Operator onboarding](../deploy/operator-onboarding.md).

## Registration Client entity diagram

![](../../../../.gitbook/assets/reg-client.drawio.png)

The relationship of the Registration Client with other services is explained here. _NOTE_: The numbers do not signify a sequence of operations or control flow.

1. Registration Client connects to the Upgrade Server to check on upgrades and patch downloads.
2. All the masterdata and configurations are downloaded from SyncData-service.
3. Registration Client always connects to external biometric devices through SBI.
4. Registration Client scans the document proofs from any document scanner.
5. Acknowledgment receipt print request is raised to any connected printers.
6. Packets ready to be uploaded meta-info are synced to the Sync Status service. Also, the status of already uploaded packets is synced back to the registration Client.
7. All the synced packets are uploaded to the Packet Receiver service one by one.

The image below shows the setup of the Registration Client Host machine.

![](../../../../.gitbook/assets/reg-client-host-machine.png)

1. Registration Client comprises JavaFX UI, Registration-services libraries, and any third-party biometric-SDK.
2. SBI is allowed to run on loopback IP and should listen on any port within the 4501-4600 range. More than one SBI can run on the host machine. Registration Client scans the allowed port range to identify the available SBI.
3. Registration Client connects to the local Derby database. This is used to store all the data synced.
4. All the completed registration packets are stored under the packetstore directory.
5. `.mosipkeys` directory is used to store sensitive files. `db.conf` under this directory stores encrypted DB passwords. This is created at the start of the registration client for the first time.
6. TPM - is the hardware security module used to get machine identifier, secure DB password, and prove the client authenticity on auth requests and packets created in the host machine.

## Data protection

* The registration packets and synced data are stored in the client machine.
* Most of the synced data are stored in the Derby DB. Derby DB is encrypted with the bootpassword.
* Derby DB boot password is encrypted with the machine TPM key and stored under `.mosipkeys/db.conf`.
* Synced UI-SPEC/script files are saved in plain text under the registration client working directory. During sync, SPEC/script file hash is stored in derby and then the files are saved in the current working directory. Every time the file is accessed by the client performs the hash check.
* Synced pre-registration packets are encrypted with a TPM key and stored under the configured directory.
* The directory to store the registration packets and related registration acknowledgments is configurable.
* The registration packet is a signed and encrypted ZIP.
* Registration acknowledgment is also signed and encrypted with a TPM key.

## Background Tasks

The Registration Client runs background tasks to keep data synchronized with the Registration Processor. It continuously updates the server with newly created packets and syncs additional metadata to improve packet recovery in case of a client failure.

Another background task handles packet uploads. If supervisor approval is required (`'y'`), approved packets are uploaded in batches. If approval is not required (`'n'`), packets are uploaded during the next scheduled job. With this feature, the registration client has fully capable auto upload.

You can configure these settings in the [Scheduled Jobs](../deploy/registration-client-configuration.md#scheduled-jobs) and [Batch Configuration](../deploy/registration-client-configuration.md#batch-size) sections.

## Configurations

Registration Client can be customized as per a country's requirements. For details related to Registration Client configurations, refer to [Registration Client configuration](https://docs.mosip.io/1.2.0/modules/registration-client/registration-client-configuration).

## UI Specifications for Registration Tasks

* The blueprint of registration forms to be displayed in the registration client is created as json called UI-SPEC.
* Every process ( NEW / LOST / UPDATE UIN / CORRECTION ) has its own UI-SPEC json.
* Kernel-masterdata-service exposes APIs to create and publish UI-SPEC.
* Published UI-SPEC json are versioned.
* Only published UI-SPEC is synced into registration-client.
* UI-SPEC json files are tamper proof, the client checks the stored file hash every time it tries to load the registration UI.
* UI-SPEC json will fail to load if tampered.

Default UI Specifications loaded with Sandbox installation is available [here](https://github.com/mosip/mosip-infra/blob/1.2.0-rc2/deployment/v3/mosip/kernel/masterdata/xlsx/ui_spec.xlsx)

## Developer Guide

To know more about the developer setup, read the [Registration Client Developers Guide](https://docs.mosip.io/1.2.0/modules/registration-client/registration-client-developers-guide).
