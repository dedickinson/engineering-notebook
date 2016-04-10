FROM dedickinson/base

MAINTAINER duncan@dickinson.name

USER root

RUN curl https://bintray.com/sbt/rpm/rpm | tee /etc/yum.repos.d/bintray-sbt-rpm.repo

RUN yum -y install sbt

USER $APP_USER
