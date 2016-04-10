FROM dedickinson/base-java

MAINTAINER duncan@dickinson.name

ENV APP_VERSION=2.4.5
ENV APP_DIST=groovy-binary-$APP_VERSION.tar.gz

USER root

ADD $APP_DIST $APP_HOME/
RUN chown -R app.app $APP_HOME

USER $APP_USER

RUN ln -s $APP_HOME/groovy-$APP_VERSION groovy

RUN echo "export PATH=$APP_HOME/groovy/bin:$PATH" >> $APP_HOME/.bashrc
