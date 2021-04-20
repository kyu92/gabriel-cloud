# Docker image for Gabriel - BookServiceConsumer
# VERSION 0.0.1
# Author: Kyu92

FROM openjdk:8

MAINTAINER kyu <a928495870@qq.com>

ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

RUN ["mkdir", "-p", "/book-service-consumer"]

WORKDIR /book-service-consumer
VOLUME /book-service-consumer

COPY boot.* /book-service-consumer
COPY *.jar /book-service-consumer
COPY *.yml /book-service-consumer/config
COPY *.properties /book-service-consumer/config

RUN ["chmod", "+x", "/book-service-consumer/boot.sh"]

CMD ["sh", "boot.sh"]