FROM docker-registry.rewe-digital.com/rewe-debian-openjdk:11.latest
MAINTAINER Big Data Sparks <dev-team-sparks@rewe-digital.com>

ARG JAR_FILE=podscaling-0.0.1-SNAPSHOT.jar

ARG SERVICE_USER=service

ENV SYSTEM_MEMORY 1024
ENV SERVICE_PORT 3149
ENV JAVA_DISABLE_DEBUG=true
ENV JAVA_DISABLE_JMX=true

COPY ./build/libs/${JAR_FILE} /opt/rewe/app.jar

USER ${SERVICE_USER}
