# Policy Manager

### **Using Keycloak to allocate/get 'Partner Admin' and/or 'Policy Manager'**

Using the same partner admin credentials, follow the steps below so that the user can be configured with POLICYMANAGER role too.

Steps to configure **POLICYMANAGER** role in keycloak:

1. Go to keycloak and search your 'User-Name' in Users tab.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image2.png" alt=""><figcaption></figcaption></figure>

2. Go to the **Role Mapping** tab.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image3.png" alt=""><figcaption></figcaption></figure>

3. In the **Available Roles** section, select '**Policy Manager'**, click **Add** to move the selected role to the **Assigned Roles** list.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image4.png" alt=""><figcaption></figcaption></figure>

4. Log in to the **PMS** portal with the same user credentials, you should now have access to the **Admin Dashboard** with 'Policies' card accessible for use.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image5.png" alt=""><figcaption></figcaption></figure>

{% hint style="info" %}
**Note:**&#x20;

Both PARTNER\_ADMIN and POLICYMANAGER roles are necessary for **Policies** card to appear on Admin dashboard UI.

Therefore add **Policy Manager** role when you want that the 'Policies-Card' should also get enabled for you and turn you into a 'Policy Manager' as well.

If only PARTNER\_ADMIN role would have been configured, only the following cards would have been displayed:
{% endhint %}



This card is accessible for roles Partner Admin and Policy Manager.

In UI- both PARTNER\_ADMIN and POLICYMANAGER roles should be granted for the card to appear in the dashboard.

As a partner admin cum policy manager - creation and management of Policy Group, Authentication Policy, Datashare Policy is enabled within Policies card.



{% hint style="info" %}
**Note**:

If only 'Policy Manager' role is configured in keycloak, then the user will still be able to access the portals as a normal partner. Hence both; 'Partner Admin' & 'Policy Manager' roles are necessary to access all the cards/privileges above.
{% endhint %}

{% hint style="success" %}
**Important**:

After configuring the roles and if PMS portal is still logged in, make sure to logout and login again for the roles to get updated.
{% endhint %}

## Policies

You can use the 'Policies' to create and manage Policy Group, Authentication Policy and Datashare (You should have privileges of both; Partner Admin and Policy Manager).

The 'Policies' section is accessible to you only if both **Partner Admin** and **Policy Manager** roles are allocated to you and also the 'Policies' card will appears enabled on the the dashboard.

### Policies has following theree tabs:

* Policy Group, (This tab is selected by default)
* Authentication Policy,
* Datashare Policy

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image33.png" alt=""><figcaption></figcaption></figure>

**Policy Group**

Policy Group tab allows you to do following:

* View Policy Group
  * List view
  * Details View
* Create Policy Group
* Deactivate Policy Group

**Authentication Policy**

* View Authentication Policy
  * List view
  * Details View
* Create Authentication Policy (by mapping to an already created Policy Group)
* Deactivate Authentication Policy
* Clone Authentication Policy
* Edit Authentication Policy (Which is in draft status)
* Publish Authentication Policy (Which is in draft status so that the status changes to 'Activated')

**Datashare Policy**

* View Datashare Policy:
  * List view
  * Details view
* Create Datashare Policy
* Deactivate Datashare Policy
* Clone Datashare Policy
* Edit Datashare Policy (Which is in draft status)
* Publish Datashare Policy (Which is in draft status so that the status changes to 'Activated')

### Policy Group

#### View Policy Group

**List View - Policy Groups**

All the policy groups created so far by Partner Admin / Policy Manager are displayed on 'List of Policy Groups' page.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image33.png" alt="" width="427"><figcaption></figcaption></figure>

**Details View - Policy Group**

Admin can either click on 'Go Back' to redirect to 'List of Policy Groups' page as shown below or click on 'Home' to navigate back to Home page/ dashboard.

The options provided in 'Action menu are: View, Deactivate.

