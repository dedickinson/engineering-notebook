FROM demo_ci/base_epel

MAINTAINER duncan@dickinson.name

RUN  yum update -y && yum install -y mongodb-server && \
     yum clean all

RUN  mkdir -p /var/lib/mongodb && \
     touch /var/lib/mongodb/.keep && \
     chown -R mongodb:mongodb /var/lib/mongodb

VOLUME ["/var/lib/mongodb"]

USER mongodb

ADD mongodb.conf /etc/mongodb.conf

EXPOSE 27017
CMD ["/usr/bin/mongod", "--quiet", "--config", "/etc/mongodb.conf", "run"]
