FROM dedickinson/basejava

MAINTAINER duncan@dickinson.name

ENV GROOVY_VERSION=2.4.5
ENV GROOVY_HOME=$HOME
ENV APP_DIST groovy.zip

USER root
RUN yum -y install unzip

USER $APP_USER

RUN env

COPY $APP_DIST ./dist.zip

RUN unzip groovy.zip -v -d $APP_HOME && \
    rm dist.zip

#RUN echo "export JAVA_HOME=$JAVA_HOME" >> /home/user/.bashrc
#RUN echo "export GROOVY_HOME=$GROOVY_HOME" >> /home/user/.bashrc
#RUN echo "export PATH=$GROOVY_HOME/bin:$JAVA_HOME/bin:$PATH" >> /home/user/.bashrc
