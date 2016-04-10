FROM dedickinson/base-java

MAINTAINER duncan@dickinson.name

ENV APP_VERSION 1.2.7
ENV APP_DIST kafka-manager-$APP_VERSION.tar.gz
ENV APP_PORT 9000
ENV APP_CONFIG conf/application.conf
ENV ZK_HOSTS zookeeper:2181

USER root

ADD $APP_DIST $APP_HOME/
RUN chown -R app.app $APP_HOME

USER $APP_USER

RUN ln -s $APP_HOME/kafka-manager-$APP_VERSION kafka-manager

WORKDIR $APP_HOME/kafka-manager

EXPOSE $APP_PORT

CMD ["bin/kafka-manager","-Dconfig.file=conf/application.conf"]
