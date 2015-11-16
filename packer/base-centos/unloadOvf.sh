#!/bin/bash

. ./env.sh

if [ $(VBoxManage list vms|grep "$BOX_NAME"|wc -l) == 0 ]
    then
    echo "$BOX_NAME is not registered with VirtualBox"
else
    echo Attempting to shut down $BOX_NAME
fi

if [ $(VBoxManage list runningvms|grep "$BOX_NAME"|wc -l) == 1 ]
    then
    echo "VM is running - stopping now"
    VBoxManage controlvm $BOX_NAME poweroff
    echo "Will sleep for 5s just to let VirtualBox tidy stuff up"
    sleep 5s
else
    echo "VM ($BOX_NAME) is not currently running"
fi

echo "Unregistering $BOX_NAME"
VBoxManage unregistervm --delete $BOX_NAME
