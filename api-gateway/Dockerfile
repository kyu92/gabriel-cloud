# Docker image for Gabriel - Gateway
# VERSION 0.0.1
# Author: Kyu92
FROM openjdk:8

MAINTAINER kyu <a928495870@qq.com>

ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
ENV NACOS_ADDR=127.0.0.1:8848

RUN ["mkdir", "-p", "/gateway"]
#RUN /sbin/ip route|awk '/default/ { print $3,"\tdockerhost" }' >> /tmp/hosts
#RUN mv /tmp/hosts /etc/hosts

WORKDIR /gateway
VOLUME /gateway

COPY ./bin /gateway/bin
COPY ./config /gateway/config
COPY ./lib /gateway/lib
COPY ./README.md /gateway/README.md

RUN chmod +x /gateway/bin/boot.sh
WORKDIR /gateway/bin

CMD sh /gateway/bin/boot.sh