FROM demo_ci/base_epel

RUN  yum update -y && \
     yum install -y qpid-cpp-server python-qpid-qmf python-qpid \
     yum clean all

EXPOSE 5672

ENTRYPOINT ["qpidd", "-t", "--auth=no"]
