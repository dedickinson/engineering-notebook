#!/bin/bash

#See: http://stackoverflow.com/questions/25434139/vboxlinuxadditions-run-never-exits-with-0
#set -e

sudo yum -y install gcc kernel-devel bzip2

sudo mkdir /media/cdrom
sudo mount /dev/sr0 /media/cdrom
sudo /media/cdrom/VBoxLinuxAdditions.run --nox11
sudo umount /media/cdrom
sudo rmdir /media/cdrom
