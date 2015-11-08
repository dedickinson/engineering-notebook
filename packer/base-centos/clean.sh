#!/bin/bash

rm -rf output-*
rm *.box
rm -rf .vagrant
rm Vagrantfile

VBoxManage unregistervm centos-7-base --delete

echo "I don't delete the packer_cache directory."
