#!/bin/bash

set -e

cd ~/uploads/etc

# Setup the sudoers config
visudo -c -f sudoers
sudo EDITOR="cp sudoers" visudo

sudo cp /etc/ssh/sshd_config /etc/ssh/sshd_config.orig
sudo cp ssh/sshd_config /etc/ssh/sshd_config

sudo cp /etc/pam.d/su /etc/pam.d/su.orig
sudo cp pam.d/su /etc/pam.d/su

cd ~/
rm -rf uploads
