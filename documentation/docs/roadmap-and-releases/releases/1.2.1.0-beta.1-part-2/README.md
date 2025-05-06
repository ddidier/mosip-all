# 1.2.1.0-beta.1 (Part 2)

**Release Version**: 1.2.1.0-beta.1

**Release Type:** Beta Release

**Release Date**: 16th December, 2024

### **Overview**

This component wise beta release introduces significant enhancements, including migration to Java 21 for improved compatibility and performance, the categorization of MOSIP data into default and seed data for streamlined deployment and customization, and support for new cryptographic algorithms (SECP256K1, SECP256R1, and Ed25519) to strengthen security

Additionally, transitioning from the landing zone folder to the MinIO object store addresses scalability and storage challenges, ensuring efficient data handling.

This release has been tested with and supports [eSignet 1.3.0](https://docs.esignet.io/versions/v1.3.0). For users who have already deployed MOSIP and intend to upgrade to Java 21, please refer to [this guide](https://docs.mosip.io/1.2.0/java-21-migration-guide) for a detailed step-by-step process to ensure a seamless transition.

{% hint style="info" %}
**Note:**

As the Inji stack continues to evolve, we are introducing the inji-config repository to manage the Spring Cloud configuration for the stack. Hence Inji-config file has been moved out from the mosip-config repository and placed under the inji-config repository [here](https://github.com/mosip/inji-config).
{% endhint %}

As part of the Java 21 migration in this beta release, users must follow specific steps to migrate Artifactory and MOSIP configuration files. Before proceeding with the migration, ensure that two separate Artifactory servers are running:

1. **Java 11 Artifactory Server**: This is for repositories not yet compatible with Java 21 in this beta release.
2. **Java 21 Artifactory Server**: For repositories that support Java 21.

This setup ensures compatibility and seamless operation of Java 11 and Java 21-supported repositories. Any latest Java 11-compatible Artifactory server can be used for this purpose.

{% hint style="info" %}
**Note:** This component wise beta release will be part of the MOSIP Identity v1.2.1.0-beta.1 release.
{% endhint %}

#### **Major Areas of Work**

* Code Migration from JAVA 11 to JAVA 21
* Enabling Digital Signature with ECC Algorithm in Key Manager
* Data Segregation to Default, Seed, and Test
* Functional bug fixes
* Security bug fixes
* DSL Automation

#### **Repositories Released**

<table><thead><tr><th width="374">Repositories</th><th>Tags Released</th></tr></thead><tbody><tr><td><ol><li>biosdk-client</li></ol></td><td><a href="https://github.com/mosip/biosdk-client/tree/v1.3.0-beta.1">v1.3.0-beta.1</a></td></tr><tr><td><ol start="2"><li>otp-manager</li></ol></td><td><a href="https://github.com/mosip/otp-manager/tree/v1.3.0-beta.1">v1.3.0-beta.1</a></td></tr><tr><td><ol start="3"><li>biosdk-services</li></ol></td><td><a href="https://github.com/mosip/biosdk-services/tree/v1.3.0-beta.1">v1.3.0-beta.1</a></td></tr><tr><td><ol start="4"><li>packet-manager</li></ol></td><td><a href="https://github.com/mosip/packet-manager/tree/v1.3.0-beta.1">v1.3.0-beta.1</a></td></tr><tr><td><ol start="5"><li>admin-services</li></ol></td><td><a href="https://github.com/mosip/admin-services/tree/v1.3.0-beta.1">v1.3.0-beta.1</a></td></tr><tr><td><ol start="6"><li>print</li></ol></td><td><a href="https://github.com/mosip/print/tree/v1.3.0-beta.1">v1.3.0-beta.1</a></td></tr><tr><td><ol start="7"><li>websub</li></ol></td><td><a href="https://github.com/mosip/websub/tree/v1.3.0-beta.1">v1.3.0-beta.1</a></td></tr><tr><td><ol start="8"><li>mosip-config</li></ol></td><td><a href="https://github.com/mosip/mosip-config/tree/v1.3.0-beta.1">v1.3.0-beta.1</a></td></tr><tr><td><ol start="9"><li>artifactory-ref-impl</li></ol></td><td><a href="https://github.com/mosip/artifactory-ref-impl/tree/v1.3.0-beta.1">v1.3.0-beta.1</a></td></tr><tr><td><ol start="10"><li>digital-card-service</li></ol></td><td><a href="https://github.com/mosip/digital-card-service/tree/v1.3.0-beta.1">v1.3.0-beta-1</a></td></tr><tr><td><ol start="11"><li>mosip-data</li></ol></td><td><a href="https://github.com/mosip/mosip-data/tree/v1.3.0-beta.1">v1.3.0-beta-1</a></td></tr><tr><td><ol start="12"><li>image-compressor</li></ol></td><td><a href="https://github.com/mosip/image-compressor/tree/v0.1.0-beta.1">v0.1.0-beta.1</a></td></tr><tr><td><ol start="13"><li>imagedecoder</li></ol></td><td><a href="https://github.com/mosip/imagedecoder/tree/v0.10.0-beta.1">v0.10.0-beta.1</a></td></tr><tr><td><ol start="14"><li>admin-ui</li></ol></td><td><a href="https://github.com/mosip/admin-ui/tree/v1.3.0-beta.1">v1.3.0-beta.1</a></td></tr><tr><td><ol start="15"><li>pre-registration</li></ol></td><td><a href="https://github.com/mosip/pre-registration/tree/v1.3.0-beta.1">v1.3.0-beta.1</a></td></tr><tr><td><ol start="16"><li>nfiq</li></ol></td><td><a href="https://github.com/mosip/nfiq/tree/v0.1.0-beta.1">v0.1.0-beta.1</a></td></tr><tr><td><ol start="17"><li>mosip-ref-impl</li></ol></td><td><a href="https://github.com/mosip/mosip-ref-impl/tree/v1.3.0-beta.1">v1.3.0-beta.1</a></td></tr><tr><td><ol start="18"><li>registration</li></ol></td><td><a href="https://github.com/mosip/registration/tree/v1.3.0-beta.1">v1.3.0-beta.1</a></td></tr><tr><td><ol start="19"><li>captcha</li></ol></td><td><a href="https://github.com/mosip/captcha/tree/v0.1.0-beta.1">v0.1.0-beta.1</a></td></tr><tr><td><ol start="20"><li>pre-registration-ui</li></ol></td><td><a href="https://github.com/mosip/pre-registration-ui/tree/v1.3.0-beta.1">v1.3.0-beta.1</a></td></tr></tbody></table>

#### **Documents for reference**

* [Enhancements & Bug fixes](https://docs.mosip.io/1.2.0/releases/1.2.1.0-beta.1-part-2/enhancements-and-bug-fixes)
* [Functional test report](https://docs.mosip.io/1.2.0/releases/1.2.1.0-beta.1-part-2/test-report)
* [Known issues](https://mosip.atlassian.net/issues/?filter=11674)
