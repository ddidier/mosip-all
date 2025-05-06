#!/bin/sh
# Creates configmap and secrets for Email 
## Usage: ./install.sh [kubeconfig]

[ $# -lt 3 ] && { echo "Usage: ./email.sh <smtp_host> <username> <password> [kubeconfig]"; exit 1; }

if [ $# -ge 3 ] ; then
  export KUBECONFIG=$4
fi

NS=msg-gateways

echo Create namespace
kubectl create ns $NS 

echo Istio label 
kubectl label ns $NS istio-injection=enabled --overwrite

kubectl -n $NS create configmap email-gateway --from-literal=email-smtp-host=$1 --from-literal=email-smtp-username=$2 --dry-run=client  -o yaml | kubectl apply -f -
kubectl -n $NS create secret generic email-gateway --from-literal=email-smtp-secret="$3" --dry-run=client  -o yaml | kubectl apply -f -
