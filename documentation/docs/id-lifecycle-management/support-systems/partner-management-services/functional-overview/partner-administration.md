# Partner Administration

## Partner Admin & Policy Manager

### What all activities does a 'Partner Admin' perform?

Partner Admin supervises the overall partner and policy management functionalities in PMS. The admin is responsible for:

* Upload Root Certificate
* Intermediate CA Certificates
* Manage Partners and Policies
* Approve / Reject new entries created by different partners or deactivate partner related records

{% hint style="info" %}
**Note**:&#x20;

Partner Admin can also assume the role of Policy Manager to:

* Create and manage Policy Group(s) and Policies
{% endhint %}

#### What all activities does a 'Policy Manager' perform?

As a partner admin cum policy manager you can performs following:

1. Create and manage Policy Group, Authentication Policy and Datashare Policy

{% hint style="info" %}
**Note**:

For the '**Policies**' card to appear on the dashboard, both; (Partner Admin and Policy Mangaer) roles must be granted.
{% endhint %}

#### Registering as Partner Admin

Partner admin too has to self-register just like any other 'Partner Registration' by selecting any one of the partner type i.e.

You (Partner Admin) can first self-register in PMS as any one of the partner type and then get the various roles/privileges by going to Keycloak.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image1.png" alt=""><figcaption></figcaption></figure>

**Using Keycloak to allocate/get 'Partner Admin' and/or 'Policy Manager'**

After registration you need to go to keycloak to enable roles.

1. Go to keycloak and search your 'User-Name' in Users tab.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image2.png" alt=""><figcaption></figcaption></figure>

2. Go to the **Role Mapping** tab.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image3.png" alt=""><figcaption></figcaption></figure>

3. In the **Available Roles** section, select **Partner Admin** or **Policy Manager**, click **Add** to move the selected role to the **Assigned Roles** list.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image4.png" alt=""><figcaption></figcaption></figure>

4. You can now log in to the **PMS** portal with the same user credentials and you will have access to the **Admin Dashboard**.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image5.png" alt=""><figcaption></figcaption></figure>

**Note:** Add **Policy Manager** role when you want that the 'Policies-Card'/ 'Priviledge' should also get enabled for you and turn you into a 'Policy Manager' as well.

#### Registering as Policy Manager

By following the above steps (1-4) in keycloak, the admin can also configure **Policy Manager** role to enable and manage **Policies** card as shown in the dashboard below:

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image6.png" alt=""><figcaption></figcaption></figure>

{% hint style="info" %}
**Note**:

If only 'Policy Manager' role is configured in keycloak, then the user will still be able to access the portals as a normal partner. Hence both; 'Partner Admin' & 'Policy Manager' roles are necessary to access all the cards/privileges above.
{% endhint %}

{% hint style="success" %}
**Important**:&#x20;

After configuring the roles and if PMS portal is still logged in, make sure to logout and login again for the roles to get updated.
{% endhint %}

### Certificate Trust Store

Certificate Trust Store provides features such as Upload, Download, View Root CA and Intermediate CA certificates to Partner Admin such that at the time of CA Signed Certificate upload by partner MOSIP verifies the certificate chain of trust and then signs the partner's certificate using MOSIP(PMS) private key.

* Root Trust (Root CA) Certificate
* Intermediate Trust (Intermediate CA) Certificate

#### Root Trust (Root CA) Certificate Features

You can use the 'Root Trust (Root CA) Certificate' section to do the following:

* View Root CA
  * List View
  * Details View
* Upload Certificate: Upload Root CA certificate such that the root of trust can be verified when an intermediate CA is uploaded.
* Download Root CA: Download the root certificate as and when needed.

**View Root CA Certificate**

**Certificate Trust Store (List View)**

* Go to Dashboard > Certificate Trust Store, all the CA Certificates appaears in 'List View' as in figure below:

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image7.png" alt=""><figcaption></figcaption></figure>

In Certificate Trust Store (List View), the user can view the list of '**Root CA Certificates**' uploaded by admin till date with details such as **Certificate ID**, **Partner Domain**, **Issued To**, **Issued By**, **Validity Period** and **Validity Status** (Valid / Expired) etc.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image8.png" alt=""><figcaption></figcaption></figure>

Each active certificate record has two options in action menu - **View** and **Download** Certificate.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image9.png" alt=""><figcaption></figcaption></figure>

