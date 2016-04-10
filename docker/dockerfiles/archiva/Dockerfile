FROM dedickinson/base-java

MAINTAINER duncan@dickinson.name

USER $APP_USER

ENV APP_VERSION=2.2.0
ENV APP_DIST=apache-archiva-$APP_VERSION-bin.tar.gz
ENV APP_PORT=8080

ADD $APP_DIST $APP_HOME/

USER root
RUN chown -R app.app $APP_HOME

USER $APP_USER
RUN ln -s apache-archiva-$APP_VERSION archiva

RUN mkdir $APP_DATA/logs && \
    mkdir $APP_DATA/data && \
    mkdir $APP_DATA/temp && \
    mkdir $APP_DATA/conf

RUN cp -r $APP_HOME/archiva/conf/* $APP_DATA/conf/

EXPOSE $APP_PORT

VOLUME $APP_DATA
#VOLUME $APP_DATA/logs
#VOLUME $APP_DATA/data
#VOLUME $APP_DATA/temp
#VOLUME $APP_DATA/conf

WORKDIR $APP_HOME/archiva

CMD ["./bin/archiva", "console"]
