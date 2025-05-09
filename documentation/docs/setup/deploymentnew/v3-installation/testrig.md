# Testrig

## Authdemo Service

* The Authdemo service is utilized to execute the IDA APIs that are employed by API-testrig and DSLrig.
* The purpose of the Authdemo Service is to showcase the functionality of authentication.
* It can be considered as a simplified iteration of an authentication service, serving as a mock or prototype for the purpose of testing.

```
cd $INFRA_ROOT/deployment/v3/testrig/authdemo
./install.sh
```

* When prompted, input the NFS host, it's pem-key, ssh login user of NFS server.
* Install script will create the NFS directories i.e., `/srv/nfs/mosip/packetcreator-authdemo-authcerts` to store the certificates generated by Authdemo service.

These certificates will be used by API-testrig, orchestrator and packetcreator.

## API-testrig

API-testRig tests the working of APIs of the MOSIP modules.

MOSIP’s successful deployment can be verified by comparing the results of API-testrig with the testrig benchmark.

```
cd $INFRA_ROOT/deployment/v3/testrig/apitestrig
./install.sh
```

* When prompted, input the hour of the day to execute the API-testrig.
* Daily API-testrig cron job will be executed at the very opted hour of the day.
* The reports will move to the object store ( i.e., s3/minio) under `automationtests` bucket.

## Packetcreator

Packetcreator will create packets for DSL orchestrator.

**Note:** It is recommended to deploy the packetcreator on a separate server/ cluster from where the other DSL orchestrators can access this service.

```
cd $INFRA_ROOT/deployment/v3/testrig/packetcreator
./install.sh
```

* When prompted, input the NFS host, it's pem-key, ssh login user of NFS server.
* Install script will create two NFS directories i.e., `/srv/nfs/mosip/packetcreator_data`, `/srv/nfs/mosip/packetcreator-authdemo-authcerts`.
* `Packetcreator_data` contains biometric data which is used to create packets.
* The default `packetcreator_data` can be accessible from [here](https://github.com/mosip/mosip-automation-tests/tree/develop/mosip-packet-creator/src/main/resources/dockersupport/centralized/mountvolume/profile_resource).
* Copy the `packetcreator_data` from the link mentioned above to the NFS directory `/srv/nfs/mosip/packetcreator_data`.
* Ensure to use the same NFS host and path i.e., `/srv/nfs/mosip/packetcreator-authdemo-authcerts` for Authdemo and packetcreator service.
* When prompted, input the kubernetes ingress type (i.e., Ingress/Istio) and DNS as required if you are using the Ingress-nginx.

## DSLrig/ DSLOrchestrator

* DSLrig will test end-to-end functional flows involving multiple MOSIP modules.
* The Orchestrator utilizes the Packet Creator to generate packets according to the defined specifications. It then communicates with the Authdemo Service making REST API calls to perform authentication-related actions or retrieve the necessary information.

```
cd $INFRA_ROOT/deployment/v3/testrig/dslrig
./install.sh
```

* When prompted, input the NFS host, it's pem-key, ssh login user of NFS server.
* Install script will create NFS directories, i.e., `/srv/nfs/mosip/dsl-scenarios/sandbox.xyz.net` to store the DSL scenario sheet.
* The Default template for DSL scenario sheet can be accessible from [here](https://github.com/mosip/mosip-automation-tests/tree/develop/mosip-acceptance-tests/ivv-orchestrator/src/main/resources/local/scenarios).
* Copy the scenario csv from the above link to the NFS directory `/srv/nfs/mosip/dsl-scenarios/sandbox.xyz.net`. Make sure to rename the csv files by replacing `env` with your domain ex: `sandbox.xyz.net`.
* To run the dslorchestrator for sanity only, update the `dslorchestrator` configmap `TESTLEVEL` key to `sanity`.
* The reports will move to object store (i.e., s3/minio) under `dslreports` bucket.
