FROM dedickinson/base

MAINTAINER duncan@dickinson.name

USER root

RUN yum -y install java-1.8.0-openjdk-devel && \
    yum -y clean packages

USER $APP_USER
