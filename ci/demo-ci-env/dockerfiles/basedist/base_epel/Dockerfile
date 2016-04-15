FROM demo_ci/base

MAINTAINER duncan@dickinson.name

USER root

RUN yum install -y epel-release && yum clean all

USER $APP_USER
