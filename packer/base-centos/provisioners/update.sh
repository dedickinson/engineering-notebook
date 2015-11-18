#!/bin/bash

# Apply the most current updates
sudo yum -y update

sudo yum -y install \
    curl \
    deltarpm \
    gawk \
    firewalld \
    policycoreutils-python
