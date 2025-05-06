FROM postgres

ARG SOURCE
ARG COMMIT_HASH
ARG COMMIT_ID
ARG BUILD_TIME
LABEL source=${SOURCE}
LABEL commit_hash=${COMMIT_HASH}
LABEL commit_id=${COMMIT_ID}
LABEL build_time=${BUILD_TIME}
RUN apt-get update
RUN apt-get install -y python3 python3-pip python3-xlrd git

WORKDIR /home/mosip/repos/

ENV GIT_REPO_URL=

ENV GIT_BRANCH=

ENV MOSIP_DB_NAME=

ENV DB_SERVERIP=

ENV DB_PORT=5432

ENV SU_USER=postgres

ENV SU_USER_PWD=

ENV DEFAULT_DB_NAME=postgres

ENV DBUSER_PWD=

ENV DML_FLAG=

ADD postgres-init.sh postgres-init.sh

ENTRYPOINT ["./postgres-init.sh" ]
