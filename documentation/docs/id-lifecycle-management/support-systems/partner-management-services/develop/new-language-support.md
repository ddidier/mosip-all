# New Language Support


## Overview

This guide provides step-by-step instructions for adding support for a new language in PMS application that uses react-i18next for internationalization. The i18n.js file is already configured to dynamically load language files based on the selected locale. Currently, the application supports English, French, and Arabic. For demonstration, we will add Spanish as an additional language.

## Repository

The PMS React application source code is available at: [PMS Revamp UI Repository](https://github.com/mosip/partner-management-portal/tree/release-1.2.2.x/pmp-revamp-ui).

## Adding a New Language

#### Add a New Translation File

Each language in the application is stored as a separate JSON file inside the /pmp-revamp-ui/public/i18n directory. To add Spanish (es), create a new translation file:

Open es.json and add the translations required, for example:

```json
{

"dashboard": {

"welcomeMsg": "Bienvenido \{{firstName\}} \{{lastName\}}",

"accountStatus": "Estado de la cuenta"

}

}
```

#### Add Language Locale in Keycloak

We use Keycloak for authentication, and users select their preferred language during login. Once logged in, the selected language is picked up by the i18n bundle. This requires updating Keycloak's supported locales.

**Steps to Enable Spanish in Keycloak**

1. **Log in to Keycloak Admin Console** as an administrator.
2. **Navigate to Realm Settings**:
   * Select the appropriate realm.
   * Go to the "Realm Settings" section.
   * Click on the **"Themes"** tab.

<figure><img src="../../../../.gitbook/assets/temp-pms-nls-image1.png" alt=""><figcaption></figcaption></figure>


3. **Add Spanish to Supported Locales**:
   * Ensure that "Internationalization" is enabled.
   * In the "Supported Locales" field, add es (for Spanish) to the list.
   * Click "Save" to apply changes.

<figure><img src="../../../../.gitbook/assets/temp-pms-nls-image2.png" alt=""><figcaption></figcaption></figure>
       
4. **Verify Language Selection in Keycloak Login Page:**
   * Navigate to the Keycloak login page.
   * Ensure that Spanish appears as an option in the language selection dropdown.

<figure><img src="../../../../.gitbook/assets/temp-pms-nls-image3.png" alt=""><figcaption></figcaption></figure>


#### Rebuild the Application

After adding the new language bundle, you must **rebuild the code** to ensure the new translations are correctly loaded into the application.

**Steps to Rebuild the React Application**

1. Open a terminal or command prompt.
2. Navigate to your React project directory:

`cd partner-management-portal\pmp-revamp-ui`

3. Install dependencies (if not already installed):

`npm install`

4. Build the project:

`npm run build`

5. Deploy the updated build to your hosting environment.

#### Verify Language Support in PMS Application

1. Open the application in a browser.
   * Select Spanish from the language options on the Keycloak login page.
   * Confirm that the UI displays the correct Spanish translations after logging in.

<figure><img src="../../../../.gitbook/assets/temp-pms-nls-image4.png" alt=""><figcaption></figcaption></figure>

By following these steps, you can easily add support for any new language in your application.
