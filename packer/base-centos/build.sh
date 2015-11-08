#!/bin/bash

set -e

source ../../lib/download.sh

# Download all items required
#Not needed as Packer can do this
#if [ ! -e "iso/" ]; then
#    mkdir iso
#fi
#download "iso" "http://mirror.aarnet.edu.au/pub/centos/7/isos/x86_64/CentOS-7-x86_64-Minimal-1503-01.iso"

packer validate centos-7.json
packer build centos-7.json
vagrant box add output-vagrant-box/centos-7-base.box --name centos-7-base --force
