FROM java:openjdk-8u91-jdk

USER root

RUN adduser --system --no-create-home groovy && \
    mkdir /groovy

WORKDIR /groovy

RUN wget http://jcenter.bintray.com/org/codehaus/groovy/groovy-binary/2.4.5/groovy-binary-2.4.5.zip && \
    unzip groovy-binary-2.4.5.zip && \
    rm groovy-binary-2.4.5.zip && \
    mv groovy-2.4.5/* /groovy && \
    chown -R groovy /groovy

USER groovy

#ENTRYPOINT ["bin/groovy", "-Dgrape.root=.grape"]

# To build: docker build --tag=groovy .
