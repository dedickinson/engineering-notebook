# See http://activemq.apache.org/getting-started.html
FROM java:openjdk-8u91-jre

RUN adduser --system --no-create-home activemq && \
    mkdir /activemq

WORKDIR /activemq

RUN wget http://mirror.aarnet.edu.au/pub/apache/activemq/5.13.3/apache-activemq-5.13.3-bin.tar.gz && \
    tar xvzf /activemq/apache-activemq-5.13.3-bin.tar.gz --strip-components 1 && \
    rm /activemq/apache-activemq-5.13.3-bin.tar.gz

RUN chown -R activemq /activemq

# Web console:
EXPOSE 8161

# Listen port
EXPOSE 61616

USER activemq

WORKDIR /activemq/bin

CMD ["./activemq", "console"]

# To build: docker build --tag=activemq .
# To run: docker run -ti --rm -p 8161 -p 61616 activemq

# Username/password: admin/admin
