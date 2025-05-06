# Keycloak

## Overview
Keycloak serves as the administrative access control module in MOSIP, managing authentication and authorization across its microservices. Since MOSIP is built on a microservice architecture, it heavily relies on OAuth 2.0 tokens for access control. These tokens contain embedded role definitions that determine who can access specific microservice APIs ensuring secure and granular access control.

### Keycloak's Role in MOSIP
 * **Microservice Security** – Each MOSIP microservice validates incoming API requests using OAuth 2.0 tokens issued by Keycloak. If the token lacks the necessary role, access is denied. This mechanism applies universally, even between internal MOSIP modules, ensuring a zero-trust architecture.
 * **Data Access Control** – Most sensitive data in MOSIP is encrypted in the database, making APIs the only entry point for accessing information. Keycloak enforces strict authentication and authorization policies, preventing internal attacks and API misuse.
 * **Administrative Oversight** – Administrators can monitor and control access permissions centrally via Keycloak, providing visibility into who can access what within the system.
 * **User Repository Management** – Keycloak also functions as the internal identity store for critical MOSIP roles, including:
    * **Operators** – Handle citizen registration and data collection.
    * **Supervisors** – Oversee registration processes and ensure compliance.
    * **Adjudicators** – Review and resolve identity conflicts or duplicate registrations.

By enforcing strict access controls and acting as both an identity provider and an authorization enforcer Keycloak helps MOSIP maintain high security, scalability, and compliance while protecting against unauthorized access.



<!--
* Choice of Keycloak as IAM. 
* MOSIP compliant with any OAUTH 2.0 IAM.


## MOSIP theme

## Roles

## Installation

-->