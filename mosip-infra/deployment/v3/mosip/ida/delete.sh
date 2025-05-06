#!/bin/sh
# Uninstalls all IDA helm charts 
NS=ida
while true; do
    read -p "Are you sure you want to delete ALL IDA helm charts?(Y/n) " yn
    if [ $yn = "Y" ]
      then
        helm -n $NS delete ida-keygen
        helm -n $NS delete ida-auth
        helm -n $NS delete ida-internal
        helm -n $NS delete ida-otp
        break
      else
        break
    fi
done
