FROM dedickinson/base-java

MAINTAINER duncan@dickinson.name

USER $APP_USER

ENV APP_VERSION 2.10-0.8.2.2
ENV APP_DIST kafka_$APP_VERSION.tgz
ENV APP_PORT 9092
ENV APP_LOGS /var/opt/app/logs
ENV APP_BROKER_ID 747
ENV APP_ZK_PORT 2181
ENV APP_ZK_SERVER zookeeper

ADD $APP_DIST $APP_HOME/

USER root
RUN chown -R app.app $APP_HOME

USER $APP_USER
RUN ln -s kafka_$APP_VERSION kafka

RUN mkdir -p $APP_LOGS

WORKDIR $APP_HOME/kafka

VOLUME $APP_LOGS

EXPOSE $APP_PORT

RUN { \
    echo "broker.id=$APP_BROKER_ID"; \
    echo "log.dirs=$APP_LOGS"; \
    echo "port=$APP_PORT"; \
    echo "zookeeper.connect=$APP_ZK_SERVER:$APP_ZK_PORT"; \
	} > config/kafka.cfg

CMD ["bin/kafka-server-start.sh", "config/kafka.cfg"]
