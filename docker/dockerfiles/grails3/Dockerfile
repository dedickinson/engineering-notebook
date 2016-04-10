#SEE: http://blog.codenvy.com/cooking-custom-build-environments/

FROM dedickinson/jdk8:v1
MAINTAINER Duncan Dickinson <duncan@dickinson.name>

RUN yum -y install unzip wget

ENV USER=grails
ENV GRAILS_DOWNLOAD=https://github.com/grails/grails-core/releases/download/v3.0.2/grails-3.0.2.zip
ENV GRAILS_HOME /home/$USER/grails
ENV PATH $GRAILS_HOME/bin:$PATH

RUN useradd $USER
WORKDIR /home/$USER

USER $USER
RUN wget -q --output-document=grails.zip $GRAILS_DOWNLOAD && unzip grails.zip && rm -rf grails.zip

RUN ln -s grails-3.0.2/ grails

ENV GRAILS_HOME /home/$USER/grails
ENV PATH $GRAILS_HOME/bin:$PATH

