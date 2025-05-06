# Client management with PMS

*   e-Signet is onboarded as MISP partner in MOSIP system with the below policy:

    ```json
     {
     "allowAuthRequestDelegation": true,
     "allowKycRequestDelegation": true,
     "trustBindedAuthVerificationToken": true,
     "allowKeyBindingDelegation": true
     }
    ```
* License key of the MISP partner must be updated in the `esignet-default.properties`.\
  Property name : `mosip.esignet.misp.license.key`
* Create and Update of OIDC clients are managed via PMS.\


{% swagger src="../api/partner-management-service-openapi (1).json" path="/oidc/client" method="post" %}
[partner-management-service-openapi (1).json](<../api/partner-management-service-openapi (1).json>)
{% endswagger %}

{% swagger src="../api/partner-management-service-openapi.json" path="/oidc/client/{client_id}" method="put" %}
[partner-management-service-openapi.json](../api/partner-management-service-openapi.json)
{% endswagger %}

* Relying party is onboarded as an `Auth` partner. Auth partner is required to have the below allowed `auth-types` in the policy.



```json
{ "allowedAuthTypes" : [
                        {"authSubType":"","authType":"kycauth","mandatory":false},
                        {"authSubType":"","authType":"kycexchange","mandatory":false},
                        {"authSubType":"","authType":"otp-request","mandatory":false},
                       ]
}
```

* An auth-partner may have one or more OIDC clients.
* SHA-256 hash of the OIDC client public key is considered as `clientID`.
*   Authentication Context References (ACR) and user claims are derived based on the policy of the auth partner.

    a. `allowedKycAttributes` are used to derive user claims using the `identity_mapping.json`

    b. `allowedAuthTypes` are used to derive ACR values using the `amr-acr-mapping.json`
* Client management endpoints of e-Signet `oidc-service` is invoked from PMS with the derived values, `clientID` and the provided public-key.
* Also, the client-details with policy and partner details are sent as an event to MOSIP IDA system.

## Configurations

1. To get the mapping of OIDC claims with MOSIP KYC-attributes.
2. To get the mapping of auth types in policy with ACR values.
3. The claims supported by e-Signet should be present in `identity-mapping.json` file.

## Sample auth-policy of a relying party

```json
{"authTokenType":"policy",
  "allowedKycAttributes":[{"attributeName":"fullName"},
    {"attributeName":"gender"},
    {"attributeName":"phone"},
    {"attributeName":"email"},
    {"attributeName":"dateOfBirth"},
    {"attributeName":"city"},
    {"attributeName":"face"},
    {"attributeName":"addressLine1"}],
  "allowedAuthTypes":[{"authSubType":"IRIS","authType":"bio","mandatory":false},
    {"authSubType":"FINGER","authType":"bio","mandatory":false},
    {"authSubType":"","authType":"otp","mandatory":false},
    {"authSubType":"FACE","authType":"bio","mandatory":false},
    {"authSubType":"","authType":"otp-request","mandatory":false},
    {"authSubType":"","authType":"kycauth","mandatory":false},
    {"authSubType":"","authType":"kycexchange","mandatory":false},
    {"authSubType":"","authType":"wla","mandatory":false}]
}
```
