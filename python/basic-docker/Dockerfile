FROM centos:7

RUN yum install -y python python-setuptools
RUN easy_install pip
RUN mkdir -p /opt/app
WORKDIR /opt/app

RUN echo "print('hello, world')">/opt/app/hello.py

EXPOSE 8000

VOLUME ["/opt/app"]

#To build: docker build --tag python27 .
#To run:
#   docker run --rm -ti -v $(pwd):/opt/app python27 /bin/bash
#   docker run --rm -ti python27 /usr/bin/python2.7
#   docker run --rm -ti python27 /bin/bash
