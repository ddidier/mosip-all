# Postgres Upgrade

## Overview
* Used to upgrade the schemas and required data from one MOSIP version to another.
* It can upgrade one version at a time.
* In case, you want to upgrade multiple versions, the same needs to be updated sequentially in upgrade.csv.

## Install

### Docker
* Make sure docker is installed and the user has the necessary privileges to run docker commands.
*  Set below mentioned ENV variables in the system:
   * DB_SERVERIP
   * DB_PORT
   * SU_USER
   * SU_USER_PWD
   * SYS_ADMIN_USER
   * SYS_ADMIN_PWD
*  Update the `upgrade.csv` with sequential upgrade details.
*  Run Upgrade using below command:
```
docker run -itd --name upgrade-db \
       -p $5432:5432  \
       -e DB_SERVERIP=$DB_SERVERIP \
       -e DB_PORT=$DB_PORT \
       -e SU_USER=$SU_USER \
       -e SU_USER_PWD=$SU_USER_PWD \
       -e SYS_ADMIN_USER=$SYS_ADMIN_USER \
       -e SYS_ADMIN_PWD=$SYS_ADMIN_PWD \
       -v ./upgrade.csv:/home/mosip/repos/db_upgrade_scripts/upgrade.csv \
       mosipid/postgres-upgrade:<tag>
```

### K8 cluster
To install in a k8 cluster using defined helm charts refer to the infra [install scripts](https://github.com/mosip/mosip-infra/tree/develop/deployment/v3/external/postgres#db-upgrade).
