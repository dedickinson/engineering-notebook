FROM centos:centos7
MAINTAINER Pulp Team <pulp-list@redhat.com>

ADD https://repos.fedorapeople.org/repos/pulp/pulp/rhel-pulp.repo /etc/yum.repos.d/

RUN  yum update -y && \
     yum groupinstall -y \
         pulp-admin && \
     yum install -y \
         pulp-docker-admin-extensions pulp-python-admin-extensions && \
     yum clean all

# config and uploads dir
VOLUME ["/.pulp /run/pulp_uploads"]

# make sure this gets the correct permission
RUN mkdir /root/.pulp && chmod 700 /root/.pulp

ADD admin.conf /root/.pulp/admin.conf
