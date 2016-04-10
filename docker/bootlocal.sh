mkdir -p /docker-shared
mount -t vboxsf -o defaults,uid=`id -u docker`,gid=`id -g docker` docker-shared /docker-shared
