FROM jenkins:1.625.1

ENV JENKINS_HOME /var/jenkins_home
ENV CI jenkins

USER jenkins
COPY executors.groovy /usr/share/jenkins/ref/init.groovy.d/executors.groovy
COPY seed.groovy /usr/share/jenkins/ref/init.groovy.d/seed.groovy

COPY plugins.txt /plugins.txt
RUN /usr/local/bin/plugins.sh /plugins.txt