**View Root CA details**

On clicking View, the Root CA certificate detail can be viewed individually.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image10.png" alt=""><figcaption></figcaption></figure>

**Upload Root CA**

To upload Root CA / Intermediate CA Certificate, click on 'Upload Trust Certificate'.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image9.png" alt=""><figcaption></figcaption></figure>

Admin is thus navigated to Upload Trust Certificate page.

{% hint style="info" %}
**Note**:

Admin can upload Root CA / Intermediate CA certificate in the same page but should be in a sequential order ie. Root CA Certificate upload first and then Corresponding Intermediate CA certificate upload.
{% endhint %}



Select the partner domain (AUTH / DEVICE / FTM) **in the Upload section**. Partner Domain typically refers to the specific functional area for which the **Root or Intermediate CA certificate** is being uploaded.

* AUTH: Select Partner domain as AUTH if **Root or Intermediate CA certificate** is being uploaded for Authentication Partner.
* DEVICE: Select Partner domain as DEVICE if **Root or Intermediate CA certificate** is being uploaded for Device Provider.
* FTM: Select Partner domain as FTM if **Root or Intermediate CA certificate** is being uploaded for FTM Chip Provider.



{% hint style="info" %}


**Note**:

* Only .cer or .pem format certificates are allowed for upload
* Future dated certificates is not allowed for upload, in case it is attempted an error message is thrown.
* Only Version 3 certificate is allowed for upload.
* If the corresponding root certificate is not uploaded, then while submitting the Intermediate certificate upload, an error message appears asking 'Please upload corresponding Root Certificate to proceed further'.
{% endhint %}

{% hint style="success" %}


**Note for Root CA Certificate**:

* Issued To and Issued By is the same - which means these are self signed certificates.
{% endhint %}



**Download Root CA**

In the same page (Root CA details), an option to download the Root CA certificate in .p7b file is also provided. Clicking on download, a success message appears.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image11.png" alt=""><figcaption></figcaption></figure>

On opening the .p7b file from local system, the Root CA Certificate can be viewed as below:

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image12.png" alt=""><figcaption></figcaption></figure>

#### Intermediate Trust (Intermediate CA) Certificate

* View Intermediate CA
  * View Intermediate CA: List View of all uploaded Intermediate CA certificates is displayed.
  * View Intermediate Certificate details: Details View of uploaded intermediate certificate is displayed along with the list of certificates within the certificate trust chain.
* Upload Root CA Certificate: Partner Admin can upload **Intermediate CA** certificate so that the root of trust can be verified when a partner uploads Partner / FTM Chip Certificate.
* Download Certificate Chain of Trust: Partner Admin downloads the certificate chain of trust of intermediate certificate as and when needed.

**Viewing the Intermediate CA Certificate**

**Intermediate CA Certificates (List View)**

On clicking the Intermediate CA tab, List of all Intermediate CA certificates uploaded by Partner Admin is displayed.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image16.png" alt=""><figcaption></figcaption></figure>

Action menu for all active certificates displays the following options:

* View
* Download Certificate Chain

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image17.png" alt=""><figcaption></figcaption></figure>

**Viewing the Intermediate CA Certificate (Details View)**

Either by clicking on the row item or the View option in action menu, the admin is navigated to View Intermediate CA Certificate details page where the certificate details are displayed such as Certificate ID, Partner Domain - (AUTH, FTM, DEVICE), Issued To- _\<subject > field of Certificate,_ Issued By- _\<issuer > field of Certificate,_ Valid From, Valid To\*- same as system browser date format\* etc

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image18.png" alt=""><figcaption></figcaption></figure>

**Upload Intermediate Certificate**

To upload the Intermediate CA certificate, carry out the same steps of Root CA Certificate upload(explianed above).

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image21.png" alt=""><figcaption></figcaption></figure>

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image22.png" alt=""><figcaption></figcaption></figure>

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image23.png" alt=""><figcaption></figcaption></figure>

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image24.png" alt=""><figcaption></figcaption></figure>

{% hint style="info" %}
**Note**:

* The Subject of the root certificate matches the Issuer of the intermediate certificate.
* Issued To and Issued By are different as the Intermediate CA certificate is signed by the Root CA.
* Intermediate certificate must expire before its root certificate.
* Validity of Root CA Certificate > Intermediate CA Certificate > CA Signed Partner Certificate
* Sequence of Upload: Root CA Certificate (by Partner Admin)→ Intermediate CA Certificate (by Partner Admin) → CA signed Partner Certificate (by Partner)
{% endhint %}



