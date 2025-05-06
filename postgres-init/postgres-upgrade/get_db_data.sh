#!/usr/bin/env bash

while IFS= read -r line; do
  REPO_NAME=$line
  if [ -z $line ]; then
    continue;
  fi
  if [ $REPO_NAME == "mosip-compliance-toolkit" ]
  then
  branch=v1.3.2
  git clone -b $branch https://github.com/mosip/$REPO_NAME.git ./repos/$REPO_NAME
  elif [ $REPO_NAME == "esignet" ]
  then
  branch=v1.3.0
  git clone -b $branch https://github.com/mosip/$REPO_NAME.git ./repos/$REPO_NAME
  elif [ $REPO_NAME == "commons" ]
  then
  branch=v1.2.0.1
  git clone -b $branch https://github.com/mosip/$REPO_NAME.git ./repos/$REPO_NAME
  elif [ $REPO_NAME == "id-authentication" ]
  then
  branch=v1.2.0.1
  git clone -b $branch https://github.com/mosip/$REPO_NAME.git ./repos/$REPO_NAME
  elif [ $REPO_NAME == "keymanager" ]
  then
  branch=v1.2.0.1
  git clone -b $branch https://github.com/mosip/$REPO_NAME.git ./repos/$REPO_NAME
  elif [ $REPO_NAME == "audit-manager" ]
  then
  branch=v1.2.0.1
  git clone -b $branch https://github.com/mosip/$REPO_NAME.git ./repos/$REPO_NAME
  elif [ $REPO_NAME == "admin-services" ]
  then
  branch=v1.2.0.1
  git clone -b $branch https://github.com/mosip/$REPO_NAME.git ./repos/$REPO_NAME
  elif [ $REPO_NAME == "id-repository" ]
  then
  branch=v1.2.0.1
  git clone -b $branch https://github.com/mosip/$REPO_NAME.git ./repos/$REPO_NAME
  elif [ $REPO_NAME == "partner-management-services" ]
  then
  branch=v1.2.0.1
  git clone -b $branch https://github.com/mosip/$REPO_NAME.git ./repos/$REPO_NAME
  elif [ $REPO_NAME == "pre-registration" ]
  then
  branch=v1.2.0.1
  git clone -b $branch https://github.com/mosip/$REPO_NAME.git ./repos/$REPO_NAME
  elif [ $REPO_NAME == "registration" ]
  then
  branch=v1.2.0.1
  git clone -b $branch https://github.com/mosip/$REPO_NAME.git ./repos/$REPO_NAME
  elif [ $REPO_NAME == "resident-services" ]
  then
  branch=v1.2.0.1
  git clone -b $branch https://github.com/mosip/$REPO_NAME.git ./repos/$REPO_NAME
  else
  git clone -b $1 https://github.com/mosip/$REPO_NAME.git ./repos/$REPO_NAME
  fi
  done < ./repo-list.txt
