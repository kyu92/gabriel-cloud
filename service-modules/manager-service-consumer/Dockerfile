# Docker image for Gabriel - ManagerServiceConsumer
# VERSION 0.0.1
# Author: Kyu92

FROM openjdk:8

MAINTAINER kyu <a928495870@qq.com>

ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

RUN ["mkdir", "-p", "/manager-service-consumer"]

WORKDIR /manager-service-consumer
VOLUME /manager-service-consumer

COPY boot.* /manager-service-consumer
COPY *.jar /manager-service-consumer
COPY *.yml /manager-service-consumer/config
COPY *.properties /manager-service-consumer/config

RUN ["chmod", "+x", "/manager-service-consumer/boot.sh"]

CMD ["sh", "boot.sh"]