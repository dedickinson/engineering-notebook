FROM demo_ci/base

USER root

RUN yum -y install openssh-server && yum clean all

RUN useradd jenkins && echo "jenkins:jenkins" | chpasswd

RUN mkdir -p /var/run/sshd

COPY etc/ssh/* /etc/ssh/

EXPOSE 22

ENTRYPOINT ["/usr/sbin/sshd", "-D"]
