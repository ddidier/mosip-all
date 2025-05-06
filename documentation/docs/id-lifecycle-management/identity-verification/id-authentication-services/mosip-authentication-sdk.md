# MOSIP Authentication SDK

### **Overview**

The **MOSIP Authentication SDK** is a (Python-based) wrapper designed to simplify interaction with the **MOSIP Authentication Service**, enabling seamless integration of robust identity verification workflows into Python applications. This SDK abstracts complex details such as request/response structures, encryption/decryption mechanisms, and error handling, allowing developers to implement authentication workflows quickly and efficiently.\
\
Currently, the SDK supports OTP authentication and demographic authentication. Future updates will expand its functionality to include biometric authentication.\
\
Additionally, although the SDK is currently Python-based, we will soon be expanding support to other languages to offer broader compatibility.

### Purpose

This page provides an overview of the Authentication SDK, outlining its functionality and providing a [detailed process for installing](https://docs.mosip.io/1.2.0/modules/id-authentication-services/mosip-authentication-sdk#installation-process) and testing the IDA API using the SDK.

#### **Why Use This SDK?**

While building your solution around MOSIP, it is recommended to use eSignet, MOSIP's OAuth- and OIDC-based solution, for most online and scalable authentication needs due to its modern, standards-compliant design. However, the MOSIP Authentication SDK offers its own advantages, particularly in the flexibility it provides, making it an invaluable tool for addressing a wide range of identity verification requirements.

1. **Ease of Integration**: Simplifies the process of working with MOSIP’s APIs, reducing the learning curve for developers
2. **Consistency**: Provides a uniform interface for different authentication operations, ensuring a consistent experience
3. **Security**: Manages encryption and decryption of requests and responses, adhering to MOSIP's security standards
4. **Flexibility**: Supports multiple authentication methods, including demographic authentication, offering versatility in identity verification workflows

### **Key Features**

1. **Simplified API Interaction**: Abstracts the complexity of direct API calls to MOSIP services
2. **Support for Multiple Authentication Workflows**: Includes controllers for both KYC-based and general authentication
3. **Comprehensive Configuration**: Allows customization via a configuration file (authenticator-config.toml)
4. **Secure Handling**: Automatically encrypts requests and decrypts responses to ensure secure communication
5. **Error Management**: Provides clear error messages and handling mechanisms

### **Controllers**

The SDK provides two primary controllers, each designed for a specific authentication workflow:

1. kyc-auth-controller\
   Used for **Know Your Customer (KYC)** authentication. This controller facilitates verification using demographic data or OTP verification.\
   **Reference**: [KYC Auth Controller API Documentation](https://mosip.github.io/documentation/1.2.0/authentication-service.html#tag/kyc-auth-controller)
2. auth-controller\
   Used for general authentication of individuals, allowing verification based on a wide range of identifiers such as demographic authentication and OTP authentication.\
   **Reference**: [Auth Controller API Documentation](https://mosip.github.io/documentation/1.2.0/authentication-service.html#operation/authenticateIndividual)

### **Method Reference**

The SDK provides two key methods for authentication:

1. kyc **Method**: Used for KYC-based authentication by verifying an individual's demographic data and OTP.
2. auth **Method**: Handles general authentication requests with similar parameters as kyc.

Both methods require the individual's ID (individual\_id), ID type (individual\_id\_type), demographic data (DemographicsModel), optionally an OTP, biometric data, and consent confirmation. These methods streamline identity verification processes for diverse use cases. \
\
Please refer below to know more about the methods.

#### kyc Method

Authenticates an individual using KYC-based workflow.

```python
kyc(
    individual_id: str,
    individual_id_type: str,
    demographic_data: DemographicsModel,
    otp_value: Optional[str] = None,
    biometrics: Optional[List[BiometricModel]] = None,
    consent: bool = False
) -> Response
```

#### auth Method

Performs a general authentication.

```python
auth(
    individual_id: str,
    individual_id_type: str,
    demographic_data: DemographicsModel,
    otp_value: Optional[str] = None,
    biometrics: Optional[List[BiometricModel]] = None,
    consent: bool = False
) -> Response
```

**Common Parameters**

* individual\_id _(str)_: The unique ID of the individual (e.g., VID, UIN)
* individual\_id\_type _(str)_: Specifies the type of ID used (e.g., VID, UIN)
* demographic\_data _(DemographicsModel)_: A model containing demographic details such as name and address
* otp\_value _(Optional\[str])_: The One-Time Password (OTP) for authentication, if applicable
* consent _(bool)_: Indicates if the individual has given consent for authentication

### Installation Process

**Pre-requisites:**

Before beginning the installation and configuration of this SDK, the user must complete the following steps:

1. **Register as an Authentication Partner (AP)**: Register their organization as an Authentication Partner. Please refer to this link [here](https://docs.mosip.io/1.2.0/partners#authentication-partner-ap) and follow the steps for registration.
2. **Obtain the IDA-FIR(K21) Certificate**: The user must possess the IDA-FIR(K21) certificate. The certificate can be obtained [here](https://mosip.github.io/documentation/1.2.0/authentication-internal-service.html#operation/getCertificate).
3. **Provide required details in the request**:
   1. app id: IDA
   2. ref : IDA-FIR
4. **Install pip on the machine**: The user should install pip to manage Python packages. Installation instructions can be found [here](https://pip.pypa.io/en/stable/installation/).

#### **Configuration**

During installation, the SDK must be configured by updating the authenticator-config.toml file. Please refer to this link [here](https://github.com/mosip/ida-auth-sdk/blob/v0.9.0/mosip_auth_sdk/_authenticator/authenticator-config.toml) for the configuration file, This file contains essential details, such as:

* **Service Endpoints**
* **Encryption Keys**
* **Timeout Settings**
* **Logging Settings**

Refer to this link [here](http://https/github.com/mosip/ida-auth-sdk/blob/v0.9.0/examples/config.toml) for a sample configuration file to guide you in the setup process.

### **Installation**

Install the SDK using pip:

```
pip install git+https://github.com/mosip/ida-auth-sdk.git@v0.9.0
```

### **Usage**

Users who wish to try out this SDK should follow these steps:

1. **Initialize the Authenticator**: Set up the authentication instance to begin interacting with the SDK
2. **Create Demographic Data**: Prepare the necessary demographic information required for authentication
3. **Perform Authentication**: Execute the authentication request using the SDK
4. **Handle the Response**: Process and utilize the response received from the authentication service

For detailed guidance on performing these steps during the installation process, please refer to the model implementation below.

**Basic Example:**

```python
from mosip_auth_sdk import MOSIPAuthenticator
from mosip_auth_sdk.models import DemographicsModel, BiometricModel

# Initialize the authenticator
authenticator = MOSIPAuthenticator(config={
    # Configuration settings (refer to authenticator-config.toml for details)
})

# Create demographic data
demographics_data = DemographicsModel(
    name="John Doe",
    address="123 Main Street"
    # Add other fields as required
)

# Perform KYC authentication
response = authenticator.kyc(
    individual_id="123456789",
    individual_id_type="VID",
    demographic_data=demographics_data,
    otp_value="1234",
    consent=True
)

# Handle the response
if response.status_code == 200:
    decrypted_data = authenticator.decrypt_response(response.json())
    print("Authentication successful:", decrypted_data)
else:
    print("Authentication failed:", response.json())
```

#### **Error Handling**

The SDK provides clear error messages and codes to help diagnose issues effectively. Review the errors field in the response for details.

#### **Encryption and Decryption**

All communication with the MOSIP service is securely encrypted. Use the decrypt\_response method to handle encrypted responses appropriately.

### **Conclusion**

The **MOSIP Authentication SDK** simplifies the integration of robust authentication workflows into Python applications, ensuring secure, efficient, and compliant identity verification. By abstracting the complexities of direct API interaction, the SDK enables developers to focus on building impactful solutions without having to manage intricate implementation details.

### Get in Touch <a href="#get-in-touch" id="get-in-touch"></a>

If you require any assistance or encounter any issues during the testing and integration process, kindly reach out to us through the support provided below.

* Navigate to [Community](https://community.mosip.io/).
* Provide a detailed description about the support you require or provide complete information about the issue you have encountered, including steps to reproduce, error messages, logs and any other required details.

_Thank you. Wishing you a pleasant experience!_
