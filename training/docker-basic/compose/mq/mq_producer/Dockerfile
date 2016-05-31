FROM groovy

WORKDIR /groovy/scripts
COPY *.groovy /groovy/scripts/

ENV GROOVY_HOME /groovy
ENV PATH /groovy/bin:$PATH

RUN ["groovy", "-Dgrape.root=/groovy/.grape", "-Dgroovy.grape.report.downloads=true", "producer.groovy", "install"]

CMD ["groovy", "-Dgrape.root=/groovy/.grape", "producer.groovy"]

#To build: docker build --tag=mq_producer .
#To run: docker run --rm -ti mq_producer
