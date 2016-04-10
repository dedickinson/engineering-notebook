FROM dedickinson/base-java

MAINTAINER duncan@dickinson.name

USER $APP_USER

ENV APP_VERSION 3.4.6
ENV APP_DIST zookeeper-$APP_VERSION.tar.gz
ENV APP_PORT 2181

ADD $APP_DIST $APP_HOME/

USER root
RUN chown -R app.app $APP_HOME

USER $APP_USER
RUN ln -s zookeeper-$APP_VERSION zookeeper

EXPOSE $APP_PORT

VOLUME $APP_DATA

WORKDIR $APP_HOME/zookeeper

RUN { \
    echo "tickTime=2000"; \
    echo "dataDir=$APP_DATA"; \
    echo "clientPort=$APP_PORT"; \
	} > conf/zoo.cfg

CMD ["bin/zkServer.sh", "start-foreground", "conf/zoo.cfg"]
