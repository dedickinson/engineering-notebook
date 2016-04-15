FROM centos:7.2.1511

MAINTAINER duncan@dickinson.name

RUN yum -y update && \
    yum -y clean all

#ENV APP_HOME=/opt/app
#ENV APP_DATA=/var/opt/app
#ENV APP_USER=app
#
#RUN groupadd --gid 1000 app
#RUN useradd --system --uid 1000 -g app --create-home --home-dir=$APP_HOME $APP_USER
#
#RUN mkdir -p $APP_DATA && \
#    chown -R $APP_USER.$APP_USER $APP_DATA
#
#USER $APP_USER
#
#WORKDIR $APP_HOME
