# Docker image for Gabriel - BookServiceProvider
# VERSION 0.0.1
# Author: Kyu92

FROM openjdk:8

MAINTAINER kyu <a928495870@qq.com>

ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

RUN ["mkdir", "-p", "/book-service-provider"]

WORKDIR /book-service-provider
VOLUME /book-service-provider

COPY boot.* /book-service-provider
COPY *.jar /book-service-provider
COPY *.yml /book-service-provider/config
COPY *.properties /book-service-provider/config

RUN ["chmod", "+x", "/book-service-provider/boot.sh"]

CMD ["sh", "boot.sh"]