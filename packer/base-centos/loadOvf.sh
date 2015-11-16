#!/bin/bash

. ./env.sh

if [ "$1" == "" ]; then
    echo "Please provide the name of the output folder containing centos-7-base-$VERSION.ovf:"
    ls -1d output-*
    exit 1
fi

OVF_FILE=$1/centos-7-base-$VERSION.ovf

if [ -f $OVF_FILE ]
    then
    echo "Importing $OVF_FILE"
    VBoxManage import $OVF_FILE
    echo "Starting $BOX_NAME"
    VBoxManage startvm $BOX_NAME
else
    echo "$OVF_FILE not found"
fi