**Downloading the Intermediate CA Certificate**

Clicking on Download, downloads the entire certificate chain as .p7b file and a success message is displayed - 'Certificate Chain of Trust for the given Intermediate CA certificate is downloaded successfully'.

{% hint style="info" %}
**Note:** For expired status, 'Download Certificate Chain' button will be disabled in View Root Certificate page / Tabular View page.
{% endhint %}



<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image19.png" alt=""><figcaption></figcaption></figure>

On clicking the .p7b file from local system, the certificate hierarchy of the intermediate CA certificate is present where its corresponding root certificate is also downloaded.

## Partners

As a **Partner Admin** you can view the list of all partners who have enrolled to PMS portal by clicking on the Partners card on dashboard or side panel, hamburger menu.

### ‘Partner’ Features:

1. View Partner

* List View - (Action menu: View, Deactivate)
* Details View - of individual Partner and the certificate details

3. Download original Partner Certificate and MOSIP Signed certificate
4. Deactivate Partner

#### View Partner Details

**Viewing a Partner**

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image25.png" alt=""><figcaption></figcaption></figure>

{% hint style="info" %}
**Note:** Deactivate option appears disabled if the partner is already deactivated.
{% endhint %}



<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image26.png" alt=""><figcaption></figcaption></figure>

**Viewing a Partner's details**

Click on a row item or use the view option in action menu you come to 'Partner Details Page' to view the Partner Details such as **Partner type**, **Organisation name**, First Name, Last Name, Phone Number, Email Address, Policy Group (If partner is of the type 'Authentication Partner'). Partner certificate details are also visible.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image27.png" alt=""><figcaption></figcaption></figure>

#### Download original certificate / MOSIP Signed certificate

The admin can download original certificate / MOSIP Signed certificate as and when necessary.

{% hint style="info" %}
**Note:**

The download functionality of following certificates is possible only during following instances:

* This button is enabled only for Activated partner record of which the certificate is already uploaded.
* This button is disabled for deactivated partner records/partner records whose partner certificate is not uploaded yet.
* If Original Certificate / MOSIP Signed Certificate is expired then on clicking respective menu items in the button-dropdown an appropriate error message is displayed.
{% endhint %}



<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image28.png" alt=""><figcaption></figcaption></figure>

On downloading the Original / MOSIP Signed certificate, a success message appears.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image29.png" alt=""><figcaption></figcaption></figure>

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image30.png" alt=""><figcaption></figcaption></figure>

#### Deactivate a Partner

To deactivate a partner, click on Deactivate option in action menu. A popup window appears seeking for confirmation from the partner.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image31.png" alt=""><figcaption></figcaption></figure>

After confirming deactivation, the respective record is greyed out in the tabular view. The action menu here appears enabled with only 'View' option after deactivation and Deactivate in action menu is disabled.

{% hint style="info" %}
**Note:**&#x20;

After deactivation, the View partners page will display the following&#x20;

1. 'Deactivated' status.
2. Certificate section is greyed out with and download button is disabled.
{% endhint %}



<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image32.png" alt=""><figcaption></figcaption></figure>

The deactivated partner will not be able to create or utilize any of the services in their PMS portal (For e.g. no new transactions will work such as creation of OIDC Client , API Key etc).

{% hint style="warning" %}
**Known Issue:**&#x20;

Even after partner deactivation partner is able to access the existing transactions in their PMS portal such as following:

1. Existing OIDC client ids are still operational for Authentication Partner.
2. Existing API keys are still operational for Authentication Partner.
3. SBI / Devices / FTM - trust validation does not fail even after partner deactivation.
{% endhint %}


## **SBI - Device:**

SBI - Device is exclusively used to manage Device Provider's requests on SBI and Device creation.

The 'SBI-Devices' has 2 Tabs namely **SBI and Device**. SBI tab view is selected by default

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image66.png" alt=""><figcaption></figcaption></figure>

**SBI features**

* View SBI -
  * List View of SBIs created by Device Providers along with the status
  * Details View - View submitted SBI details,either on clicking on view option in action menu of any of the submitted SBI details in the tabular view or by clicking on the active row item itself, it navigates to View SBI details page
