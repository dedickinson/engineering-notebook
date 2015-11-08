#!/bin/bash
sudo mkdir /home/vagrant/.ssh
sudo chown -R vagrant:vagrant /home/vagrant/.ssh
sudo chmod 700 /home/vagrant/.ssh
sudo chmod 0400 /home/vagrant/.ssh/*

sudo yum -y update
