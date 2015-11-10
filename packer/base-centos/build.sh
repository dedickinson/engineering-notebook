#!/bin/bash

set -e

packer validate centos-7.json
packer build centos-7.json
vagrant box add output-vagrant-box/centos-7-base.box --name centos-7-base --force
