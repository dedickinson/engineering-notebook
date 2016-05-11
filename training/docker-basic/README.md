# Getting started with Docker

Learn some basics:

- https://docs.docker.com/mac/step_two/

## Roll up the sleeves

1. Install [Docker Toolbox](https://www.docker.com/products/docker-toolbox)
    - Install the various docker tools, a VM and [VirtualBox](https://www.virtualbox.org/)
    - A [native app](https://beta.docker.com/) is on the way
2. Start Kitematic
    - This starts the VM in VirtualBox
    - Select the Centos image and start it. This draws the image down from [Docker Hub](https://hub.docker.com/) if the image isn't available locally.
    - Execute a command in the running commander
    - Stop the container then start it (it's fast)
    - Set the Kitematic config to stop the VM on exit
    - Exit Kitematic
3. Using the command line (OS X):
    1. `docker-machine ls`
    1. `docker-machine start default` (starts a docker host VM)
    1. `eval $(docker-machine env)` (sets up environment variables)
    1. `docker ps` (lists any running containers)
    1. `docker run --rm -ti centos:7 /bin/bash` (starts a CentOS container and puts you into its shell - the container is deleted when you exit)
        1. Look around
        1. `exit`
    1. `docker ps` (check that the container has stopped)
4. Start a web server - [nginx](http://nginx.org/en/)
    1. We'll use the [nginx image](https://hub.docker.com/_/nginx/)
    1. `docker run -d --name webserver -P nginx` (starts the web server as a daemon)
    1. `docker ps` - you'll see the webserver container
    1. `docker-machine ip` (gets the host IP)
    1. `docker port webserver`
    1. Check out your webserver: e.g. http://192.168.99.100:32769/
    1. `docker stop webserver`
    1. `docker ps -a` (shows all containers - including stopped ones)
    1. `docker rm webserver` (deletes the container)
5. Web server variations:
    1. Find out about the image: `docker inspect nginx`
    1. Set a port mapping: `docker run -d --name webserver -p 8080:80 nginx`
    1. Set a volume: `docker run -d --name webserver -p 8080:80 -v /some/content:/usr/share/nginx/html:ro nginx`