* Approve/ Reject SBIs - On clicking Approve/Reject in action menu of Pending for Approval records
* Deactivate an SBI - On clicking Deactivate option in action item of activated records in Tabular view screen
* View Linked Devices - Of a given SBI can be viewed through a filtered search on the pre-selected SBI

**Device features:**

* View
  * List View: Of Devices created by Device Providers along with the status
  * View submitted Device details : Either on clicking on view option in action menu of any of the submitted API key details in the tabular view or by clicking on the row item itself, it navigates to View device details page
* Approve/ Reject devices: On clicking Approve/Reject in action menu of Pending for Approval records
* Deactivate Device: On clicking Deactivate option in action item of activated records in Tabular view screen
* List of all SBIs created by various different device providers are available here. Any SBIs that are pending for approval can be approved/ rejected

## SBI

#### Approve or reject SBI

Go to Dashboard → SBI-Device → List of SBIs to Approved or Reject.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image67.png" alt=""><figcaption></figcaption></figure>

Select on Approve / Reject option from the given record and chooses appropriate action.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image68.png" alt=""><figcaption></figcaption></figure>

On approval, the status changes to 'Approved' and on rejection, the status changes to 'Rejected'

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image69.png" alt=""><figcaption></figcaption></figure>

You can click on View option in the action menu to view any individual records,

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image70.png" alt=""><figcaption></figcaption></figure>

To approve or reject an SBI, select the approve / reject option in action menu.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image62.png" alt=""><figcaption></figcaption></figure>

The approved / rejected status is updated on the tabular view.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image71.png" alt=""><figcaption></figcaption></figure>

To know the list of linked devices associated to this SBI, click on the linked devices count in the tabular view or in the individual view page.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image72.png" alt=""><figcaption></figcaption></figure>

#### Deactivate SBI

To deactivate an SBI, click on Deactivate option in action menu. An alert appears seeking for confirmation. Also admin is informed how the linked devices will be impacted after SBI deactivation.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image73.png" alt=""><figcaption></figcaption></figure>

After confirming Deactivation the respective SBI record is greyed out and the status is displayed as 'Deactivated'.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image74.png" alt=""><figcaption></figcaption></figure>

**Impact on linked devices after SBI deactivation**

Impact on linked devices after SBI deactivation is as below:

1. All approved device records are displayed in 'Deactivated' status and those row items being greyed out. The action menu in such records should be enabled with only View option, (Deactivate in action menu is disabled).
2. The devices of which the status was 'Pending for Approval' before SBI deactivation will now be displayed with 'Rejected' status.
3. Rejected devices will continue to remain in the same status even after SBI deactivation.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image75.png" alt=""><figcaption></figcaption></figure>

### **Device**

#### View Devices

On clicking 'Devices' tab, **List of all Devices** submitted so far are displayed.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image76.png" alt=""><figcaption></figcaption></figure>

Click on view option in action menu or the row item itself (of any active device record) to view the device details individually.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image80.png" alt=""><figcaption></figcaption></figure>

#### Approve / Reject Devices

On clicking the action menu of the respective device record, an option 'Approve / Reject' is provided

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image77.png" alt=""><figcaption></figcaption></figure>

A popup window appears for the admin to take appropriate action - Approve / Reject and select the respective button

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image78.png" alt=""><figcaption></figcaption></figure>

The status is thus updated accordingly in **List of Devices** Page as Approved / Rejected based on the above action.

'Pending for Approval' status is displayed when the device request is pending with admin for approval and no action has been taken by admin yet.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image79.png" alt=""><figcaption></figcaption></figure>

#### Deactivate Device

Click on deactivate option in action menu. A confirmation window appears to proceed for deactivation.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image81.png" alt=""><figcaption></figcaption></figure>

The deactivated device record is greyed out and status is also changed to 'Deactivated'

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image82.png" alt=""><figcaption></figcaption></figure>

## **FTM Chip:**

The following features are provided to admin to manager FTM Chip Provider's requests:

* View FTM Chip
  * View List View: Of FTM chip details]{.underline} along with the status of approval
  * View FTM details: Either on clicking on view option in action menu of active FTM Chip details in the tabular view or by clicking on the row item itself, it navigates to View FTM details page
