# Partner onboarding
## Onboarding steps
1. Add policy group
1. Add policy 
1. Add partner
1. Create partner and CA certificates
1. Upload certificates
1. Map partner to policy
1. Add MISP

## Prerequisites
1. Python3.9
1. Set up python3.9 virtual env
    ```sh
    mkdir ~/.venv
    python3.9 -m venv ~/.venv/partner
    ```
1. Switch to virtual env 
    ```
    source ~/.venv/partner/bin/activate
    ```
1. Install required modules
    ```sh
    pip install -r requirements.txt
    ```
1. Install utility Keystore Explorer.
2. 
## Creation of certificates
1. Run `create_cert.py` to create certificate for partners.  The input to this script is a json as given in following examples `input/partners`

## Onboard
1. Onboard partners using Postman collection. Note that the values here are samples, you need to plug-in values appropriately in the json params of each API call.
1. Use `single_line.sh` script to convert a cert from file to single line - required for Postman API calls.
1. After upload of partner certificates, MOSIP returns back signed partner certificate.  Make sure that existing certficate in .p12 keystore file of partner is replaced with this certificate.  You may use utlity Keystore Explorer for this.
 
## Various attributes
* **partnerType**: Partner types are pre-populated in `partner_type` table of `mosip_pms` DB and must not be altered.
* **policyType**:  One of `Auth/DataShare/CredentialIssuance` 
* **authTokenType**: One of `random/partner/policy`
* **partnerDomain**: One of `AUTH/DEVICE/FTM`.  These values are specified as `mosip.kernel.partner.allowed.domains` property in `kernel-default.properties` file.  For registration devices specify DEVICE.
* **app_id**: App Id from where certificate has to be pulled. Generally IDA.
* **cert_source**: `internal/generated/provided`. Cert may be already inside mosip, or has been provided external or needs to be generated.
* **overwrite**: Applicable with `cert_source==generated`. Whether to regenerate.
* **cert.country**: 2 Character country code
* **org_name**: Must match partner name.

## Certs
For internal certs the partner name must match the organisation name given in the cert.

## Policy group
* Multiple policies can be within policy group.
* Partner - policy group mapping is 1-1. 
* Within a policy group, partner can select multiple policies.

## Notes
* While adding a partner the same automatically gets added in Keycloak as well.
* IDA module is also like a partner to mosip system.  For biometric auth in Registration Processor, IDA Internal service is used.  In this case IDA has to be onboarded as `Online_Verification_Partner` with datashare policy.
* To generate p12 keystore for private key and certificate needed for print service, use this command:
```
$ openssl pkcs12 -export -inkey pvt_key.pem  -in cert.pem  -out key.p12
```
