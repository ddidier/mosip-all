# Packet Manager

## Overview

Packet Manager performs the following functions:

1. Reads/writes registration packets from/to the [Object Store](https://docs.mosip.io/1.2.0/id-lifecycle-management/supporting-components/persistence/object-store).
2. Performs in-memory encryption and decryption of packets.
3. Performs security checks, checksum, file validations, ID object validations, etc. on the registration packet.
4. Provides packet information to other services via APIs. In case of multiple packets associated with an ID, pulls information from packets based on configured priority. (See [`packetmanager.default.priority`](https://github.com/mosip/mosip-config/blob/develop3-v3/application-default.properties)).

Packet Manager runs as a service and is also available as a library.

![](../../../.gitbook/assets/packet-manager.png)

The relationship of the Packet Manager with other services is explained here.&#x20;

{% hint style="info" %}
_**NOTE**: The numbers do not signify a sequence of operations or control flow. Arrows indicate data flow_
{% endhint %}

1. [Resident Services](../../identity-management/resident-services/) uses the Packet Manager library to create packets.
2. [Registration Processor](../../identity-issuance/registration-processor/) reads packet data using the Packet Manager service.
3. Packets are stored and retrieved from the [Object Store](https://docs.mosip.io/1.2.0/id-lifecycle-management/supporting-components/persistence/object-store).
4. Audit logs.
5. Encryption and decryption of packet.
6. [Registration Client](../../identity-issuance/registration-client/) uses the Packet Manager library to create packets.

## Packet structure

Refer to [Registration Packet Structure](registration-packet-structure.md).

## API

Refer to [API Documentation](https://mosip.github.io/documentation/1.2.0/1.2.0.html).

## Source code

[Github repo](https://github.com/mosip/packet-manager/tree/release-1.2.0).
