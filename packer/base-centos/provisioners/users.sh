#!/bin/bash

# Configure SSH access for Vagrant user
mkdir /home/vagrant/.ssh
chmod 700 /home/vagrant/.ssh
sudo chown -R vagrant:vagrant /home/vagrant/.ssh
