#!/bin/bash

set -e

cd ~/uploads/etc

# Setup the sudoers config
sudo visudo -c -f sudoers
sudo EDITOR="cp sudoers" visudo
sudo rm /etc/sudoers.d/vagrant

sudo cp /etc/ssh/sshd_config /etc/ssh/sshd_config.orig
sudo cp ssh/sshd_config /etc/ssh/sshd_config

sudo cp /etc/pam.d/su /etc/pam.d/su.orig
sudo cp pam.d/su /etc/pam.d/su

sudo cp /etc/default/grub /etc/default/grub.orig
sudo cp default/grub /etc/default/grub
sudo grub2-mkconfig -o /boot/grub2/grub.cfg

cd ~/
rm -rf ~/uploads
