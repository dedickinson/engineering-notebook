#!/bin/bash

# clean yum data
sudo yum -y clean all

#Tidy up unused storage
sudo swapoff -a
sudo mkswap /dev/mapper/vg_vagrantcentos-lv_swap
sudo dd if=/dev/zero of=/boot/EMPTY bs=1M
sudo rm -f /boot/EMPTY
sudo dd if=/dev/zero of=/EMPTY bs=1M
sudo rm -f /EMPTY
