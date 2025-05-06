#!/bin/sh
# Installs Softhsm for IDA
## Usage: ./install_ida.sh [kubeconfig]

if [ $# -ge 1 ] ; then
  export KUBECONFIG=$1
fi

NS=ida
STORAGE_CLASS=gp2
CHART_VERSION=1.2.0

echo Create namespaces
kubectl create ns $NS 

echo Istio label 
kubectl label ns $NS istio-injection=enabled --overwrite
helm repo update

echo Installing Softhsm for IDA 
helm -n $NS install softhsm mosip/softhsm --set persistence.storageClass=$STORAGE_CLASS -f values.yaml --version $CHART_VERSION
