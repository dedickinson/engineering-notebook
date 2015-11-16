#!/bin/bash

BUILDS=$1

. ./env.sh

set -e

if [ "$BUILDS" == "" ]
    then
    echo "Please specify a builder: "
    packer inspect centos-7.json -machine-readable|grep template-builder|cut -d',' -f4
    exit 1
fi

echo Building: $BOX_NAME
echo Builders: $BUILDS
echo Build file: $BUILD_FILE

if [ $(VBoxManage list vms|grep "$BOX_NAME"|wc -l) == 1 ]
    then
    echo "VirtualBox has a registered VM named $BOX_NAME - please remove this before continuing."
    exit 1
fi

packer validate -var 'vm_name'="$BOX_NAME" -only=$BUILDS $BUILD_FILE

packer build \
    -only=$BUILDS \
    -var 'ssh_key="$(<~/.ssh/vagrant.pub)"' \
    -var 'vm_name'="$BOX_NAME" \
    centos-7.json

#vagrant box add output-vagrant-box/$BOX_NAME.box --name $BOX_NAME-vagrant --force