Clicking on View in action menu or by clicking the row item itself, admin is navigated to View Policy Group page where the policy group details are displayed along with its status: Activated or Deactivated.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image36.png" alt=""><figcaption></figcaption></figure>

**Create Policy Group**

On clicking the 'Create Policy Group' option on the top right of the screen, we can create a Policy Group by providing suitable name and description that is self explanatory for partners, who would be selecting them during Partner Policy Request to create API Key / OIDC Client etc.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image34.png" alt=""><figcaption></figcaption></figure>

On click of Submit, a success message appears.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image35.png" alt=""><figcaption></figcaption></figure>

**Deactivate Policy Group**

If the admin wants to deactivate the Policy Group, then click on Deactivate option in action menu.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image37.png" alt=""><figcaption></figcaption></figure>

A popup window appears seeking for confirmation before proceeding to deactivate.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image38.png" alt=""><figcaption></figcaption></figure>

After confirming deactivation, the respective record is greyed out in the tabular view.

The action menu here is enabled with only View option. (Deactivate in action menu is disabled).

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image39.png" alt=""><figcaption></figcaption></figure>

After deactivation, the View policy group page [MOSIP-36963](https://mosip.atlassian.net/browse/MOSIP-36963) will display 'Deactivated' status

Once the policy group is deactivated by Policy Manager, the partner will not be able to fetch this policy group in any of the screens in their PMS portal.

{% hint style="info" %}
**Note:**

Policy Group cannot be deactivated if there are active or draft policies associated to the given policy group.
{% endhint %}

If the Policy Group has active or draft policy / policies associated to it, then on clicking Confirm, following error message is displayed along with the count of such policies -

a) In case of Active and Draft policies associated to Policy Group:

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image40.png" alt=""><figcaption></figcaption></figure>

b) In case of Active policies associated to Policy Group:

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image41.png" alt=""><figcaption></figcaption></figure>

c) In case of Draft policies associated to policy group:

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image42.png" alt=""><figcaption></figcaption></figure>

#### Authentication Policy / Datashare Policy:

* On clicking Authentication Policy tab, List of all previously created Authentication Policies are displayed.
* On clicking Datashare Policy tab, List of all previously created Datashare Policies are displayed.

{% hint style="info" %}
**Note**:

The steps and features are same for both Authentication and Datashare Policy.
{% endhint %}

Policies can have the following status - Draft, Activated or Deactivated.

1. Only Draft or Activated row items are clickable which navigates to View Authentication Policy details.
2. Action - Action menu displays a common menu item (View, Clone, Deactivate) with only the following menu items enabled for clicking based on below statuses:
   1. Draft: Publish, View, Edit
   2. Activated: View , Clone , Deactivate
   3. Deactivated: View

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image43.png" alt=""><figcaption></figcaption></figure>

**Create Authentication Policy**

On clicking 'Create Authentication Policy' button, Partner Admin / Policy manager is navigated to Create Authentication Policy page where details such as policy group, policy name, description etc will have to be entered.

{% hint style="info" %}
**Note**:

Only active policy groups are available in the policy group dropdown.
{% endhint %}

Click on the upload button to upload policy data. Only json files are allowed for upload.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image44.png" alt=""><figcaption></figcaption></figure>

Before saving the policy in draft, the policy data can be edited in the text area after policy data json file has been successfully uploaded.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image45.png" alt=""><figcaption></figcaption></figure>

On clicking on Save as Draft, following success message appears.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image46.png" alt=""><figcaption></figcaption></figure>

On clicking 'Go Back', admin is navigated back to List view where the policy is saved as 'draft' status.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image47.png" alt=""><figcaption></figcaption></figure>

The Edit option provided to Draft policy can be used by admin to make any changes in the policy details (except policy group) before publishing the policy.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image48.png" alt=""><figcaption></figcaption></figure>

On submitting after making required changes, a success message appears.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image49.png" alt=""><figcaption></figcaption></figure>

