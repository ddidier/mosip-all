# Security

## MOSIP Security Practices

This document provides a comprehensive analysis of security implementations across multiple levels. Additionally, it clearly delineates the boundaries of responsibility between MOSIP and the countries implementing the system.

Within this document, we have categorized security practices into 'Internal Practices' and 'Operational Protection'.

**Internal security practices** are integrated into the MOSIP development lifecycle to build security within the system from the ground up. These include rigorous threat modeling, secure coding practices, comprehensive code reviews, and continuous vulnerability assessments to ensure that potential risks are identified and mitigated early. By embedding these security measures during development MOSIP fosters a proactive security culture that not only minimizes vulnerabilities but also supports a robust defense strategy throughout the system's lifecycle.

On the other hand, **operational security practices** include firewall rules, intrusion detection systems, continuous monitoring, and incident response strategies. These measures focus on maintaining the security and integrity of the system during its operational phase, addressing runtime threats and ensuring compliance with best practices. Operational practices are outside of MOSIP development stage and to be taken up by the implementing countries.

### Internal Practices

Internal security practices encompass measures such as security requirement elicitation, design, adherence to the MOSIP Principles, Platform development, static and dynamic code analysis, dependency scanning, code signing, and vulnerability management. These practices ensure that potential threats are identified and mitigated early in the development lifecycle.

#### Platform Design

MOSIP's fundamental architecture and design incorporate high levels of privacy and security.

(Table to be updated soon)

#### Development and Release Practices

This section details the measures taken during the development, testing, and release phases to ensure maximum security. Multiple checks are enforced at each stage through the use of various tools, tests, and scans. Key practices include:

**Static Application Security Testing (SAST)**

Static Application Security Testing (SAST) is a cornerstone of our security strategy. Tools like SonarCloud are used to perform in-depth code analysis during the development phase, identifying vulnerabilities such as SQL injection, cross-site scripting (XSS), and insecure coding practices. SAST provides developers with real-time feedback, enabling them to address security flaws early, thereby reducing the cost and effort of remediation later in the software lifecycle. These tools integrate seamlessly into our CI/CD pipelines, ensuring that security is addressed continuously and early. Dependency scanning tools like Dependabot, CodeQL, and others further enhance this layer of protection by monitoring and updating vulnerable dependencies.

* **Sonar Cloud** - Development Phase - SonarCloud is integrated with Github actions, offering developers actionable insights directly within the workflow. By highlighting security hotspots and technical debt, it enables teams to prioritize and address critical issues efficiently.

> MOSIP Sonar cloud Link : [https://sonarcloud.io/organizations/mosip/projects](https://sonarcloud.io/organizations/mosip/projects)

* **CodeQL** (Java and Python) - Development Phase - CodeQL performs semantic code analysis, enabling the detection of complex vulnerabilities
* **Github Dependabot** (Vulnerability assessment and Version upgrade suggestions) - Development Phase - Dependabot simplifies the process of updating dependencies by creating pull requests with the necessary upgrades reducing manual effort and ensuring the codebase remains secure against known vulnerabilities. Its integration into GitHub workflows ensures timely updates and fosters a proactive approach to dependency management.
* **Open source compliance scanning** - Ensures that all open-source components in use comply with licensing requirements and security best practices. This scanning helps in identifying potential legal or security risks associated with third-party libraries. Automated tools are used to track, analyze, and flag issues related to incompatible or outdated licenses, ensuring smooth and compliant project operations.
* **Github scan** - Provides robust scanning capabilities integrated directly into GitHub repositories. It includes features such as secret scanning, dependency graph analysis, and vulnerability alerts, helping developers proactively detect and fix security issues within their workflows.
* **Data Breach Detector** - It is a prodcution grade tool/script which goes through the DB and utilizes Deduce library to find out anomalies in various places such as names, address or numbers in plaintext etc.

**Dynamic Application Security Testing (DAST)**

DAST focuses on identifying security vulnerabilities in a running application by simulating real-world attack scenarios. Unlike SAST, which examines static code, DAST tests live applications, analyzing responses to detect flaws such as authentication issues, session management vulnerabilities, and exposure of sensitive data. Tools like Burp Suite Professional and ZED Attack Proxy (ZAP) are leveraged to conduct automated and manual penetration tests. These tools allow testers to evaluate application behavior under various conditions, ensuring robust protection against runtime threats. By integrating DAST into the release process, vulnerabilities can be identified and mitigated before applications are deployed into production.

* **Burp Suite Professional** : This tool is used for automated and manual penetration testing . It provides features such as intercepting proxy, web vulnerability scanner, and advanced debugging capabilities. Burp Suite enables testers to identify vulnerabilities like SQL injection, cross-site scripting (XSS), and insecure session management. It also supports extensions for customized scanning and integrates seamlessly into security workflows.
* **ZED Attack Proxy:** This tool is used for finding vulnerabilities in web applications during the development and testing phases.

**Release Practices**

Release practices are essential for ensuring the security, authenticity, and traceability of software releases. Here is an overview of the components used in MOSIP releases.

* **Image Signing:** ASC (ASCII-armored PGP) signing is typically used to ensure the authenticity and integrity of software artifacts, including Docker images, by attaching a digital signature. When signing software images, MOSIP uses the private key to sign the image, and users can verify the signature using the corresponding public key.
* **JAR (Java Archive) Signing** is the practice of signing Java archive files to ensure that the contents of the JAR haven't been tampered with and to provide a way to verify the source of the file. This is currently not implemented in MOSIP.

### Operational Practices

These recommendations provide a robust framework to ensure the security and integrity of production systems for MOSIP implementing countries, helping to mitigate risks and enhance overall cybersecurity posture.

* **SBI Compliant Devices:** Ensure that all devices used in the production environment are compliant with the latest Secure Biometric Interface (SBI) standards to ensure a highest level of security.
* **Trusted Platform Module:** A Trusted Platform Module (TPM) is a specialized chip on a local machines that stores cryptographic keys specific to the host system for hardware authentication. The private key is maintained inside the chip and can't be extracted out. By leveraging this security feature every individual machine would be uniquely registered and identified by the MOSIP server component with it's TPM public key.
* **Compliance Tool Kit:** MOSIP Provides Compliance Tool Kit (CTK) to help the device vendors to check if their products comply with SBI specifications.
* **Access and Audit Logs**: Enables detailed access and audit logging for all critical systems and services in the production environment.
* **Patch Management (Host/Machines)**: Implement a robust patch management policy to ensure that all production systems are up-to-date with the latest security patches.
* **Safe Data Centers**: Ensure that data centers housing production systems are designed and operated with a focus on security, availability and operational safety.
* **International standards**: Stay compliant on international standards such as ISO/IEC 27001, NIST Cybersecurity Framework, and relevant national regulations. Better to validate the compliance using third party assessments.
* **Ensuring Data Protection**: Enforce robust data protection measures to safeguard sensitive information at rest and in transit.
* **Consent-Based Data Handling**: Ensure that data is only collected, processed, and stored with the explicit consent of the individuals it pertains to, in accordance with privacy laws and regulations.
* **Regular Security Audits**: Perform regular security audits to assess the effectiveness of security measures and identify potential vulnerabilities in production systems.
* **Principle of Least Privilege**: Ensure that users and systems are granted only the minimum level of access necessary to perform their tasks, reducing the risk of accidental or malicious misuse.
* **Rate Limiting**: Implement rate limiting to protect services from abuse, such as brute force attacks or denial-of-service (DoS) attempts.