* Approve / Reject FTM chip details: submitted by FTM Chip Providers
* Download FTM Chip Certificate: On clicking on Download option within FTM Chip Certificate section in 'View FTM Chip Certificate' page, then originally uploaded FTM Chip certificate can be downloaded
* Deactivate FTM detail: On clicking on 'Deactivate' option in action menu of approved records in Tabular view of FTM details screen, the respective FTM detail along with its certificate will be deactivated.

### View FTP Chip Details

The List of FTM Chip details displays all FTM Chip details created by FTM Chip Provider

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image83.png" alt=""><figcaption></figcaption></figure>

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image84.png" alt=""><figcaption></figcaption></figure>

You can navigate to view 'List of FTM Chip details' page where list of all FTM Chip records submitted so far by different FTM Chip providers.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image85.png" alt=""><figcaption></figcaption></figure>

### View Details of FTM Chip

To view FTM Chip details indivudally, click on View option in action menu

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image89.png" alt=""><figcaption></figcaption></figure>

### Approve / Reject FTM Chip

Click on the action menu of the respective FTM Chip record, an option 'Approve/ Reject' is provided

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image86.png" alt=""><figcaption></figcaption></figure>

A popup window appears for the admin to take appropriate action - Approve / Reject and select the respective button

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image87.png" alt=""><figcaption></figcaption></figure>

The status is thus updated accordingly in **List of Devices** Page as Approved / Rejected based on the above action.

{% hint style="info" %}
**Note**: 'Pending for Approval' status is displayed when the FTM Chip request is pending with admin for approval and no action has been taken by admin yet.
{% endhint %}



<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image88.png" alt=""><figcaption></figcaption></figure>

### Download FTM Chip Certificate

To download the FTM Chip Certificate uploaded by FTM Chip Provider, click on download button.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image90.png" alt=""><figcaption></figcaption></figure>

To deactivate an FTM Chip record, click on Deactivate option in action menu and a confirmation popup appears.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image91.png" alt=""><figcaption></figcaption></figure>

The deactivated FTM Chip record is greyed out after deactivation.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image92.png" alt=""><figcaption></figcaption></figure>

## Authentication Services

Authentication Services has two tabs namely **OIDC Client and API key**. OIDC Client tab view is selected by default.

**OIDC Client**

* View OIDC Client
  * Lis view of OIDC clients created by partners along with the status
  * View submitted OIDC Client details: Either on clicking on view option in action menu of any of the submitted OIDC details in the tabular view or by clicking on the row item itself, it navigates to View OIDC Client details page
* Deactivate an OIDC Client: On clicking Deactivate option in action item of activated records in Tabular view screen

**API Key**

* View API Keys
  * Tabular view of API keys: Generated by partners along with the status
  * View submitted API Key details: Either on clicking on view option in action menu of any of the submitted API key details in the tabular view or by clicking on the row item itself, it navigates to View API key details page
* Deactivate: API key on clicking Deactivate option in action item of activated records in Tabular view screen

### OIDC Client

#### View OIDC Clients

Within OIDC Client tab, all OIDC Clients created by various Authentication partners are displayed.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image93.png" alt=""><figcaption></figcaption></figure>

For Activated records → the action menu has two options: View, Deactivate

For Deactivated records → the action menu is enabled with only 1 option: View, Deactivate.

On clicking view option in action menu, the admin is redirected to View OIDC Client details page.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image94.png" alt=""><figcaption></figcaption></figure>

#### Deactivate OIDC Client:

On clicking view option in action menu, the admin is redirected to View OIDC Client details page.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image95.png" alt=""><figcaption></figcaption></figure>

### API Key

#### View API Key

To view the list of all API Keys created by Authentication partner, click on API Key tab

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image96.png" alt=""><figcaption></figcaption></figure>

For Activated records → the action menu has two options: View, Deactivate

For Deactivated records → the action menu is enabled with only 1 option: View, Deactivate.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image97.png" alt=""><figcaption></figcaption></figure>

On clicking view option in action menu, the admin is redirected to View API Key details page.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image98.png" alt=""><figcaption></figcaption></figure>

#### Deactivate an API Key

To deactivate an API Key, click on deactivate option in action menu.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image99.png" alt=""><figcaption></figcaption></figure>

The deactivated record is greyed out and is updated with Deactivated status.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image100.png" alt=""><figcaption></figcaption></figure>