To publish policy which is currently in draft status, click on 'publish' option in action menu. A popup window appears seeking for confirmation to publish.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image50.png" alt=""><figcaption></figcaption></figure>

On clicking Publish, a success message appears . Click on close to close the window.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image51.png" alt=""><figcaption></figcaption></figure>

The given policy changes to 'Activated' status after being published. Once activated, the admin cannot edit the policy, hence the option is disabled.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image52.png" alt=""><figcaption></figcaption></figure>

**Clone Policy**

To clone any active policy onto another policy group, click on 'clone' in action menu. A popup window appears to select the policy group where the policy has to be cloned.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image53.png" alt=""><figcaption></figcaption></figure>

On selecting the policy group where policy has to be cloned, click on Clone and a success message appears.

Click on Close to navigate back to List of Authentication Policies screen.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image54.png" alt=""><figcaption></figcaption></figure>

**Deactivate Policy**

To deactivate a policy, click on Deactivate option in action menu of any activated policy record. A popup window appears seeking for confirmation.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image55.png" alt=""><figcaption></figcaption></figure>

{% hint style="info" %}
**Note**:

If the Policy has active partners associated to it i.e. there are **Approved** partner policy requests, then on clicking Confirm, following error message is displayed and the admin will be restricted to deactivate such policy groups.
{% endhint %}

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image56.png" alt=""><figcaption></figcaption></figure>

{% hint style="info" %}
**Note:**

1. Policy can be deactivated if there are no policy requests associated with this policy.
2. Policy can be deactivated if there are Rejected policy requests associated with this policy.
3. Policy cannot be deactivated if there are pending policy requests associated with this policy. In this case , following error message is displayed- '\<title> Error: Partner - Policy Request Detected! \<Description> Pending policy requests are associated with this policy. Please take appropriate action in List of Partner Policy Linking screen'.
4. Once the policy is deactivated by partner admin/policy manager, the partner will not be able to fetch this policy in any of the screens in their PMS portal.
{% endhint %}

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image57.png" alt=""><figcaption></figcaption></figure>

**Viewing Policy**

On clicking **View** option of any policy or by clicking the row item itself, admin is navigated to View Authentication Policy where policy details can be viewed. Also click on preview to view the policy data in json format.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image58.png" alt=""><figcaption></figcaption></figure>

On clicking preview, policy data can be viewed in json format and an option to Download the data in local system is provided.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image59.png" alt=""><figcaption></figcaption></figure>

#### Partner - Policy Linking:

The features provided to Partner Admin:

1. Approve/ Reject Policy requested by partner - clicking on 'Approve/ Reject' option in action menu of a policy record whose status is in pending for approval
2. Tabular view of Policies requested by partners along with the status
3. View individual policy request details : Either on clicking on view option in action menu of any of the active policy request in the tabular view or by clicking on the row item itself, it navigates to View Policy Request details page.

All the policy requests created by various partners are displayed in 'List of Partner - Partner Linkages' . The different statuses possible are: Pending for Approval, Approved, Rejected, Deactivated.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image60.png" alt=""><figcaption></figcaption></figure>

The options provided for policy linking requests in 'Pending for Approval' are to Approve/ Reject. Also an option to view the policy request details is also provided.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image61.png" alt=""><figcaption></figcaption></figure>

On clicking the Approve/ Reject option, the window appears - and partner admin can click on either Approve or Reject to take appropriate action

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image62.png" alt=""><figcaption></figcaption></figure>

The status- Approved / Rejected gets updated in the tabular view.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image63.png" alt=""><figcaption></figcaption></figure>

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image64.png" alt=""><figcaption></figcaption></figure>

On clicking view of active record or the row item itself, the partner- policy linking view page is displayed along with comment history where partner comments and admin's approval status is displayed.

<figure><img src="../../../../.gitbook/assets/temp-pms-admin-image65.png" alt=""><figcaption></figcaption></figure>
