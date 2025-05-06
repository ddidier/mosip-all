# Softhsm

## Install
```sh
sh install_kernel.sh
sh install_ida.sh
```
## Defaults
* Replication factor is 1.  Multiple replication factors are not supported on AWS at the moment 'cause AWS EBS does not support `ReadWriteMany`.
* Keys are created in the mounted PV which gets mounted at `/softhsm/tokens` inside the container.
* Random PIN generated if not specified. Set `securityPIN` in `values.yaml`.
