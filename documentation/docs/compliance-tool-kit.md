# Compliance Tool Kit

Compliance Tool Kit (CTK) is an online portal that can be used by MOSIP partners to test the compliance of their product developed as per specifications (specs) published/adopted by MOSIP. 

Currently, CTK supports testing of compliance with the below specifications:

1. **SBI**: Secure Biometric Interface (SBI) is used to interface with biometric devices. Device partners are required to build a software layer that provides a unified communication protocol for all biometric capture use cases. The specifications that should be followed are defined as Secure Biometrics Interface (SBI) specs. The compliance tool kit ensures that the interface built by the device provider is following the specs, and certain level of security and integrity as defined in the SBI spec. The device hardware security features are not tested as part of this toolkit. 

2. **SDK**: Biometric Service Providers (BSPs) provide SDK implementation which supports quality check, match, extraction, and conversion of biometrics. MOSIP defines an [IBioAPI](https://github.com/mosip/commons/blob/master/kernel/kernel-biometrics-api/src/main/java/io/mosip/kernel/biometrics/spi/IBioApi.java) as the specification for this SDK implementation. Biometric SDK providers are also required to integrate this [http service](https://github.com/mosip/biosdk-services/tree/release-1.2.0) in their solution. This allows to run the SDK as an independant http service. The compliance tool kit would make sure that these interfaces are as per the MOSIP-defined specifications for smooth interaction.

<!--
3. **ABIS**- Automated Biometric Identification System (ABIS) vendors provide ABIS component which performs de-duplication of a resident's biometric data. This is useful to ensure the uniqueness of residents’ biometrics. MOSIP’s ABIS partners communicate with MOSIP via a queue. MOSIP defines ABIS specs for the same.  
-->

## How does Compliance Tool Kit work?

* To support compliance with the specifications, CTK has predefined test cases for each of the above specs. 
* Each test case is run on a given method of the specs. Each test case defines the attributes required to create the request to be sent to the method.
* Each test case also defines the response expected from the method. In this response, various validators are run. Each validator will perform a predefined check on the response. If all validations are successful, then the test case is passed otherwise it is a failed test case. 
* Partners can use CTK to run these test cases to check if their implementation adheres to the MOSIP’s specs or not. 

![Compliance Tool Kit Process Flow Diagram](_images/compliance-toolkit-flow-diagram.png)

## Compliance Tool Kit Portal

To set up the Compliance Tool Kit, refer [How to set up CTK](https://docs.mosip.io/1.2.0/modules/compliance-tool-kit/how-to-guides/ctk-setup-steps).

To use the CTK portal, refer to the [Compliance Tool Kit User Guide](https://docs.mosip.io/1.2.0/modules/compliance-tool-kit/ctk-user-guide).

## Build and deploy

To access the build and read through the deployment instructions, refer to the below mentioned READMEs:
1. [Compliance Tool Kit README (backend)](https://github.com/mosip/mosip-compliance-toolkit/tree/develop#readme)
2. [Compliance Tool Kit UI README](https://github.com/mosip/mosip-compliance-toolkit-ui/tree/develop#readme)

## Configurations

For details related to the Compliance Tool Kit configurations, refer [Compliance Tool Kit configuration document](https://github.com/mosip/mosip-compliance-toolkit/tree/0.0.9-B1).

## Developer Guide

To be able to add new test cases to CTK, refer [How to add more test cases](https://docs.mosip.io/1.2.0/modules/compliance-tool-kit/how-to-guides/ctk-test-cases).

## Source code

To access the source code for Compliance Tool Kit, refer, 
* [Compliance Tool Kit Backend](https://github.com/mosip/mosip-compliance-toolkit/tree/0.0.9-B1)
* [Compliance Tool Kit UI](https://github.com/mosip/mosip-compliance-toolkit-ui/tree/0.0.9-B1)

