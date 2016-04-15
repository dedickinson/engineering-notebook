FROM demo_ci/base_epel

MAINTAINER duncan@dickinson.name

ADD https://repos.fedorapeople.org/repos/pulp/pulp/rhel-pulp.repo /etc/yum.repos.d/

RUN yum install -y python-qpid python-qpid-qmf pulp-docker-plugins \
    pulp-python-plugins findutils nmap-ncat qpid-tools && \
    yum groupinstall -y pulp-server-qpid pulp-admin && \
    yum clean all

# A volume will be mounted here, clobbering the base structure. We keep a copy
# so it can be copied back over top of that volume.
RUN cp -a /var/lib/pulp /var/local/var_lib_pulp
RUN cp -a /etc/pulp /var/local/etc_pulp
RUN cp -a /etc/pki/pulp /var/local/etc_pki_pulp

ADD setup.sh /setup.sh
ADD server.conf /var/local/etc_pulp/server.conf
RUN chgrp apache /var/local/etc_pulp/server.conf
