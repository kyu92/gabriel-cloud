# Docker image for Gabriel - ManagerServiceProvider
# VERSION 0.0.1
# Author: Kyu92

FROM openjdk:8

MAINTAINER kyu <a928495870@qq.com>

ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

RUN ["mkdir", "-p", "/manager-service-provider"]

WORKDIR /manager-service-provider
VOLUME /manager-service-provider

COPY *.jar /manager-service-provider
COPY *.yml /manager-service-provider/config
COPY *.properties /manager-service-provider/config

RUN ["chmod", "+x", "/manager-service-provider/boot.sh"]

CMD ["sh", "boot.sh"]