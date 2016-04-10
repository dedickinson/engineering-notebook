
Create a new docker host: `docker-machine create --driver=virtualbox support`

Setup terminal: `eval "$(docker-machine env support)"`

## Start it up

Note: This is only used once the environment has been prepared

    docker-compose up -d

## Preparing the support environment

    docker-compose --file docker-compose-registry.yml up -d
    ./load_base_images.sh

### Start the environment

This starts the main components of the support environment:

    docker-compose up -d

### General tips

* Handy image build and push: `./build_image.sh zookeeper`
* Stop all running containers: `docker stop $(docker ps -a -q)`
* Delete all images: `docker rmi $(docker images -q)`
    * Delete dangling images: `docker rmi $(docker images -f "dangling=true" -q)`
* Push to the local repo: `docker push localhost:5000/<image>`
* Query the local repo:
    * From the host VM: `curl http://localhost:5000/v2/_catalog`
    * From your PC: `curl http://192.168.99.100:5000/v2/_catalog` (IP address may be different)
* Loading an image from DockerHub - see `pull_image.sh`

As I use CentOS images, SELinux is part of the mix so here are some things I'm tracking:

* http://www.projectatomic.io/blog/2015/06/using-volumes-with-docker-can-cause-problems-with-selinux/
