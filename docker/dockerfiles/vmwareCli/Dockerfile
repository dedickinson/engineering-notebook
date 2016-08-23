FROM centos:7

# Doco: http://pubs.vmware.com/vsphere-60/topic/com.vmware.vcli.getstart.doc/cli_install.4.4.html#991697
# Get the archive file from https://my.vmware.com/group/vmware/details?downloadGroup=VCLI600&productId=491

RUN yum -y install e2fsprogs e2fsprogs-devel libuuid-devel openssl-devel perl-devel glibc.i686 zlib.i686 perl-XML-LibXML libncurses.so.5 perl-Crypt-SSLeay libwww-perl-5 IO-Compress-2 which libssl-dev

RUN mkdir /vmware

COPY VMware-vSphere-CLI-6.0.0-2503617.x86_64.tar.gz /vmware/

WORKDIR /vmware

RUN tar zxvf VMware-vSphere-CLI-6.0.0-2503617.x86_64.tar.gz

WORKDIR /vmware/vmware-vsphere-cli-distrib

#RUN yes | PAGER=cat ./vmware-install.pl default
