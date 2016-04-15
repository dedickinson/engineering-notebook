FROM demo_ci/base_epel
MAINTAINER Pulp Team <pulp-list@redhat.com>

ADD https://repos.fedorapeople.org/repos/pulp/pulp/rhel-pulp.repo /etc/yum.repos.d/

RUN yum update -y && yum install -y python-crane httpd mod_wsgi && yum clean all

RUN ln -s /usr/share/crane/apache.conf /etc/httpd/conf.d/crane.conf

ENV APACHE_RUN_USER apache
ENV APACHE_RUN_GROUP apache

EXPOSE 80

ENTRYPOINT ["/usr/sbin/httpd"]
CMD ["-D", "FOREGROUND"]
