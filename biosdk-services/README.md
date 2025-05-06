[![Maven Package upon a push](https://github.com/mosip/biosdk-services/actions/workflows/push_trigger.yml/badge.svg?branch=release-1.2.0)](https://github.com/mosip/biosdk-services/actions/workflows/push_trigger.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?branch=release-1.2.0&project=mosip_biosdk-services&metric=alert_status)](https://sonarcloud.io/dashboard?branch=release-1.2.0&id=mosip_biosdk-services)

# Bio-SDK Service

## Overview
This service provides a mock implementation of Bio-SDK REST Service. It by default loads [Mock BIO SDK](https://github.com/mosip/mosip-mock-services/tree/master/mock-sdk) internally on the startup and exposes the endpoints to perform 1:N match, segmentation, extraction as per the [IBioAPI](https://github.com/mosip/commons/blob/master/kernel/kernel-biometrics-api/src/main/java/io/mosip/kernel/biometrics/spi/IBioApi.java).

To know more about Biometric SDK, refer [biometric-sdk](https://docs.mosip.io/1.2.0/biometrics/biometric-sdk).

### License
This project is licensed under the terms of [Mozilla Public License 2.0](LICENSE).
