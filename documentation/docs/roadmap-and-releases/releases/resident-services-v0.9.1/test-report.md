# Test Report

### Testing Scope

The scope of testing is to verify fitment to the specification from the perspective of&#x20;

●     Functionality&#x20;

●     Deployability&#x20;

●     Configurability&#x20;

●     Customizability

Verification is performed not only from the end user perspective but also from the System Integrator (SI) point of view. Hence Configurability and Extensibility of the software are also assessed. This ensures the readiness of software for use in multiple countries. Since MOSIP is an “API First” product platform, the Verification scope required comprehensive automation testing for all the MOSIP APIs. An automation Test Rig is created for the same.

The Resident Revamp testing scope revolves around the following flows:

●     Get Information

●     Pre-registration Booking

●     Verify phone/email

●     View My history

●     Track services

●     Manage My VID

●     Secure MY ID

●     Get a personalized Card

●     Share credentials with partner

●     Multilingual (English/Arabic/French)

●     Resident UI End to End testing

●     Regression Testing

### Test Approach

Persona based approach has been adopted to perform the IV\&V, by simulating test scenarios that resemble a real-time implementation.

A Persona is a fictional character/user profile created to represent a user type that might use a product/or a service in a similar way. Persona based testing is a software testing technique that puts software testers in the customer's shoes, assesses their needs from the software, and thereby determines use cases/scenarios that the customers will execute. The persona's needs may be addressed through any of the following.

●     Functionality&#x20;

●     Deployability&#x20;

●     Configurability&#x20;

●     Customizability

The verification methods may differ based on how the need was addressed.

For regression check, “MOSIP Test Rig” - an automation testing suite - is indigenously designed and developed for supporting persona-based testing. MOSIP Test Rig covers the end to end test execution and reporting. The end to end functional test scenarios are written starting from pre-registration, to the creation of a packet in the registration center, processing the packet through the registration processor, generating UIN, and authenticating identity using IDA through various permutations and combinations of cases being covered. MOSIP Test Rig will be an open source artifact that can also be enhanced and used by countries to validate the SI deliveries before going live. Persona classes include both negative and positive personas. Negative persona classes include users like Bribed Registration Office, Malicious Insider, etc. The needs of positive persona classes must be met, whereas the needs of negative persona classes must be effectively restricted by the software.

## Verified configuration

Verification is performed on various configurations as mentioned below

&#x20;    ●   Default configuration - with 3 Languages (English/Arabic/French)

## Test execution statistics

#### Functional test results by modules

Below are the test metrics by performing functional testing using mock MDS and mock ABIS. The process followed was black box testing which based its test cases on the specifications of the software component under test. The functional test was performed in combination with individual module testing as well as integration testing. Test data were prepared in line with the user stories. Expected results were monitored by examining the user interface. The coverage includes GUI testing, System testing, End-To-End flows across multiple languages and configurations. The testing cycle included the simulation of multiple identity schema and respective UI schema configurations.

#### Manual Verification (UI+API)

<table><thead><tr><th valign="top">Total Test Cases</th><th valign="top">Passed</th><th valign="top">Failed</th><th valign="top">Skipped (N/A)</th></tr></thead><tbody><tr><td valign="top">2502</td><td valign="top">2461</td><td valign="top">29</td><td valign="top">12</td></tr></tbody></table>

**Test Rate:** 99%  With **Pass Rate:** 98%

{% hint style="info" %}
**Note:** NA - 12 Test Cases which are descoped scenarios/not developed feature
{% endhint %}

#### External API verification results by modules

The section below provides details on API test metrics, executing the MOSIP functional automation Framework. All external API test executions were performed at module-level isolation. The test data is used to test each endpoint, and the expectations of each test data set are mapped to assert the test case.

<table><thead><tr><th valign="top">Total</th><th valign="top">Passed</th><th valign="top">Failed</th><th valign="top">Skipped</th></tr></thead><tbody><tr><td valign="top">1126</td><td valign="top">1117</td><td valign="top">9</td><td valign="top">0</td></tr></tbody></table>

**Test Rate:** 100% With **Pass Rate:** 99.2%

#### UI Automation results

The below section provides details on UI Automation by executing the MOSIP functional automation Framework.

<table><thead><tr><th valign="top">Total</th><th valign="top">Passed</th><th valign="top">Failed</th><th valign="top">Skipped</th></tr></thead><tbody><tr><td valign="top">25</td><td valign="top">25</td><td valign="top">0</td><td valign="top">0</td></tr></tbody></table>

**Test Rate:** 100% With **Pass Rate:** 100%

The functional and test rig codebase branch used for the above metrics is:

**Hash Tag:**\
`mosipqa/resident-ui@sha256:69e0edb4a61c724d1fca36178984382bfd87f58f6df54520704bac0da06c55f1`&#x20;

### Detailed Test metrics

Below are the detailed test metrics for manual and automation testing. The project metrics are derived from Defect density, Test coverage, Test execution coverage, test tracking, and efficiency.

The various metrics that assist in test tracking and efficiency are as follows:

●  **Passed Test Cases Coverage:** It measures the percentage of passed test cases. (Number of passed tests / Total number of tests executed) x 100

●  **Failed Test Case Coverage:** It measures the percentage of all the failed test cases. (Number of failed tests / Total number of test cases executed) x 100

### Sonar Report

<figure><img src="../../../.gitbook/assets/0.9.1-sonar-report.png" alt=""><figcaption><p>Sonar Report</p></figcaption></figure>

Please refer to the GitHub link for the XLS file given [here](https://github.com/mosip/test-management/tree/master/Resident%20Revamp%201.2.0.1/0.9.1).
