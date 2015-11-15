#!/bin/bash

set -e

VERSION=$(<VERSION)
BOX_NAME=centos-7-base-$VERSION
BUILDS=$1

echo Building: $BOX_NAME
echo Builders: $BUILDS

packer validate -var 'vm_name'="$BOX_NAME" -only=$BUILDS centos-7.json

packer build \
    -only=$BUILDS \
    -var 'ssh_key="$(<~/.ssh/vagrant.pub)"' \
    -var 'vm_name'="$BOX_NAME" \
    centos-7.json

#vagrant box add output-vagrant-box/$BOX_NAME.box --name $BOX_NAME-vagrant --force
