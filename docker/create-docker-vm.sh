#!/bin/bash

docker-machine create --driver virtualbox default \
    --virtualbox-cpu-count "1" \
    --virtualbox-memory "2048" \
    --virtualbox-disk-size "20000" \
    --virtualbox-no-share true

docker-machine stop default

#See: http://stackoverflow.com/questions/30040708/how-to-mount-local-volumes-in-docker-machine
VBoxManage sharedfolder add default --name docker-shared --hostpath ~/var --automount

docker-machine start default

docker-machine scp bootlocal.sh default:~/bootlocal.sh
docker-machine ssh default sudo mv /home/docker/bootlocal.sh /mnt/sda1/var/lib/boot2docker/bootlocal.sh
docker-machine ssh default sudo chmod ug+x /mnt/sda1/var/lib/boot2docker/bootlocal.sh
docker-machine ssh default sudo /mnt/sda1/var/lib/boot2docker/bootlocal.sh

docker-machine restart default

#sudo mkdir /docker-shared
#sudo cat /mnt/sda1/var/lib/boot2docker/bootlocal.sh
#mkdir -p /docker-shared
#mount -t vboxsf -o defaults,uid=`id -u docker`,gid=`id -g docker` docker-shared /docker-shared
