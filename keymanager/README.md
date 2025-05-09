[![Maven Package upon a push](https://github.com/mosip/keymanager/actions/workflows/push_trigger.yml/badge.svg?branch=release-1.2.0)](https://github.com/mosip/keymanager/actions/workflows/push_trigger.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?branch=1.2.0-rc2&project=mosip_keymanager&metric=alert_status)](https://sonarcloud.io/dashboard?branch=release-1.2.0&id=mosip_keymanager)

# Key Manager 

## Overview
Refer [Key Manager](https://docs.mosip.io/1.2.0/modules/keymanager)

## Databases
Refer to [SQL scripts](db_scripts).

## Build & run (for developers)
The project requires JDK 1.11. 
1. Build and install:
    ```
    $ cd kernel
    $ mvn install -DskipTests=true -Dmaven.javadoc.skip=true -Dgpg.skip=true
    ```
1. Build Docker for a service:
    ```
    $ cd <service folder>
    $ docker build -f Dockerfile
    ```

## Deploy
To deploy on Kubernetes cluster using Dockers refer to [Sandbox Deployment](https://docs.mosip.io/1.2.0/deployment/sandbox-deployment).

## Test
Automated functional tests available in [Functional Tests repo](https://github.com/mosip/mosip-functional-tests).

## APIs
API documentation is available [here](https://mosip.github.io/documentation/).

## License
This project is licensed under the terms of [Mozilla Public License 2.0](LICENSE).



