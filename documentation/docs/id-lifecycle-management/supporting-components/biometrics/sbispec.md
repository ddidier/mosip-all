---
hidden: true
---

# SBISpec

## Secure Biometrics Device Interface Specification

### Introduction and Background

This standard specifies a language-agnostic protocol and corresponding interfaces for biometric devices to support features such as the discovery of devices, capability exposure of the device, and capture of biometrics using the device for all instant capture modalities. This protocol also specifically addresses the trustworthiness of both the device and the captured data, in addition to data security.

#### Objective

This document provides the technical specifications of the interface and the compliance details for biometrics devices to adhere to the Secure Biometrics Device Interface (SBI) standard.

#### Target Audience

This document aims to assist biometric device manufacturers, their developers, and their designers in building SBI-compliant devices.

#### SBI Devices

All devices that collect biometric data for SBI-reliant applications should operate within the specifications of this document.

### Revision History

\| Version | State | Date | Changes | | ------- | ------ | ------- | | draft.1 | Draft | 20-Oct-2022 | Initial Draft |

### Glossary of Terms

* **Device Provider**: An entity that manufactures or imports the devices in their name. This entity should have legal rights to obtain an organization-level digital certificate from the respective authority in the country.
* **FTM Provider**: An entity that manufactures or guarantees the trustworthiness of the foundational trust module. This can be the device provider as well. **Device**: Hardware capable of capturing biometric information.
* **L1 Certified Device/L1 Device**: A device certified as capable of performing encryption in line with this specification in its trusted zone.
* **L0 Certified Device/L0 Device**: A device certified as one where the encryption is done on the host machine device driver or the SBI service.
* **FTM Provider Certificate**: A digital certificate was issued to the "Foundational Trust Provider". This certificate proves that the provider has successfully gone through the required Foundational Trust Provider evaluation. The entity is expected to keep this certificate in secure possession in an HSM. All the individual FTM trust certificates are issued using this certificate as the root. This certificate would be issued by the countries in conjunction with a trusted registry.
* **Device Provider Certificate**: A digital certificate issued to the "Device Provider". This certificate proves that the provider has been certified for L0/L1 respective compliance. The entity is expected to keep this certificate in secure possession in an HSM. All the individual trust certificates are issued using this certificate as the root. This certificate is issued by the countries in conjunction with a trusted registry.
* **Registration**: The process of applying for a Foundational ID.
* **KYC**: Know Your Customer. The process of providing consent to perform profile verification and update.
* **Auth**: The process of verifying one’s identity.
* **FPS**: Frames Per Second
* **Management Server**: A server run by the device provider to manage the life cycle of the biometric devices.
* **Device Registration**: The process of a device obtaining a certificate from the management server.
* **Signature**: All signatures should be as per [RFC 7515](https://datatracker.ietf.org/doc/html/rfc7515).
  * **Header in signature**: The header in the signature means the attribute with "alg" set to RS256 and x5c set to base64encoded certificate.
  * **Payload**: Payload is the byte array of the actual data, always represented as base64urlencoded.
  * **Signature**: Base64urlencoded signature bytes
* **ISO format timestamp**: [ISO 8601](https://www.iso.org/iso-8601-date-and-time-format.html) with format `yyyy-mm-ddTHH:MM:ssZ` (Example: 2020-12-08T09:39:37Z). This value should be in UTC (Coordinated Universal Time).

### Device Specification

The SBI device specification provides compliance guidelines to devices for them to work with SBI-reliant applications. The compliance is based on device capability, trust and communication protocols. An SBI-compliant device would follow the standards established in this document. It is expected that the devices are compliant with this specification and tested and validated. The details of each of these are outlined in the subsequent sections.

#### Device Capability

The SBI-compliant device is expected to perform the following,

* Should have the ability to collect one or more biometric
* Should have the ability to sign the captured biometric image or template.
* Should have the ability to protect secret keys
* Should have no mechanism to inject the biometric

***

### Device Trust

SBI-compliant devices provide a trusted environment for the devices to be used in registration, KYC and AUTH scenarios. The trust level is established based on the device support for trusted execution.

* L1 - The trust is provided by a secure chip with a secure execution environment.
* L0 - The trust is provided at the software level. No hardware-related trust exists. This type of compliance is used in controlled environments.

#### Foundational Trust Module (FTM)

The foundational trust module would be created using a secure microprocessor capable of performing all required biometric processing and secure storage of keys. The foundational device trust would satisfy the below requirements.

* The module can securely generate, store and process cryptographic keys.
* Generation of asymmetric keys and symmetric keys with TRNG.
* The module can protect keys from extraction.
* The module has to protect the keys from physical tampering, temperature, frequency and voltage-related attacks.
* The module could withstand Hardware cloning.
* The module could withstand probing attacks
* The module provides memory segregation for cryptographic operations and protection against buffer overflow attacks
* The module provides the ability to withstand cryptographic side-channel attacks like Differential Power analysis attacks and timing attacks.
* CAVP validated implementation of the cryptographic algorithm.
* The module can perform a cryptographically validatable secure boot.
* The module can run trusted applications.

The foundational device trust derived from this module is used to enable trust-based computing for biometric capture. The foundational device trust module provides for a trusted execution environment based on the following:

* Secure Boot
  * Ability to cryptographically verify code before execution.
  * Ability to check for integrity violations of the module/device.
  * Halt upon failure.
  * Ability to securely upgrade and perform forward-only upgrades, to thwart downgrade attacks.
  * SHA256 hash equivalent or above should be used for all hashing requirements
  * All root of trust is provisioned upon first boot or before.
  * All upgrades would be considered a success only after the successful boot with proper hash and signature verification.
  * The boot should fail upon hash/signature failures and would never operate in an intermediary state.
  * A maximum of 10 failed attempts should lock the upgrade process and brick the device. However, chip manufacturers can decide to be less than 10.
* Secure application
  * Ability to run applications that are trusted.
  * Protect against the downgrading of applications.
  * Isolated memory to support cryptographic operations.
  * All trust is anchored during the first boot and not modifiable.

**Certification**

The FTM should have at least one of the following certifications in each category to meet the given requirement.

**Category: Cryptographic Algorithm Implementation**

* CAVP (RSA, AES, SHA256, TRNG (DRBGVS), ECC)

**Note:**

The supported algorithm and curves are listed here.

**Category: FTM Chip**

(ONE of the following certifications)

* FIPS 140-2 L3 or above
* PCI PTS 5 or above (Pre-certified)
* PCI - PED 2.0 or above (Pre-Certified)
* One of the following Common Criteria (CC) certification
  * https://www.commoncriteriaportal.org/files/ppfiles/pp0035a.pdf
  * https://www.commoncriteriaportal.org/files/ppfiles/pp0084a\_pdf.pdf

**System/Device Level Tamper (optional)**

System/Device Level Tamper Responsiveness is recommended (not mandatory). In this case, FTM should be capable of showcasing Tamper Responsiveness (keys must be erased) against a tamper at the system/device level.

**Threats to Protect**

The FTM should protect against the following threats.

* Hardware cloning attacks - Ability to protect against attacks that could result in a duplicate with keys.
* Hardware Tamper attacks
  * Physical tamper - No way to physically tamper and obtain its secrets.
  * Voltage & frequency related attacks - Should shield against voltage leaks and should prevent low voltage. The FTM should always be in either of the state operational normally or inoperable. The FTM should never be operable when its input voltages are not met.
  * Temperature attacks on the crypto block - Low or High the FTM is expected to operate or reach an inoperable state. No state in between.
* Differential Power Analysis attack.
* Probing attacks - FTM should protect its surface area against any probe-related attacks.
* Segregation of memory for execution of cryptographic operation (crypto block should be protected from buffer overflow type attacks).
* Vulnerability of the cryptographic algorithm implementation.
* Attacks against secure boot & secure upgrade.
* TEE/Secure processor OS attack (if applicable).

**Foundational Trust Module Identity**

Upon FTM provider approval by the adopters, the FTM provider would submit a self-signed public certificate to a trusted registry. Let us call this the FTM root. The adopter would use this certificate to seed their device's trust database. The FTM root and their key pairs should be generated and stored in FIPS 140-2 Level 3 or more compliant devices with no possible mechanism to extract the keys. The foundational module upon its first boot is expected to generate a random asymmetric key pair and provide the public part of the key to obtain a valid certificate. The FTM provider would validate to ensure that the chip is unique and would issue a certificate with the issuer set to an FTM certificate chain. The entire certificate issuance would be in a secured provisioning facility. Auditable upon notice by the adopters or its approved auditors. The certificate issued to the module will have a defined validity period as per the application certificate policy document defined by the application adopters. This certificate and private key within the FTM chip are expected to be in its permanent memory.

**Note:**

The validity of the chip certificate can not exceed 20 years from the date of manufacturing.

#### Device

SBI devices are most often used to collect biometrics. The devices are expected to follow the specifications for all levels of compliance and their usage. The SBI devices have a Trust Level where the device is expected to be whitelisted with a fully capable PKI and secure storage of keys at the hardware.

* L0 - A device can obtain L0 certification when it uses a software-level cryptographic library with no secure boot or FTM. These devices will follow different device identities and the same would be mentioned as part of exception flows.
* L1 - A device can obtain L1 certification when it is built in a secure facility with one of the certified FTMs.

**Device Identity**

All devices that connect to an SBI-reliant application must be identifiable. The SBI specification relies on cryptographic Identity as its basis for trust.

**Physical ID**

An identification mark that shows SBI compliance and a readable unique device serial number (minimum of 12 alphanumeric characters), make and model. The same information has to be available over a 2D QR Code or Barcode. This is to help field support and validation.

**Digital ID**

A digital device ID in SBI would be a signed JSON (RFC 7515) as follows:

```json
{
  "serialNo": "Serial number",
  "make": "Make of the device",
  "model": "Model of the device",
  "type": "Type of the biometric device",
  "deviceSubType": "Subtypes of the biometric device",
  "deviceProvider": "Device provider name",
  "deviceProviderId": "Device provider id",
  "dateTime": "Current datetime in ISO format"
}
```



Signed with the JSON Web Signature (RFC 7515) using the "Foundational Trust Module" Identity key, this data is the fundamental identity of the device. Every SBI-compliant device will need the foundational trust module.

The only exception to this rule is for the L0-compliant devices that have the purpose of "Registration". L0 devices would sign the Digital ID with the device key.

A signed digital ID would look as follows:

```
"digitalId": "base64urlencoded(header).base64urlencoded(payload).base64urlencoded(signature)"
```

The header in the digital ID would have:

```
"alg": "RS256",
"typ": "JWT",
"x5c": "<Certificate of the FTM chip, If in case the chain of certificates are sent then the same will be ignored">
```

SBI assumes that the first certificate in the x5c is the FTM's chip public certificate issued by the FTM root certificate.

An unsigned digital ID would look as follows:

```
"digitalId": "base64urlencoded(payload)"
```

Payload is the Digital ID JSON object.

**Note:**

For an L0 unregistered device, the digital ID will be unsigned. In all other scenarios, except for a discovery call, the digital ID will be signed either by the chip key (L1) or the device key (L0).

**Accepted Values for Digital ID**

| Parameters     | Description                                                                                                                                                                               |
| -------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| serialNo       | <ul><li>Serial number of the device.</li><li>This value should be the same as printed on the device (refer Physical ID).</li></ul>                                                        |
| make           | <ul><li>Brand name.</li><li>This value should be the same as printed on the device (Refer Physical ID).</li></ul>                                                                         |
| model          | <ul><li>Model of the device.</li><li>This value should be the same as printed on the device (refer Physical ID).</li></ul>                                                                |
| type           | <ul><li>Currently allowed values for device type are "Finger", "Iris" or "Face".</li><li>More types can be added based on Adopter's implementation.</li></ul>                             |
| deviceSubType  | <ul><li>Device Subtype is based on the device type.</li><li>For Finger - "Slap", "Single" or "Touchless"</li><li>For Iris - "Single" or "Double"</li><li>For Face - "Full face"</li></ul> |
| deviceProvider | <ul><li>Name of the device provider.</li><li>Device provider should be a legal entity in the country.</li></ul>                                                                           |
| dateTime       | <ul><li>Current time during the issuance of the request.</li><li>This is in ISO format.</li></ul>                                                                                         |

#### Keys

List of keys used in the device and their explanation.

* **Device Key**

Each biometric device would contain an authorized private key after the device registration. This key is rotated frequently based on the requirement of the SBI-reliant application. By default, SBI recommends a 30-day key rotation policy for the device key. The device keys are created by the device providers inside the FTM during a successful registration. The device keys are used for signing the biometric. More details of the signing and its usage will be here. This key is issued by the device provider and the certificate of the device key is issued by the device provider key which in turn is issued by the SBI-reliant application after approval of the device provider's specific model.

* **FTM Key**

The FTM key is the root of the identity. This key is created by the FTM provider during the manufacturing/provisioning stage. This is a permanent key and would never be rotated. This key is used to sign the Digital ID.

* **Application Key**

The Application key is the public key provided by the SBI-reliant application. This key is used to encrypt the biometric. Details of the encryption are listed below. We recommend rotating this key every 1 year.

### Device Service - Communication Interfaces

The section explains the necessary details of the biometric device connectivity, accessibility, discover-ability and protocols used to build and communicate with the device.

The device should implement only the following set of APIs. All the APIs are independent of the physical layer and the operating system, with the invocation being different across operating systems. While the operating system names are defined in this spec a similar technology can be used for unspecified operating systems. It is expected that the device service ensures that the device is connected locally to the host.

#### Device Discovery

Device discovery would be used to identify SBI-compliant devices in a system by the applications. The protocol is designed as a simple plug-and-play with all the necessary abstractions to the specifics.

**Device Discovery Request**

```json
{
  "type": "type of the device"
}
```

**Accepted Values for Device Discovery Request**

* type - "Biometric Device", "Finger", "Face", "Iris"

**Note:**

"Biometric Device" - is a special type used in case you are looking for any biometric device.

**Device Discovery Response**

```json
[
  {
    "deviceStatus": "Device status",
    "certification": "Certification level",
    "serviceVersion": "Device service version",
    "deviceSubId": ["Array of supported device sub Ids"],
    "callbackId": "Base URL to reach to the device",
    "digitalId": "Unsigned Digital ID of the device",
    "serialNo": "Serial No. of the device",
    "specVersion": ["Array of supported SBI specification version"],
    "purpose": "Auth  or Registration or empty if not registered",
    "jwk": {
      "kty":"EC",
      "crv":"P-256",
      "x":"f83OJ3D2xF1Bg8vub9tLe1gHMzV76e8Tus9uPHvRVEU",
      "y":"x_FEzRu9m36HLN_tue659LNpXW6pCyStikYjKIWI5a0",
      "kid":"kid of the public key used"
      //Additonal key parameters
      //The jwk should adhere to RFC7517
    },
    "error": {
      "errorCode": "101",
      "errorInfo": "Invalid JSON Value Type For Discovery.."
    }
  },
  ...
]
```

**Accepted Values for Device Discovery Response**

| Parameters      | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| --------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| deviceStatus    | <ul><li>Allowed values are "Ready", "Busy", "Not Ready" or "Not Registered".</li><li>Error codes should be sent for devices in "Busy", "Not Ready" and "Not Registered" from the below error code section.</li><li><p></p><ul><li></li></ul></li></ul>                                                                                                                                                                                                                                                                                                                  |
| certification   | Allowed values are "L0" or "L1" based on the level of certification.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
| serviceVersion  | Device service version.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| deviceSubId     | <ul><li>Allowed values are 0, 1, 2 or 3.</li><li>The device sub id could be used to enable a specific module in the scanner appropriate for a biometric capture requirement.</li><li>Device sub id is a simple index which always starts with 1 and increases sequentially for each sub-device present.</li><li>In the case of Finger/Iris it's 1 for left slap/iris, 2 for right slap/iris and 3 for two thumbs/irises.</li><li>The device sub id should be set to 0 if we don't know any specific device sub id (0 is not applicable for fingerprint slap).</li></ul> |
| callbackId      | <ul><li>This differs as per the OS.</li><li>In the case of Linux and Windows operating systems it is an HTTP URL.</li><li>In the case of android, it is the intent name.</li><li>In IOS, it is the URL scheme.</li><li>The call-back URL takes precedence over future requests as a base URL.</li></ul>                                                                                                                                                                                                                                                                 |
| digitalId       | Digital ID as per the Digital ID definition but it will not be signed.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| serialNo        | Serial No. of the device.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
| specVersion     | Array of supported SBI specification version. The array element zero will always contain the spec version using which the response is created.                                                                                                                                                                                                                                                                                                                                                                                                                          |
| purpose         | <ul><li>The purpose of the device in the SBI ecosystem.</li><li>For devices that are not registered the purpose is empty.</li><li>Allowed values for registered devices are "Auth" or "Registration".</li></ul>                                                                                                                                                                                                                                                                                                                                                         |
| jwk             | <ul><li>Contains the public key from the SBI application used for encrypting any data using <a href="https://www.rfc-editor.org/rfc/rfc2631">Diffie-Hellman Key Exchange</a> algorithm.</li><li>This attribute stores the key details as a JSON Web Key (JWK) following <a href="https://www.rfc-editor.org/rfc/rfc7517">RFC7517</a>.</li></ul>                                                                                                                                                                                                                         |
| error           | Relevant errors as defined under the error section of this document.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
| error.errorCode | Standardized error code defined in the error code section.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| error.errorInfo | Description of the error that can be displayed to the end user. Multi-lingual support.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |

**Notes:**

* The response is an array that we could have a single device enumerating with multiple biometric options.
* The service should ensure to respond only if the type parameter matches the type of device or the type parameter is a "Biometric Device".
* This response is a direct JSON as shown in the response.

**Windows/Linux**

All the device API will be based on the HTTP specification. The device always binds to any of the available ports ranging from `4501 - 4600`. The IP address used for binding has to be `127.0.0.1` and not `localhost`.

The applications that require access to SBI devices could discover them by sending the HTTP request to the supported port range. We will call this port the device\_service\_port in the rest of the document.

Any/All calls to the HTTP endpoint from the browser start with the `OPTIONS` verb due to the CORS constraint on the browser. This will be used to provide consent. This consent allows the respective domain to connect to the biometric device. The SBI Client is expected to ask the user for the approval. The approval request should clearly articulate the full domain of the requestor. Upon successful approval by the user remaining APIs are allowed. The header `Access-Control-Allow-Origin: <origin>` should be set explicitly to the domain that is making the request.

_**HTTP Request:**_

```
OPTIONS http://127.0.0.1:<device_service_port>/device
Origin: https://consent.domain.biometric.io
EXT: <app name in case Origin is not present>
Access-Control-Request-Method: POST
Access-Control-Request-Headers: EXT, Content-Type

```

`Ext` is an optional header and would be used in case Origin headers are not available. This header is mostly used by non-browser-based clients.

`Access-Control-Request-Method` list of HTTP methods for which the access is requested. In our specification, we will fix this to `POST`

`Access-Control-Request-Headers` Headers that the client will send as part of the request for which the access is requested. This has to be set to `POST`

_**HTTP Response:**_

```
HTTP 204 No Content
Access-Control-Allow-Origin: https://consent.domain.biometric.io
Access-Control-Allow-Methods: POST, OPTIONS
Access-Control-Allow-Headers: EXT, Content-Type, Cache-Control
Access-Control-Max-Age: 5000 
```

`Access-Control-Allow-Origin` Origin for which the consent is requested.

`Access-Control-Allow-Headers` confirms that these are permitted headers to be used with the actual request. It is a comma-separated list of acceptable headers.

`Access-Control-Allow-Methods` confirms that these are permitted methods to be used with the actual request. It is a comma-separated list of acceptable methods. We will limit this to `POST, OPTIONS`.

`Access-Control-Max-Age` gives the value in seconds for how long the response to the preflight request can be cached without sending another preflight request. The default value is 5 seconds. Our specification will force this to be 5 seconds.

_**HTTP Request:**_

```
POST http://127.0.0.1:<device_service_port>/device
HOST: 127.0.0.1: <device_service_port>
EXT: <app name/domain name>
```

_**HTTP Response:**_

```
HTTP/1.1 200 OK
Cache-Control:no-store
Location:http://127.0.0.1:<device_service_port>/device
Content-Length: length in bytes of the body
Content-Type: application/json
Connection: Closed
```

**Notes:**

* The payloads are JSON in both cases and are part of the body.
* CallbackId would be set to the `http://127.0.0.1:<device_service_port>/`.

**Android**

For details on Android specifications please view the section - Android SBI Specification.

#### Device Info

The device information API would be used to identify the SBI-compliant devices and their status by the applications.

**Device Info Request**

NA

**Accepted Values for Device Info Request**

NA

**Device Info Response**

```json
[
  {
    "deviceInfo": {
      "deviceStatus": "Current status",
      "firmware": "Firmware version",
      "certification": "Certification level",
      "serviceVersion": "Device service version",
      "deviceSubId": ["Array of supported device sub Ids"],
      "callbackId": "Baseurl to reach to the device",
      "digitalId": "Signed digital ID as described in the digital ID section of this document.",
      "serialNo": "Serial No. of the device",
      "env": "Target environment",
      "purpose": "Auth  or Registration",
      "specVersion": ["Array of supported SBI specification version"],
      "jwk": {
        "kty":"EC",
        "crv":"P-256",
        "x":"f83OJ3D2xF1Bg8vub9tLe1gHMzV76e8Tus9uPHvRVEU",
        "y":"x_FEzRu9m36HLN_tue659LNpXW6pCyStikYjKIWI5a0",
        "kid":"kid of the public key used"
        //Additonal key parameters
        //The jwk should adhere to RFC7517
      },
    },
    "error": {
      "errorCode": "101",
      "errorInfo": "Invalid JSON Value "
    }
  },
  ...
]
```

So the API would respond in the following format.

```json
[
  {
    "deviceInfo": "base64urlencode(header).base64urlencode(payload).base64urlencode(signature)",
    "error": {
      "errorCode": "100",
      "errorInfo": "Device not registered. In this case, the device info will be only base64urlencode(payload)"
    }
  }
]
```

**Allowed values for Device Info Response**

| Parameters                | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| ------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| deviceInfo                | <ul><li>The deviceInfo object is sent as JSON Web Token (JWT).</li><li>For a device which is not registered, the deviceInfo will be unsigned.</li><li>For a device which is registered, the deviceInfo will be signed using the device key.</li></ul>                                                                                                                                                                                                                                                                                                                   |
| deviceInfo.deviceStatus   | <ul><li>This is the status of the device.</li><li>Allowed values are "Ready", "Busy", "Not Ready" or "Not Registered".</li><li>Error codes should be sent for devices in "Busy", "Not Ready" and "Not Registered" from the below error code section.</li></ul>                                                                                                                                                                                                                                                                                                          |
| deviceInfo.firmware       | <ul><li>Exact version of the firmware.</li><li>In case of L0 this is same as serviceVersion.</li></ul>                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| deviceInfo.certification  | <ul><li>Allowed values are "L0" or "L1" based on the level of certification.</li></ul>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| deviceInfo.serviceVersion | Device service version.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| deviceInfo.serialNo       | Serial No. of the device.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
| deviceSubId               | <ul><li>Allowed values are 0, 1, 2 or 3.</li><li>The device sub id could be used to enable a specific module in the scanner appropriate for a biometric capture requirement.</li><li>Device sub id is a simple index which always starts with 1 and increases sequentially for each sub-device present.</li><li>In the case of Finger/Iris it's 1 for left slap/iris, 2 for right slap/iris and 3 for two thumbs/irises.</li><li>The device sub id should be set to 0 if we don't know any specific device sub id (0 is not applicable for fingerprint slap).</li></ul> |
| deviceInfo.callbackId     | <ul><li>This differs as per the OS.</li><li>In the case of Linux and Windows operating systems it is an HTTP URL.</li><li>In the case of Android, it is the intent name.</li><li>In IOS, it is the URL scheme.</li><li>The call-back URL takes precedence over future requests as a base URL.</li></ul>                                                                                                                                                                                                                                                                 |
| deviceInfo.digitalId      | <ul><li>The digital id as per the digital id definition.</li><li>For L0 devices which is not registered, the digital ID will be unsigned.</li><li>For L0 devices which is registered, the digital ID will be signed using the device key.</li><li>For L1 devices, the digital ID will be signed using the FTM key.</li></ul>                                                                                                                                                                                                                                            |
| deviceInfo.env            | <ul><li>The target environment.</li><li>For devices that are not registered the environment is "None".</li><li>For a device that is registered, then send the environment in which it is registered.</li><li>Allowed values for the registered device are "Staging", "Developer", "Pre-Production" or "Production".</li></ul>                                                                                                                                                                                                                                           |
| deviceInfo.purpose        | <ul><li>The purpose of the device in the SBI ecosystem.</li><li>For devices that are not registered the purpose is empty.</li><li>Allowed values for registered devices are "Auth" or "Registration".</li></ul>                                                                                                                                                                                                                                                                                                                                                         |
| deviceInfo.specVersion    | Array of supported SBI specification version. The array element Zero will always contain the spec version using which the response is created.                                                                                                                                                                                                                                                                                                                                                                                                                          |
| jwk                       | <ul><li>Contains the public key from the SBI application used for encrypting any data using <a href="https://www.rfc-editor.org/rfc/rfc2631">Diffie-Hellman Key Exchange</a> algorithm.</li><li>This attribute stores the key details as a JSON Web Key (JWK) following <a href="https://www.rfc-editor.org/rfc/rfc7517">RFC7517</a>.</li></ul>                                                                                                                                                                                                                         |
| error                     | Relevant errors as defined under the error section of this document.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
| error.errorCode           | Standardized error code defined in the error code section.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| error.errorInfo           | Description of the error that can be displayed to the end user. Multi-lingual support.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |

**Notes:**

* The response is an array that we could have a single device enumerating with multiple biometric options.
* The service should ensure to respond only if the type parameter matches the type of device or the type parameter is a "Biometric Device".

**Windows/Linux**

The applications that require more details of the SBI devices could get them by sending the HTTP request to the supported port range. The device always binds to any of the available ports ranging from 4501 - 4600. The IP address used for binding has to be 127.0.0.1 and not localhost.

_**HTTP Request:**_

```
POST http://127.0.0.1:<device_service_port>/info
HOST: 127.0.0.1:<device_service_port>
EXT: <app name>
```

_**HTTP Response:**_

```
HTTP/1.1 200 OK
CACHE-CONTROL:no-store
LOCATION:http://127.0.0.1:<device_service_port>/info
Content-Length: length in bytes of the body
Content-Type: application/json
Connection: Closed
```

**Note:**

The payloads are JSON in both cases and are part of the body.

**Android**

For details on Android specifications please view the section - Android SBI Specification.

#### Capture

The capture request would be used to capture a biometric from SBI-compliant devices by the applications. The captured call will respond with success to only one call at a time. So, in case of a parallel call, the device info details are sent with the status "Busy".

**Capture Request**

```json
{
  "env": "Target environment",
  "purpose": "Auth  or Registration",
  "specVersion": "Expected version of the SBI spec",
  "timeout": "Timeout for capture",
  "captureTime": "Capture request time in ISO format",
  "domainUri": "URI of the auth server",
  "transactionId": "Transaction Id for the current capture",
  "bio": [
    {
      "type": "Type of the biometric data",
      "count":  "Finger/Iris count, in case of face max, is set to 1",
      "bioSubType": ["Array of subtypes"],
      "requestedScore": "Expected quality score that should match to complete a successful capture",
      "deviceSubId": "Specific Device Sub Id",
      "previousHash": "Hash of the previous block"
    }
  ],
  "customOpts": {
    //Max of 50 key-value pair. This is so that vendor-specific parameters can be sent if necessary. The values cannot be hardcoded and have to be configured by the app's server and should be modifiable upon need by the applications. Vendors are free to include additional parameters and fine-tuning parameters. None of these values should go undocumented by the vendor. No sensitive data should be available in the customOpts.
  }
}
```

**Note:**

The count value should be driven by the count of the bioSubType for Iris and Finger. For Face, there will be no bioSubType but the count should be "1".

**Allowed Values for Capture Request**

| Parameters         | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| ------------------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| env                | <ul><li>The target environment.</li><li>Allowed values for registered devices are "Staging", "Developer", "Pre-Production" or "Production".</li></ul>                                                                                                                                                                                                                                                                                                                                                                                                                  |
| purpose            | <ul><li>The purpose of the device in the SBI ecosystem.</li><li>For devices that are not registered the purpose is empty.</li><li>Allowed for registered devices values is "Auth".</li></ul>                                                                                                                                                                                                                                                                                                                                                                           |
| specVersion        | Expected version of SBI specification.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| timeout            | <ul><li>Max time the app will wait for the capture.</li><li>It's expected that the API will respond before timeout with the best frame.</li><li>All timeouts are in milliseconds.</li></ul>                                                                                                                                                                                                                                                                                                                                                                            |
| captureTime        | <ul><li>Time of capture in ISO format.</li><li>The time is as per the requesting application.</li></ul>                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
| domainUri          | <ul><li>URI of the authentication server.</li><li>This can be used to federate across multiple providers countries or unions.</li></ul>                                                                                                                                                                                                                                                                                                                                                                                                                                |
| transactionId      | <ul><li>Unique ID of the transaction.</li><li>This is an internal ID to the application that's requesting a capture.</li><li>Different IDs should be used for every request.</li><li>The transactionId can have minimum length as 4 and max length as 50.</li><li>Allowed characters in transactionId are alphabets, numbers and hyphens (-).</li><li>If the above validations are not met then an error can be thrown in the error code section.</li></ul>                                                                                                            |
| bio.type           | Allowed values are "Finger", "Iris" or "Face".                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |
| bio.count          | <ul><li>Number of biometric data that is collected for a given type.</li><li>The device should validate and ensure that this number is in line with the type of biometric that's captured.</li></ul>                                                                                                                                                                                                                                                                                                                                                                   |
| bio.bioSubType     | <ul><li>For Finger: ["Left IndexFinger", "Left MiddleFinger", "Left RingFinger", "Left LittleFinger", "Left Thumb", "Right IndexFinger", "Right MiddleFinger", "Right RingFinger", "Right LittleFinger", "Right Thumb", "UNKNOWN"]</li><li>For Iris: ["Left", "Right", "UNKNOWN"]</li><li>For Face: No bioSubType</li></ul>                                                                                                                                                                                                                                            |
| bio.requestedScore | Upon reaching the quality score the biometric device is expected to auto-capture the image. If the requested score is not met, until the timeout, the best frame during the capture sequence must be captured/returned. This value will be scaled from 0 - 100 for NFIQ v1.0. The logic for scaling NFIQ v1.0 is mentioned here.                                                                                                                                                                                                                                       |
| bio.deviceSubId    | <ul><li>Allowed values are 0, 1, 2 or 3.</li><li>The device sub id could be used to enable a specific module in the scanner appropriate for a biometric capture requirement.</li><li>Device sub id is a simple index that always starts with 1 and increases sequentially for each sub-device present.</li><li>In the case of Finger/Iris it's 1 for left slap/iris, 2 for right slap/iris and 3 for two thumbs/irises.</li><li>The device sub id should be set to 0 if we don't know any specific device sub id (0 is not applicable for fingerprint slap).</li></ul> |
| bio.previousHash   | For the first capture the previousHash is the SHA256 hash of an empty UTF-8 string. From the second capture the previous capture's "hash" is used as input. This is used to chain all the captures across modalities so all captures have happened for the same transaction and during the same time.                                                                                                                                                                                                                                                                  |
| customOpts         | <ul><li>In case, the device vendor wants to send additional parameters they can use this to send a key-value pair if necessary.</li><li>The values cannot be hard coded and have to be configured by the apps server and should be modifiable upon need by the applications.</li><li>Vendors are free to include additional parameters and fine-tuning the process.</li><li>None of these values should go undocumented by the vendor.</li><li>No sensitive data should be available in the customOpts.</li></ul>                                                      |

**Capture Response**

```json
{
  "requestOrigin": "Root web domain of the application that initiated this request",
  "biometrics": [
    {
      "specVersion": "SBI spec version",
      "data": {
        "digitalId": "digital ID as described in this document",
        "serialNo": "Serial No. of the device",
        "deviceServiceVersion": "SBI version",
        "bioType": "Finger",
        "bioSubType": "UNKNOWN",
        "purpose": "Auth  or Registration",
        "env": "Target environment",
        "domainUri": "URI of the auth server",
        "bioValue": "Encrypt biodata (ISO) with random 256-bit AES key (session key) and encode encrypted biodata with base64 URL safe encoding.",
        "transactionId": "Unique transaction id",
        "timestamp": "Capture datetime in ISO format",
        "requestedScore": "Floating point number to represent the minimum required score for the capture",
        "qualityScore": "Floating point number representing the score for the current capture"
      },
      "hash": "sha256 in hex format in upper case (previous "hash" + sha256 hash of the current biometric ISO data before encryption)",
      "sessionKey": "Encrypt the session key (used to encrypt the bioValue) with Application public key and encode encrypted session key with base64 URL safe encoding.",
      "thumbprint": "SHA256 representation of the certificate (HEX encoded) that was used for encryption of session key. All texts are to be treated as uppercase without any spaces or hyphens.",
      "error": {
        "errorCode": "101",
        "errorInfo": "Invalid JSON Value"
      }
    },
    {
      "specVersion": "SBI spec version",
      "data": {
        "digitalId": "Digital ID as described in this document",
        "serialNo": "Serial No. of the device.",
        "deviceServiceVersion": "SBI version",
        "bioType": "Finger",
        "bioSubType": "Left IndexFinger",
        "purpose": "Auth  or Registration",
        "env": "target environment",
        "domainUri": "URI of the auth server",
        "bioValue": "Encrypt biodata (ISO) with random 256-bit AES key (session key) and encode encrypted biodata with base64 URL safe encoding.",
        "transactionId": "unique transaction id",
        "timestamp": "Capture datetime in ISO format",
        "requestedScore": "Floating point number to represent the minimum required score for the capture",
        "qualityScore": "Floating point number representing the score for the current capture"
      },
      "hash": "sha256 in hex format in upper case (previous "hash" + sha256 hash of the current biometric ISO data before encryption)",
      "sessionKey": "Encrypt the session key (used to encrypt the bioValue) with the Application public key and encode the encrypted session key with base64 URL safe encoding.",
      "thumbprint": "SHA256 representation of the certificate (HEX encoded) that was used for encryption of session key. All texts are to be treated as uppercase without any spaces or hyphens.",
      "error": {
        "errorCode": "101",
        "errorInfo": "Invalid JSON Value"
      }
    }
  ]
}
```

**Accepted Values for Capture Response**

| Parameters                | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
| ------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| requestOrigin             | <ul><li>Value of the Origin HTTP header is taken from the request, root domain name is extracted and added.</li><li>For capture requests initiated through non-HTTP means, this field should be an empty string.</li></ul>                                                                                                                                                                                                                                                                            |
| specVersion               | Version of the SBI specification using which the response was generated.                                                                                                                                                                                                                                                                                                                                                                                                                              |
| data                      | <ul><li>The data object is sent as JSON Web Token (JWT).</li><li>The data block will be signed using the device key.</li></ul>                                                                                                                                                                                                                                                                                                                                                                        |
| data.digitalId            | <ul><li>The digital ID as per the digital ID definition in JWT format.</li><li>For L0 devices, the digital ID will be signed using the device key.</li><li>For L1 devices, the digital ID will be signed using the FTM key.</li></ul>                                                                                                                                                                                                                                                                 |
| data.serialNo             | Serial No. of the device.                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| data.deviceServiceVersion | Device service version.                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
| data.bioType              | Allowed values are "Finger", "Iris" or "Face".                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| data.bioSubType           | <ul><li>For Finger: ["Left IndexFinger", "Left MiddleFinger", "Left RingFinger", "Left LittleFinger", "Left Thumb", "Right IndexFinger", "Right MiddleFinger", "Right RingFinger", "Right LittleFinger", "Right Thumb", "UNKNOWN"]</li><li>For Iris: ["Left", "Right", "UNKNOWN"]</li><li>For Face: No bioSubType</li></ul>                                                                                                                                                                           |
| data.purpose              | <ul><li>The purpose of the device in the SBI ecosystem.</li><li>Allowed values is "Auth".</li></ul>                                                                                                                                                                                                                                                                                                                                                                                                   |
| data.env                  | <ul><li>The target environment.</li><li>Allowed values are "Staging", "Developer", "Pre-Production" or "Production".</li></ul>                                                                                                                                                                                                                                                                                                                                                                        |
| data.domainUri            | <ul><li>URI of the authentication server.</li><li>This can be used to federate across multiple providers or countries or unions.</li></ul>                                                                                                                                                                                                                                                                                                                                                            |
| data.bioValue             | <p>The biometric ISO data is encrypted with a random symmetric (AES GCM) key and then base64 URL encoded. For symmetric key encryption of bioValue, (biometrics.data.timestamp XOR transactoinId) is computed, and the last 16 bytes and the last 12 bytes of the results are set as the aad and the IV (salt), respectively.<br>The ISO header in the biometric ISO is expected to have the capture timestamp as per ISO 8601 format (yyyy-mm-ddTHH:MM:ssZ) in UTC (Coordinated Universal Time).</p> |
| data.transactionId        | The same transactionId shared in the request should be used.                                                                                                                                                                                                                                                                                                                                                                                                                                          |
| data.timestamp            | <ul><li>Time as per the biometric device.</li><li>The biometric device is expected to sync its time from the management server at regular intervals so accurate time can be maintained on the device.</li><li>The timestamp should be as per ISO 8601 format (yyyy-mm-ddTHH:MM:ssZ) in UTC (Coordinated Universal Time).</li></ul>                                                                                                                                                                    |
| data.requestedScore       | Floating point number to represent the minimum required score for the capture. This value will be scaled from 0 - 100 for NFIQ v1.0. The logic for scaling NFIQ v1.0 is mentioned here.                                                                                                                                                                                                                                                                                                               |
| data.qualityScore         | Floating point number representing the score for the current capture. This value will be scaled from 0 - 100 for NFIQ v1.0. The logic for scaling NFIQ v1.0 is mentioned here.                                                                                                                                                                                                                                                                                                                        |
| hash                      | sha256 in hex format in upper case (previous "hash" + sha256 hash of the current biometric ISO data before encryption)                                                                                                                                                                                                                                                                                                                                                                                |
| sessionKey                | The session key (used for the encryption of the biodata (ISO)) is encrypted using the Application public certificate with RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING algorithm and then encoded the encrypted session key with base64 URL safe encoding.                                                                                                                                                                                                                                                   |
| thumbprint                | SHA256 representation of the certificate (HEX encoded) that was used for encryption of the session key. All texts are to be treated as uppercase without any spaces or hyphens.                                                                                                                                                                                                                                                                                                                       |
| error                     | Relevant errors as defined under the error section of this document.                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| error.errorCode           | Standardized error code defined in the error code section.                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| error.errorInfo           | Description of the error that can be displayed to the end-user. Multi-lingual support.                                                                                                                                                                                                                                                                                                                                                                                                                |

The entire data object is sent in JWT format. So, the data object will look like this:

```
"data" : "base64urlencode(header).base64urlencode(payload).base64urlencode(signature)
```

Here, **payload** is the entire byte array of the data blocks.

**Windows/Linux**

The applications that require capturing biometric data from an SBI device could do so by sending the HTTP request to the supported port range.

_**HTTP Request:**_

```
POST [http://127.0.0.1:<device_service_port>/capture](http://127.0.0.1/capture)
HOST: 127.0.0.1: <apps port>
EXT: <app name>
```

_**HTTP Response:**_

```
HTTP/1.1 200 OK
CACHE-CONTROL:no-store
LOCATION:[http://127.0.0.1](http://127.0.0.1):<device_service_port>/capture
Content-Length: length in bytes of the body
Content-Type: application/json
Connection: Closed
```

**Note:**

The payloads are JSON in both cases and are part of the body.

**Android**

For details on Android specifications please view the section - Android SBI Specification.

#### Device Stream

The device would open a stream channel to send live video streams. This would help when there is an assisted operation to collect biometrics. Please note that stream APIs are available only for the registration environment.

Used only for registration module-compatible devices. This API is visible only for the devices that are registered for `Registration`.

**Device Stream Request**

```json
{
  "deviceSubId": "Specific device sub-id",
  "timeout": "Timeout for stream"
}
```

**Allowed Values for device Stream Request**

| Parameters  | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
| ----------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| deviceSubId | <ul><li>Allowed values are 0, 1, 2, or 3.</li><li>The device sub-id could be used to enable a specific module in the scanner appropriate for a biometric capture requirement.</li><li>Device sub-id is a simple index that always starts with 1 and increases sequentially for each sub-device present.</li><li>In the case of a finger or iris, it's 1 for the left slap or iris, 2 for the right slap or iris, and 3 for two thumbs or iris.</li><li>The device sub id should be set to 0 if we don't know any specific device sub id (0 is not applicable for fingerprint slap).</li></ul> |
| timeout     | <ul><li>The maximum time after which the stream should close.</li><li>This is an optional parameter, and by default, the value will be 5 minutes.</li><li>All timeouts are in milliseconds.</li></ul>                                                                                                                                                                                                                                                                                                                                                                                         |

**Device Stream Response**

Live video stream with a quality of 3 frames per second or more using [M-JPEG](https://en.wikipedia.org/wiki/Motion_JPEG).

**Note:**

The preview should have quality markings and segment markings. The preview would also be used to display an error message on the user's screen. All error messages should be localized.

**Error Response for Device Stream**

```json
{
  "error": {
    "errorCode": "202",
    "errorInfo": "No Device Connected."
  }
}
```

**Windows/Linux**

Applications that require more details about the SBI devices could get them by sending an HTTP request to the supported port range.

_**HTTP Request:**_

```
POST   http://127.0.0.1:<device_service_port>/stream
HOST: 127.0.0.1: <apps port>
EXT: <app name>
```

_**HTTP Response:**_ HTTP Chunk of frames to be displayed. Minimum frames: 3 per second.

**Android**

For details on Android specifications, please view the section - Android SBI Specification.

#### Registration Capture

The registration client application will discover the device. Once the device is discovered, the status of the device is obtained with the device info API. During registration, the registration client sends the RCAPTURE API, and the response will provide the actual biometric data in a digitally signed, non-encrypted form. When the Device Registration Capture API is called, frames should not be added to the stream. The device is expected to send the images in ISO format.

The `requestedScore` is on a scale of 1-100 (NFIQ v2.0 for fingerprints). So, in cases where you have four fingers, the average of all will be considered for the capture threshold. The device would always send the best frame during the capture time, even if the requested score was not met.

The API is used by devices that are compatible with the registration module. This API should not be supported by devices that are compatible with authentication.

**Registration Capture Request**

```json
{
  "env":  "Target environment",
  "purpose": "Auth  or Registration",
  "specVersion": "Expected SBI spec version",
  "timeout": "Timeout for registration capture",
  "captureTime": "Time of capture request in ISO format",
  "transactionId": "Transaction Id for the current capture",
  "bio": [
    {
      "type": "Type of the biometric data",
      "count":  "Finger/Iris count, in case of face max, is set to 1",
      "bioSubType": ["Array of subtypes"], //Optional
      "exception": ["Finger or Iris to be excluded"],
      "requestedScore": "Expected quality score that should match to complete a successful capture",
      "deviceSubId": "Specific device ID",
      "previousHash": "Hash of the previous block"
    }
  ],
  "customOpts": {
    //max of 50 key-value pairs. This is so that vendor-specific parameters can be sent if necessary. The values cannot be hardcoded, have to be configured by the app's server, and should be modifiable as needed by the applications. Vendors are free to include additional parameters and fine-tuning parameters. None of these values should go undocumented by the vendor. No sensitive data should be available in the customOpts.
  }
}
```

**Note:**

To capture the exception photo, the exception value for Iris or Finger should be sent in the bio.exception for bio.type = 'Face'. ICAO checks are not mandatory here, but one face must be present within the frame.

**Accepted Values for Registration Capture Request**

| Parameters         | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
| ------------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| env                | <ul><li>The target environment.</li><li>Allowed values are "Staging", "Developer", "Pre-Production" or "Production".</li></ul>                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| purpose            | <ul><li>The purpose of the device in the SBI ecosystem.</li><li>The allowed value is "Registration".</li></ul>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| specVersion        | Expected version of SBI specification.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
| timeout            | <ul><li>The maximum time the app will wait for the capture.</li><li>It's expected that the API will respond before the timeout with the best frame.</li><li>All timeouts are in milliseconds.</li></ul>                                                                                                                                                                                                                                                                                                                                                                                                   |
| captureTime        | <ul><li>Time of capture in ISO format.</li><li>The time is as per the requesting application.</li></ul>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
| transactionId      | <ul><li>Unique ID of the transaction.</li><li>This is an internal ID for the application that's requesting a capture.</li><li>Different IDs should be used for every request.</li><li>The transactionId can have a minimum length of 4 and a maximum length of 50.</li><li>Allowed characters in transactionId are alphabets, numbers, and hyphens (-).</li><li>If the above validations are not met, then an error can be thrown in the error code section.</li></ul>                                                                                                                                    |
| bio.type           | Allowed values are "Finger", "Iris" or "Face".                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| bio.count          | <ul><li>Number of biometric data points that are collected for a given type.</li><li>The device should validate and ensure that this number is in line with the type of biometric that's captured.</li></ul>                                                                                                                                                                                                                                                                                                                                                                                              |
| bio.bioSubType     | <ul><li>Array of bioSubType for the respective biometric type.</li><li>For Finger: ["Left IndexFinger", "Left MiddleFinger", "Left RingFinger", "Left LittleFinger", "Left Thumb", "Right IndexFinger", "Right MiddleFinger", "Right RingFinger", "Right LittleFinger", "Right Thumb", "UNKNOWN"]</li><li>For Iris: ["Left", "Right", "UNKNOWN"]</li><li>For Face: No bioSubType</li><li>This is an optional parameter.</li></ul>                                                                                                                                                                         |
| bio.exception      | <ul><li>This is an array, and all the exceptions are marked.</li><li>If exceptions are sent for face, then follow the exception photo specification above.</li><li>For Finger: ["Left IndexFinger", "Left MiddleFinger", "Left RingFinger", "Left LittleFinger", "Left Thumb", "Right IndexFinger", "Right MiddleFinger", "Right RingFinger", "Right LittleFinger", "Right Thumb"]</li><li>For Iris: ["Left", "Right"]</li></ul>                                                                                                                                                                          |
| bio.requestedScore | Upon reaching the quality score, the biometric device is expected to auto-capture the image.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| bio.deviceSubId    | <ul><li>Allowed values are 0, 1, 2, or 3.</li><li>The device sub id could be used to enable a specific module in the scanner appropriate for a biometric capture requirement.</li><li>Device sub id is a simple index that always starts with 1 and increases sequentially for each sub-device present.</li><li>In the case of Finger/Iris it's 1 for left slap/iris, 2 for right slap/iris and 3 for two thumbs/irises.</li><li>The device sub id should be set to 0 if we don't know any specific device sub id (0 is not applicable for fingerprint slap).</li><li><p></p><ul><li></li></ul></li></ul> |
| bio.previousHash   | For the first capture the previousHash is the SHA256 hash of an empty UTF-8 string. From the second capture the previous capture's "hash" is used as input. This is used to chain all the captures across modalities so all captures have happened for the same transaction and during the same time.                                                                                                                                                                                                                                                                                                     |
| customOpts         | <ul><li>In case, the device vendor wants to send additional parameters they can use this to send a key-value pair if necessary.</li><li>The values cannot be hard coded and have to be configured by the apps server and should be modifiable upon need by the applications.</li><li>Vendors are free to include additional parameters and fine-tuning the process.</li><li>None of these values should go undocumented by the vendor.</li><li>No sensitive data should be available in the customOpts.</li></ul>                                                                                         |

**Registration Capture Response**

```json
{
  "requestOrigin": "Root web domain of the application that initiated this request",
  "biometrics": [
    {
      "specVersion": "SBI Spec version",
      "data": {
        "digitalId": "Digital ID of the device as per the Digital ID definition..",
        "bioType": "Biometric type",
        "serialNo": "Serial No of the device.",
        "deviceServiceVersion": "SBI version",
        "bioSubType": "Left IndexFinger",
        "purpose": "Auth  or Registration",
        "env": "Target environment",
        "bioValue": "base64urlencoded biometrics (ISO format)",
        "transactionId": "Unique transaction ID sent in request",
        "timestamp": "2019-02-15T10:01:57Z",
        "requestedScore": "Floating point number to represent the minimum required score for the capture. This ranges from 0-100.",
        "qualityScore": "Floating point number representing the score for the current capture. This ranges from 0-100."
      },
      "hash": "sha256 in hex format in upper case (previous "hash" + sha256 hash of the current biometric ISO data)",    
      "error": {
        "errorCode": "101",
        "errorInfo": "Invalid JSON Value Type For Discovery.. ex: {type: 'Biometric Device' or 'Finger' or 'Face' or 'Iris' } "
      }
    },
    {
      "specVersion": "SBI Spec version",
      "data": {
        "serialNo": "Serial No. of the device",
        "bioType" : "Finger",
        "digitalId": "Digital ID of the device as per the Digital ID definition.",
        "deviceServiceVersion": "SBI version",
        "bioSubType": "Left MiddleFinger",
        "purpose": "Auth  or Registration",
        "env":  "Target environment",
        "bioValue": "base64urlencoded extracted biometric (ISO format)",
        "transactionId": "Unique transaction ID sent in request",
        "timestamp": "2019-02-15T10:01:57Z",
        "requestedScore": "Floating point number to represent the minimum required score for the capture. This ranges from 0-100",
        "qualityScore": "Floating point number representing the score for the current capture. This ranges from 0-100"
      },
      "hash": "sha256 in hex format in upper case (previous "hash" + sha256 hash of the current biometric ISO data)",
      "error": {
        "errorCode": "101",
        "errorInfo": "Invalid JSON Value Type For Discovery.. ex: {type: 'Biometric Device' or 'Finger' or 'Face' or 'Iris' }"
      }
    }
  ]
}
```

**Allowed Values for Registration Capture Response**

| Parameters                | Description                                                                                                                                                                                                                                                                                                                        |
| ------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| requestOrigin             | <ul><li>Value of the Origin HTTP header is taken from the request, root domain name is extracted and added.</li><li>For capture requests initiated through non-HTTP means, this field should be an empty string.</li></ul>                                                                                                         |
| specVersion               | Version of the SBI specification using which the response was generated.                                                                                                                                                                                                                                                           |
| data                      | <ul><li>The data object is sent as JSON Web Token (JWT).</li><li>The data block will be signed using the device key.</li></ul>                                                                                                                                                                                                     |
| data.serialNo             | Serial No. of the device.                                                                                                                                                                                                                                                                                                          |
| data.bioType              | Allowed values are "Finger", "Iris" or "Face".                                                                                                                                                                                                                                                                                     |
| data.digitalId            | <ul><li>The digital ID as per the digital ID definition in JWT format.</li><li>For L0 devices, the digital ID will be signed using the device key.</li><li>For L1 devices, the digital ID will be signed using the FTM key.</li></ul>                                                                                              |
| data.bioSubType           | <ul><li>For Finger: ["Left IndexFinger", "Left MiddleFinger", "Left RingFinger", "Left LittleFinger", "Left Thumb", "Right IndexFinger", "Right MiddleFinger", "Right RingFinger", "Right LittleFinger", "Right Thumb", "UNKNOWN"]</li><li>For Iris: ["Left", "Right", "UNKNOWN"]</li><li>For Face: No bioSubType</li></ul>        |
| data.deviceServiceVersion | SBI Version                                                                                                                                                                                                                                                                                                                        |
| data.env                  | <ul><li>The target environment.</li><li>Allowed values are "Staging", "Developer", "Pre-Production" or "Production".</li></ul>                                                                                                                                                                                                     |
| data.purpose              | <ul><li>The purpose of the device in the SBI ecosystem.</li><li>Allowed values is "Registration".</li></ul>                                                                                                                                                                                                                        |
| data.bioValue             | <p>For rCapture, in bioValue, the base64 URL-encoded biometric ISO data is stored.<br>The ISO header in the biometric ISO is expected to have the capture timestamp as per ISO 8601 format (yyyy-mm-ddTHH:MM:ssZ) in UTC (Coordinated Universal Time).</p>                                                                         |
| data.transactionId        | The same transactionId shared in the request should be used.                                                                                                                                                                                                                                                                       |
| data.timestamp            | <ul><li>Time as per the biometric device.</li><li>The biometric device is expected to sync its time from the management server at regular intervals so accurate time can be maintained on the device.</li><li>The timestamp should be as per ISO 8601 format (yyyy-mm-ddTHH:MM:ssZ) in UTC (Coordinated Universal Time).</li></ul> |
| data.requestedScore       | Floating point number to represent the minimum required score for the capture.                                                                                                                                                                                                                                                     |
| data.qualityScore         | Floating point number representing the score for the current capture.                                                                                                                                                                                                                                                              |
| hash                      | sha256 in hex format in upper case (previous "hash" + sha256 hash of the current biometric ISO data).                                                                                                                                                                                                                              |
| error                     | Relevant errors as defined under the error section of this document.                                                                                                                                                                                                                                                               |
| error.errorCode           | Standardized error code defined in the error code section.                                                                                                                                                                                                                                                                         |
| error.errorInfo           | Description of the error that can be displayed to the end user. Multi-lingual support.                                                                                                                                                                                                                                             |

**Windows/Linux**

The applications that require more details of the SBI devices could get them by sending the HTTP request to the supported port range.

_**HTTP Request:**_

```
POST  http://127.0.0.1:<device_service_port>/rcapture
HOST: 127.0.0.1: <apps port>
EXT: <app name>
```

_**HTTP Response:**_

```
HTTP/1.1 200 OK
CACHE-CONTROL:no-store
LOCATION:[http://127.0.0.1](http://127.0.0.1):<device_service_port>/rcapture
Content-Length: length in bytes of the body
Content-Type: application/json
Connection: Closed
```

**Android**

For details on Android specifications please view the section - Android SBI Specification.

#### Match

On certain devices, we do have the ability to capture and match internally. For example, on a smartphone, we could capture the face and authenticate against the provided verifiable credential. This would allow the government to match the face on your ID card to the face in front of the camera.

**Match Request**

```json
{
  "env": "Target environment",
  "relyingPartyUri": "The URI for the relying party requesting for performing the match",
  "purpose": "Free text with restriction up to 256 characters.",
  "specVersion": "Expected SBI spec version.",
  "timeout": "Timeout for capture and match. Expecting a response within the same timeout.",
  "transactionId": "Unique transaction ID for the current match request.",
  "jwk": {
    "kty":"EC",
    "crv":"P-256",
    "x":"f83OJ3D2xF1Bg8vub9tLe1gHMzV76e8Tus9uPHvRVEU",
    "y":"x_FEzRu9m36HLN_tue659LNpXW6pCyStikYjKIWI5a0",
    "kid":"kid of the public key used"
    //Additonal key parameters
    //The jwk should adhere to RFC7517
  },
  "credentialFormat": "MIME type of the credential - jwt_vc_json/mso_mdoc/ldp_vc/jwt_vc_json-ld",
  "credential": "A valid credential object in the prescribed format. The value will be encoded based on credentialFormat. If not defined it will be base64URLencoded.",
  "captureBio":{
      "type": "Type of the biometric data.",
      "bioSubType": "biometric subType to be captured",
      "requestedScore": "Expected quality score that should match to complete a successful capture (optional)"
    },
  "matchTo":{
      "credentialElement": "Credential attribute name where the biometric is present.",
      "credentialPath": "Path of the credential element based on the prescribed credential format",
      "mimeType": "MIME type of the biometric data - JPEG2000, BMP, PNG, WSQ, FMR, Algo/Version" 
  }
  "customOpts": {
    "key": "value"
    //max of 50 key-value pair. This is so that vendor-specific parameters can be sent if necessary. The values cannot be hardcoded and have to be configured by the apps server and should be modifiable upon need by the applications. Vendors are free to include additional parameters and fine-tuning parameters. None of these values should go undocumented by the vendor. No sensitive data should be available in the customOpts.
  }
}
```

**Accepted Values for Match Request**

| Parameters                | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| ------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| env                       | <ul><li>The target environment.</li><li>For devices that are not registered the environment is "None".</li><li>For device that is registered, then send the environment in which it is registered.</li><li>Allowed values for registered device are "Staging", "Developer", "Pre-Production" or "Production".</li></ul>                                                                                                                                                                                                                          |
| relyingPartyUri           | The URI for the relying party requesting for performing the match.                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
| purpose                   | <ul><li>Free text with restriction up to 256 characters.</li><li>Example: Match for logging into a bank application.</li></ul>                                                                                                                                                                                                                                                                                                                                                                                                                   |
| specVersion               | Expected SBI spec version.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
| timeout                   | Timeout for capture and match. Expecting a response within the same timeout.                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| transactionId             | <ul><li>Unique transaction ID for the current match request.</li><li>This is a free text with restriction up to 50 characters.</li></ul>                                                                                                                                                                                                                                                                                                                                                                                                         |
| jwk                       | <ul><li>Contains the public key from the client application used for encrypting the credential object using <a href="https://www.rfc-editor.org/rfc/rfc2631">Diffie-Hellman Key Exchange</a> algorithm.</li><li>This attribute stores the key details as a JSON Web Key (JWK) following <a href="https://www.rfc-editor.org/rfc/rfc7517">RFC7517</a></li><li>This is an optional attribute. If not set then, the credential object is not encrypted.</li></ul>                                                                                   |
| credentialFormat          | MIME type of the credential - jwt\_vc\_json/mso\_mdoc/ldp\_vc/jwt\_vc\_json-ld                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
| credential                | A valid credential object in the prescribed format. The value will be encoded based on credentialFormat. If not defined it will be base64URLencoded.                                                                                                                                                                                                                                                                                                                                                                                             |
| captureBio.type           | Type of the biometric data.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| captureBio.bioSubType     | <ul><li>SubType of the biometrics to be captured &#x26; matched</li><li>For Finger: ["Left IndexFinger", "Left MiddleFinger", "Left RingFinger", "Left LittleFinger", "Left Thumb", "Right IndexFinger", "Right MiddleFinger", "Right RingFinger", "Right LittleFinger", "Right Thumb", "UNKNOWN"]</li><li>For Iris: ["Left", "Right", "UNKNOWN"]</li><li>For Face: No bioSubType</li><li>For Finger and Iris, if biometric subtype is marked as UNKNOWN then match has to happen against all the fingers available in the credential.</li></ul> |
| captureBio.requestedScore | Expected quality score that should match to complete a successful capture (optional)                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| matchTo.credentialElement | Credential attribute name where the biometric is present.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| matchTo.credentialPath    | <p>Path of the credential element in the credential object. The path details may vary based on the credential format. Few of the formats that we would be supporting as part of the spec.</p><ul><li>XML - <a href="https://www.w3.org/TR/1999/REC-xpath-19991116/">XPath</a></li><li>JSON - <a href="https://www.rfc-editor.org/rfc/rfc6901">JSON Pointer</a></li></ul>                                                                                                                                                                         |
| matchTo.mimeType          | MIME type of the biometric data in the credential element - JPEG2000, BMP, PNG, WSQ, FMR, Algo/Version                                                                                                                                                                                                                                                                                                                                                                                                                                           |
| customOpts                | Max of 50 key-value pair. This is so that vendor-specific parameters can be sent if necessary. The values cannot be hardcoded and have to be configured by the apps server and should be modifiable upon need by the applications. Vendors are free to include additional parameters and fine-tuning parameters. None of these values should go undocumented by the vendor. No sensitive data should be available in the customOpts.                                                                                                             |

**Match Response**

```json
{
  "match": "Yes/No/Error",
  "transactionId": "Transaction ID from the current match request.",
  "timestamp": "Timestamp in ISO ",
  "matchedAgainst": {
    "issuerId": "ID of the Issuer",
    "issuerKey": "Public key of the issuer which is base64URLencoded",
    "credentialId": "ID of the credential object that was used for verification"
  },
  "matchParams": {
    //Optional key value pairs for audit or analytics purpose
    "credentialHash": "hash of the credential object",
    "type": "type of biometrics used",
    "bioSubType": "biometric subType used for authentication",
    "internalScore": "internal match score from the SDK",
    "algo": "algorithm used for match",
    "version": "Version of the SDK used for matching",
    "key":"value"
  },
  "error": {
        "errorCode": "0",
        "errorInfo": "Success"
  }
}
```

**Allowed Values for Match Response**

| Parameters                  | Description                                                                                                                                                 |
| --------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------- |
| match                       | Match result. The value can be, Yes - if match is found, No - if there was no match and Error - if the match failed.                                        |
| transactionId               | Transaction ID from the current match request.                                                                                                              |
| timestamp                   | Time as per the device where match is performed. The timestamp should be as per ISO 8601 format (yyyy-mm-ddTHH:MM:ssZ) in UTC (Coordinated Universal Time). |
| matchedAgainst              | Details about the credential that was used for match.                                                                                                       |
| matchedAgainst.issuerId     | ID of the issuer taken from the credential object.                                                                                                          |
| matchedAgainst.issuerKey    | The public key of the issuer.                                                                                                                               |
| matchedAgainst.credentialId | The credential ID from the credential object.                                                                                                               |
| matchParams                 | Optional key value pair used for audit. Sample attributes can be, credentialHash, type, matchScore or matchAlgo                                             |
| error                       | Relevant errors as defined under the error section of this document.                                                                                        |
| error.errorCode             | Standardized error code defined in the error code section\</>                                                                                               |
| error.errorInfo             | Description of the error that can be displayed to end user. Multi lingual support.                                                                          |

**Windows/Linux**

The applications that require more details of the SBI devices could get them by sending the HTTP request to the supported port range.

_**HTTP Request:**_

```
POST  http://127.0.0.1:<device_service_port>/match
HOST: 127.0.0.1: <apps port>
EXT: <app name>
```

_**HTTP Response:**_

```
HTTP/1.1 200 OK
CACHE-CONTROL:no-store
LOCATION:[http://127.0.0.1](http://127.0.0.1):<device_service_port>/match
Content-Length: length in bytes of the body
Content-Type: application/json
Connection: Closed
```

**Android**

For details on android specifications please view the section - Android SBI Specification.

***

### Management Server and Management Client

#### Management Server Functionalities and Interactions

The management server has the following objectives.

1. Validate the devices to ensure they are genuine devices from the respective device provider. This can be achieved using the device info and the certificates for the Foundational Trust Module.
2. Register the genuine device with the SBI device server.
3. Manage/Sync time between the end device and the server. The time to be synced should be the only trusted time accepted by the device.
4. Ability to issue commands to the end device for
   1. De-registration of the device (Device Keys)
   2. Collect device information to maintain, manage, support and upgrade a device remotely.
5. A central repository of all the approved devices from the device provider.
6. Safe storage of keys using HSM FIPS 140-2 Level 3. These keys are used to issue the device certificate upon registration. The Management Server is created and hosted by the device provider outside of the application software. The communication protocols between the SBI and the Management Server can be decided by the respective device provider. Such communication should be restricted to the above-specified interactions only. No transactional information should be sent to this server.
7. Should have the ability to push updates from the server to the client devices.
8. Should have the ability to share the encryption certificate as a [JWKS](https://www.rfc-editor.org/rfc/rfc7517)(example: `https://example.com/.well-known/jwks.json`) to the devices.

**Notes:**

_As there is no adopter-specific information being exchanged at the management server or the FTM provisioning server, there are no mandates from SBI where these are located globally. However, the adopter is recommended to have an audit and contractual mechanisms to validate the compliance of these components at any point in time._

#### Management Client

The management client is the interface that connects the device with the respective management server. The communication between the management server and its clients must be designed with scalability, robustness, performance and security. The management server may add many more capabilities than what is described here, But the basic security objectives should be met at all times irrespective of the offerings.

1. For better and more efficient handling of the device at large volume, we expect the devices to auto-register to the Management Server.
2. All communication to the server and from the server should follow the below properties.
   1. All communication is digitally signed with the approved algorithms
   2. All communication to the server is encrypted using one of the approved public key cryptography (HTTPS – TLS1.2/1.3 is required with one of the approved algorithms ).
   3. All request has timestamps attached in ISO format to the milliseconds inside the signature.
   4. All communication back and forth should have the signed digital ID as one of the attributes.
3. It's expected that auto-registration has an absolute way to identify and validate the devices.
4. The management client should be able to detect the devices in a plug-and-play model.
5. All key rotations should be triggered from the server.
6. Should have the ability to detect if it's speaking to the right management server.
7. All upgrades should be verifiable and the client should have the ability to verify software upgrades.
8. Should not allow any downgrade of software.
9. Should not expose any API to capture biometrics. The management server should have no ability to trigger a capture request.
10. No logging of biometric data is allowed. (Both in the encrypted and unencrypted format)

***

### Compliance

**L1 Certified Device / L1 Device** - A device certified as capable of performing encryption on the device inside its trusted zone. **L0 Certified Device / L0 Device** - A device certified as one where the encryption is done on the host inside its device driver or the SBI device service.

#### Secure Provisioning

Secure provisioning applies to both the FTM and the Device providers.

1. The devices and FTM should have a mechanism to protect against fraudulent attempts to create or replicate.
2. The device and FTM trust should be programmed in a secure facility that is certified by the respective application adopters.
3. The organization should have a mechanism to segregate the FTMs and Devices built for SBI using a cryptographically valid and repeatable process.
4. All debug options within the FTM or device should be disabled permanently
5. All key creations needed for provisioning should happen automatically using FIPS 140-2 Level 3 or higher devices. No individual, group or organization should have a mechanism to influence this behaviour.
6. Before the devices/FTM leave the secure provisioning facility all the necessary trust should be established and should not be re-programmable.

**Note:**

* As there is no adopter-specific information being exchanged at the management server or the FTM provisioning server, there are no mandates from applications where these are located globally. However, the adopter is recommended to have an audit and contractual mechanisms to validate the compliance of these components at any point in time.\*

#### Compliance Level

| API                  | Compatible |
| -------------------- | ---------- |
| Device Discovery     | L0/L1      |
| Device Info          | L0/L1      |
| Capture              | L1         |
| Stream               | L0         |
| Registration Capture | L0/L1      |

***

### Cryptography

Supported algorithms:

| Usage                                      | Algorithm       | Key Size | Storage                                              |
| ------------------------------------------ | --------------- | -------- | ---------------------------------------------------- |
| Encryption of biometrics - Session Key     | AES             | >=256    | No storage, Key is created with TRNG/DRBG inside FTM |
| Encryption session key data outside of FTM | RSA OAEP        | >=2048   | FTM trusted memory                                   |
| Encryption session key data outside of FTM | ECC curve 25519 | >=256    | FTM trusted memory                                   |
| Biometric Signature                        | RSA             | >=2048   | Key never leaves FTM created and destroyed           |
| Biometric Signature                        | ECC curve 25519 | >=256    | Key never leaves FTM created and destroyed           |
| Secure Boot                                | RSA             | >=256    | FTM trusted memory                                   |
| Secure Boot                                | ECC curve 25519 | >=256    | FTM trusted memory                                   |

**Note:**

No other ECC curves are supported.

### Signature

In all the above APIs, some of the requests and responses are signed with various keys to verify the requester's authenticity. Here we have detailed the key used for signing a particular block in a request or response body of various APIs.

| Request/Response              | Block       | Signature Key                                                                                       |
| ----------------------------- | ----------- | --------------------------------------------------------------------------------------------------- |
| Device Discovery Response     | Device Info | NA as it will not be signed                                                                         |
| Device Discovery Response     | Digital ID  | NA as it will not be signed                                                                         |
| Device Info Response          | Device Info | <ul><li>NA in case of unregistered device</li><li>Device Key in case of registered device</li></ul> |
| Device Info Response          | Digital ID  | <ul><li>For L0 device using device key</li><li>For L1 device using FTM chip key</li></ul>           |
| Capture Response              | Data        | Device key is used                                                                                  |
| Capture Response              | Digital ID  | FTM chip key is used                                                                                |
| Registration Capture Response | Data        | Device key is used                                                                                  |
| Registration Capture Response | Digital ID  | <ul><li>For L0 device using device key</li><li>For L1 device using FTM chip key</li></ul>           |

### Android SBI Specification

This section explains the mechanism for the SBI devices to communicate in the Android operating system.

#### Status

Draft document V 0.9

#### Approach

Discovery will return the appId of the discovered items. Users will be given a choice to choose one of the discovered SBI apps. The selected app responds to the intent using the default intent callback functionality.

#### Device Discovery

_Request_: io.sbi.device\
&#xNAN;_&#x61;ction_: io.sbi.device\
&#xNAN;_&#x64;ata_: no change\
&#xNAN;_&#x74;ype_: application/json\
&#xNAN;_&#x52;equest Schema_: No change in the data structure. The serialized request data as byte array is set in the intent extras with the key as “input”.\
&#xNAN;_&#x52;esponse Schema_: No change in the data structure. The serialized response data (byte array) is set in the intent extras with the key as “response”.

_callbackId_ should be set to the appId, can not be empty in Android.

#### Device Info

_Request_: appId.Info\
&#xNAN;_&#x61;ction_: appId.Info\
&#xNAN;_&#x64;ata_: no change\
&#xNAN;_&#x74;ype_: application/json\
&#xNAN;_&#x52;equest Schema_: No change in the data structure. The serialized request data as a byte array is set in the intent extras with the key as “input”.\
&#xNAN;_&#x52;esponse Schema_: No change in the data structure. The serialized response data as a byte array is set in the intent extras with the key as “response”.

_deviceInfo_: _callbackId_ should be set to the appId, and can not be empty in Android.

#### Capture

_Request_: appId.Capture\
&#xNAN;_&#x61;ction_: appId.Capture\
&#xNAN;_&#x64;ata_: no change\
&#xNAN;_&#x74;ype_: application/json\
&#xNAN;_&#x66;lag_: FLAG\_GRANT\_READ\_URI\_PERMISSION\
&#xNAN;_&#x52;equest Schema_: No change in the data structure. The serialized request data as a byte array is set in the intent extras with the key as “input”.\
&#xNAN;_&#x52;esponse Schema_: No change in the data structure. The response content is set as content URI “content://authority/path/id” in the intent extras as a string with the key as “response”.

URI must be invalidated right after the read.

#### rCapture

_Request_: appId.rCapture\
&#xNAN;_&#x61;ction_: appId.rCapture\
&#xNAN;_&#x64;ata_: no change\
&#xNAN;_&#x74;ype_: application/json\
&#xNAN;_&#x66;lag_: FLAG\_GRANT\_READ\_URI\_PERMISSION\
&#xNAN;_&#x52;equest Schema_: No change in the data structure. The serialized request data as byte array is set in the intent extras with the key as “input”.\
&#xNAN;_&#x52;esponse Schema_: No change in the data structure. The response content is set as content URI “content://authority/path/id” in the intent extras as a string with the key as “response”.

#### match

TBA

The content provider should not support insert, update, delete

#### Device Stream

On receiving rCapture request SBI must show the stream within its UI in the foreground.

#### Security

Ensure to set the correct authority in the manifest and set the android:exported as “False”

#### Android Tab Specs

Android 9 and above with hardware-backed key store

### Error Codes

| Code | Message                                                                                 |
| ---- | --------------------------------------------------------------------------------------- |
| 0    | Success                                                                                 |
| 100  | Device not registered                                                                   |
| 101  | Unable to detect a biometric object                                                     |
| 102  | Technical error during extraction.                                                      |
| 103  | Device tamper detected                                                                  |
| 104  | Unable to connect to the management server                                              |
| 105  | Image orientation error                                                                 |
| 106  | Device not found                                                                        |
| 107  | Device public key expired                                                               |
| 108  | Domain public key missing                                                               |
| 109  | Requested number of biometrics (Finger/IRIS) not supported                              |
| 110  | Device is not ready                                                                     |
| 111  | Device is Busy                                                                          |
| 112  | Invalid Transaction ID                                                                  |
| 5xx  | Custom errors. The device provider is free to choose his error code and error messages. |

```
```
