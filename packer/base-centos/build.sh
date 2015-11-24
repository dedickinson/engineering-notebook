#!/bin/bash

BUILDS=$1

. ./env.sh

set -e

if [ "$SSH_PUBLIC_KEY_FILE" != "" ] && [ ! -f "$SSH_PUBLIC_KEY_FILE" ];
    then
    echo "The SSH public key ($SSH_PUBLIC_KEY_FILE) wasn't provided"
    exit 1
fi

if [ "$BUILD_FILE" != "" ] && [ ! -f "$BUILD_FILE" ]
    then
    echo "The build file ($BUILD_FILE) file couldn't be found"
    exit 1
fi

if [ "$BUILDS" == "" ];
    then
    echo "Please specify a builder from $BUILD_FILE: "
    packer inspect $BUILD_FILE -machine-readable|grep template-builder|cut -d',' -f4
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
    -var ssh_public_key_file=$SSH_PUBLIC_KEY_FILE \
    -var vm_name=$BOX_NAME \
    $BUILD_FILE
