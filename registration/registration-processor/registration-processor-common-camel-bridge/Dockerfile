FROM openjdk:11

#Uncomment below and Comment above line(i.e. FROM openjdk:8) for OS specific (e.g. Alpine OS ) docker base image
#FROM openjdk:8-jdk-alpine

# setting labels for extra properties needed for this Docker image, so that it can be used at run time with docker inspect
LABEL network=host
LABEL privileged=true
LABEL zone_env=dmz 

# can be passed during Docker build as build time environment for github branch to pickup configuration from.
ARG spring_config_label

# can be passed during Docker build as build time environment for spring profiles active
ARG active_profile

# can be passed during Docker build as build time environment for config server URL
ARG spring_config_url

# can be passed during Docker build as build time environment for zone
ARG zone

# can be passed during Docker build as build time environment for glowroot 
ARG is_glowroot

# can be passed during Docker build as build time environment for artifactory URL
ARG artifactory_url

# environment variable to pass active profile such as DEV, QA etc at docker runtime
ENV active_profile_env=${active_profile}

# environment variable to pass zone i.e dmz or secure at docker runtime
ENV zone_env=${zone}

# environment variable to pass github branch to pickup configuration from, at docker runtime
ENV spring_config_label_env=${spring_config_label}

# environment variable to pass spring configuration url, at docker runtime
ENV spring_config_url_env=${spring_config_url}

# environment variable to pass glowroot, at docker runtime
ENV is_glowroot_env=${is_glowroot}

# environment variable to pass artifactory url, at docker runtime
ENV artifactory_url_env=${artifactory_url}

# can be passed during Docker build as build time environment for github branch to pickup configuration from.
ARG container_user=mosip

# can be passed during Docker build as build time environment for github branch to pickup configuration from.
ARG container_user_group=mosip

# can be passed during Docker build as build time environment for github branch to pickup configuration from.
ARG container_user_uid=1001

# can be passed during Docker build as build time environment for github branch to pickup configuration from.
ARG container_user_gid=1001

RUN apt-get -y update \
&& apt-get install -y unzip sudo \
&& groupadd -g ${container_user_gid} ${container_user_group} \
&& useradd -u ${container_user_uid} -g ${container_user_group} -s /bin/sh -m ${container_user} \
&& adduser ${container_user} sudo

# set working directory for the user
WORKDIR /home/${container_user}

ENV work_dir=/home/${container_user}

ARG loader_path=${work_dir}/additional_jars/

RUN mkdir -p ${loader_path}

ENV loader_path_env=${loader_path}

# copying shaded jar into docker image
ADD ./target/*.jar ./target/
RUN find target/ -regextype posix-extended -regex '.*registration-processor-common-camel-bridge-[0-9\.]+[-A-Za-z0-9]*\.jar'  "-print0" | xargs "-0" cp -t ./ \
  && rm -rf ./target \
  && rm -rf original*.jar \
  && rm -rf *-sources.jar \
  && rm -rf *-javadoc.jar \
  && mv *.jar registration-processor-common-camel-bridge.jar

 # change permissions of file inside working dir
 RUN chown -R ${container_user}:${container_user} /home/${container_user}

 # select container user for all tasks
 USER ${container_user_uid}:${container_user_gid}

CMD if [ "$is_glowroot_env" = "present" ]; then \
    wget "${artifactory_url_env}"/artifactory/libs-release-local/io/mosip/testing/glowroot.zip ; \
    unzip glowroot.zip ; \
    rm -rf glowroot.zip ; \
    sed -i 's/<service_name>/registration-processor-common-camel-bridge/g' glowroot/glowroot.properties ; \
    java -jar -javaagent:glowroot/glowroot.jar -Dspring.cloud.config.label="${spring_config_label_env}" -Dspring.profiles.active="${active_profile_env}" -Dspring.cloud.config.uri="${spring_config_url_env}" -Dregistration.processor.zone="${zone_env}" registration-processor-common-camel-bridge.jar ; \
    else \
    java -jar -Dspring.cloud.config.label="${spring_config_label_env}" -Dspring.profiles.active="${active_profile_env}" -Dspring.cloud.config.uri="${spring_config_url_env}" -Dregistration.processor.zone="${zone_env}" registration-processor-common-camel-bridge.jar ; \
    fi


# running java jar
#CMD java -jar -Dspring.cloud.config.label=${spring_config_label_env} -Dspring.profiles.active=${active_profile_env} -Dspring.cloud.config.uri=${spring_config_url_env} -Dregistration.processor.zone=${zone_env} registration-processor-common-camel-bridge.jar
