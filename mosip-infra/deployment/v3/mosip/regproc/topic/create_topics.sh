#!/bin/sh
#
read -p "Enter IAM username: " iam_user

# This username is hardcoded in sql scripts
DB_PWD=$(kubectl get secret --namespace postgres db-common-secrets -o jsonpath={.data.db-dbuser-password} | base64 --decode)
DB_HOST=$(kubectl get cm global -o jsonpath={.data.mosip-api-internal-host})
DB_PORT=5432

echo Creating topics
cd lib
python3 create_topics.py $DB_HOST $DB_PWD $iam_user ../topics.xlsx
