---
description: >-
  Collaborate seamlessly with MOSIP through comprehensive partner guides,
  onboarding processes, and policy frameworks.
---

# Partners

## Overview

The MOSIP platform requires integration with several other systems. Typically, a System Integrator (SI) would assemble all the pieces to build a complete national ID solution. All entities that participate in providing the external components are called MOSIP Partners.

The below diagram illustrates the **MOSIP Ecosystem**, highlighting how the MOSIP platform integrates with various components to provide a complete ID solution.

![MOSIP Ecosystem](../../../.gitbook/assets/mosip-ecosystem.jpg)

## Partner types

| Partner type                         | Description                                                                                                                                                                                                                   | Label\*                       |
| ------------------------------------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------- |
| Authentication Partner/Relying Party | Entities that use MOSIP for authentication like banks, telecom, Govt. institutes etc.                                                                                                                                         | `Auth_Partner`                |
| Online Verification Partner          | Authorised and entrusted partners who host [IDA](../../identity-verification/id-authentication.md) module to provide authentication service to various partners. Even MOSIPs IDA module an is an Online Verification Partner. | `Online_Verification_Partner` |
| Credential Partner                   | Provider of credentials like printed ID card, QR code etc. to residents                                                                                                                                                       | `Credential_Partner`          |
| Device Provider                      | Provider of biometric devices that connect to registration client and authentication apps                                                                                                                                     | `Device_Provider`             |
| FTM Provider                         | Providers of [L1](../../supporting-components/biometrics/biometric-devices.md#l1) compatible [FTM](../../supporting-components/biometrics/ftm.md) integrated in biometric devices                                             | `FTM_Provider`                |
| Manual Adjudication                  | Providers of Manual Adjudication Systems(MAS); enrollment data is shared with MAS                                                                                                                                             | `Manual_Adjudication`         |
| ABIS Partner                         | Provider of [ABIS](../../supporting-components/biometrics/abis.md)                                                                                                                                                            | `ABIS_Partner`                |
| MISP Partner                         | MOSIP Infra Service Provider (MISP) provide network infrastructure/channel/pipe to various Authentication Partners to connect to the MOSIP system. Example, broadband service providers.                                      | `MISP_Partner`                |

{% hint style="info" %}
\* Label: Please refer to in `partner_type` table of `mosip_pms` database.
{% endhint %}

## Partner Policies

The MOSIP Partner Policy establishes a structured framework for collaboration between MOSIP adopters and their partners and defines the rules for data access and sharing.&#x20;

It specifies which partners can access what information and the procedures for requesting it, for instance, a registered print provider automatically receives data via WebSub if designated under the print policy whereas an authentication partner must actively call the authentication system to retrieve data based on policy guidelines. If eKYC is permitted, the partner may receive additional personally identifiable information (PII).&#x20;

The policy ensures granular control over shared attributes allowing different partners to receive varying levels of information; one may access only a name while another may obtain both a name and a photograph. This structured approach enhances security, compliance, and flexibility.

To learn more about partner policies please refer [here](https://docs.mosip.io/1.2.0/modules/partner-management-services/pms-revamp/functional-overview/auth-partner/end-user-guide#creating-policy-group-and-policy).

## Partner onboarding

Onboarding of a partner refers to registering a partner in a particular deployment of MOSIP. Partners need to be onboarded to establish trust. The onboarding process consists of loading partner details in the database, exchanging certificates, etc, detailed in the later sections. Such onboarding is required to be done on any fresh MOSIP installation. For instance, if you install a sandbox, you would need to follow the onboarding process for each partner.

The sections below describe the onboarding process for each type of partner.

### MISP

1. MISP should have a trusted X.509 certificate with a chain of CA certificates.
2. MISP self-registers on the PMS portal providing partner ID, name, organization name (same as in certificate), partner type (`MISP_type`) (_This functionality will be available on the portal in the 1.2.x version of MOSIP_)
3. MISP uploads all certificates.
4. MOSIP Admin generates the MISP license key and provides it to MISP.

### Authentication Partner (AP)

1. Policy for the AP must be pre-defined (see [Partner policies](partners.md#partner-policies)).
2. AP should have a trusted X.509 certificate with a chain of CA certificates.
3. AP registers with MISP and obtains the MISP license key (this setup is outside of the MOSIP system).
4. The MISP used by AP should have been already onboarded onto MOSIP.
5. AP self-registers on the PMS portal providing partner ID, name, organization name (same as in certificate), partner type (`Auth_Partner`) etc.
6. AP uploaded all certificates.
7. AP selects the policy group and policy. This request is sent to the MOSIP Admin for approval.
8. On approval, AP generates an API key that can be used along with the MISP license key to interact with the IDA system.

### Device Provider (DP)

1. DP should have a trusted X.509 certificate with a chain of CA certificates.
2. DP self-registers on the PMS portal providing partner ID, name, organization name (same as in certificate), partner type (`Device_Provider`) etc.
3. DP uploads all certificates.
4. Any approval from MOSIP? (TODO)

### FTM Provider (FTMP)

1. FTMP should have a trusted X.509 certificate with a chain of CA certificates.
2. FTMP self-registers on the PMS portal providing partner ID, name, organization name (same as in certificate), partner type (`FTM_Provider`) etc.
3. FTMP uploads all certificates.
4. TODO

### Credential Partner (CP)

1. Datashare policy must be pre-defined (see [Partner policies](partners.md#partner-policies)).
2. CP should have a trusted X.509 certificate with a chain of CA certificates.
3. CP self-registers on the PMS portal providing partner ID, name, organization name (same as in certificate), partner type (`Credential_Partner`) etc.
4. CP uploads all certificates.
5. CP selects the policy group and policy.
6. CP maps policy to one of the supported [credential types](https://github.com/mosip/id-repository/tree/release-1.2.0/id-repository/credential-service).
7. CP adds biometric extractors for the policy.

### Online Verification Partner (OVP)

1. Datashare policy must be pre-defined (see [Partner policies](partners.md#partner-policies)).
2. OVP should have a trusted X.509 certificate with a chain of CA certificates.
3. OVP self-registers on the PMS portal providing partner ID, name, organization name (same as in certificate), partner type (`Credential_Partner`) etc. _(Using APIs, as OVP support on PMS Portal is available in the later version of MOSIP.)_
4. OVP uploads all certificates.
5. OVP selects the policy group and policy.
6. OVP maps policy to `auth` [credential type](https://github.com/mosip/id-repository/tree/release-1.2.0/id-repository/credential-service).
7. OVP adds biometric extractors for the policy.

## MOSIP Partner Program

The MOSIP Partner Programme (MPP) was initiated to help stakeholders connect with MOSIP, and become part of an ecosystem invested in building foundational digital ID systems that are trustworthy, secure, efficient, and interoperable while being customized to specific needs.

Refer [MPP document](https://mosip.io/program_partner/MOSIP-Partner-Programme-Revised.pdf) for further details.

## PMS module

Refer to [Partner Management Services](./).
