# Docker image for Gabriel - UserServiceProvider
# VERSION 0.0.1
# Author: Kyu92

FROM openjdk:8

MAINTAINER kyu <a928495870@qq.com>

ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

RUN ["mkdir", "-p", "/user-service-provider"]

WORKDIR /user-service-provider
VOLUME /user-service-provider

COPY boot.* /user-service-provider
COPY *.jar /user-service-provider
COPY *.yml /user-service-provider/config
COPY *.properties /user-service-provider/config

RUN ["chmod", "+x", "/user-service-provider/boot.sh"]

CMD ["sh", "boot.sh"]