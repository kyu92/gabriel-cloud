# Docker image for Gabriel - UserServiceConsumer
# VERSION 0.0.1
# Author: Kyu92

FROM openjdk:8

MAINTAINER kyu <a928495870@qq.com>

ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

RUN ["mkdir", "-p", "/user-service-consumer"]

WORKDIR /user-service-consumer
VOLUME /user-service-consumer

COPY boot.* /user-service-consumer
COPY *.jar /user-service-consumer
COPY *.yml /user-service-consumer/config
COPY *.properties /user-service-consumer/config

RUN ["chmod", "+x", "/user-service-consumer/boot.sh"]

CMD ["sh", "boot.sh"]