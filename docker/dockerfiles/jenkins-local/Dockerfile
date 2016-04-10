FROM jenkins:1.625.1

ENV JENKINS_HOME /var/jenkins_home
ENV CI jenkins

# Add any required packages etc
# USER root
# RUN apt-get update && apt-get install -y git

USER jenkins
COPY executors.groovy /usr/share/jenkins/ref/init.groovy.d/executors.groovy
COPY seed.groovy /usr/share/jenkins/ref/init.groovy.d/seed.groovy

COPY plugins.txt /plugins.txt
RUN /usr/local/bin/plugins.sh /plugins.txt

#CMD ["/var/jenkins_home/war/WEB-INF/jenkins-cli.jar", "-s", "http://localhost:8080/"]
