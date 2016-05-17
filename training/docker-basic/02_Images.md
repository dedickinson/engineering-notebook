# Docker Images

In the last session we looked at starting up containers (centos7 & nginx) provided by [Docker Hub](https://hub.docker.com/). In this session we'll look further into setting up a [Dockerfile](https://docs.docker.com/engine/reference/builder/) that defines an image from which your containers are based.

## Roll up the sleeves

### Explore
1. Start Docker:
    1. `docker-machine start default`
    1. `eval $(docker-machine env)`
1. Local images
    1. When you start a container an image may be downloaded from Docker Hub or another [registry](https://docs.docker.com/registry/)
    1. You can also pull down an image without starting a container: `docker pull centos`
    1. Check the images you have locally: `docker images`
    1. Delete an image with `docker rmi <image_name>`
    1. Search for an image from the command line: `docker search centos` (look for the official one)
    1. Check out [syntax for the `docker images` command](https://docs.docker.com/v1.8/reference/commandline/images/)
1. Check out an existing Dockerfile
    1. Docker files are just text documents that Docker turns into images. This means you can version control them.
    1. Check out the [Centos dockerfiles](https://hub.docker.com/_/centos/) and you'll see that you can start a container based on different versions of an image:
        1. `docker run --rm centos` - the very latest Centos (the 'latest' version is implied)
        1. `docker run --rm centos:7` - specifies the latest Centos 7
        1. `docker run --rm centos:6.7` - a more specific version (this is the best option as you "pin" the version)
        1. You can inspect a locally available image using `docker inspect centos`
        1. List all local Centos-y images: `docker images |grep centos`
    1. Check out the [Centos 7.2.1511 Dockerfile](https://github.com/CentOS/sig-cloud-instance-images/blob/a3c59bd4e98a7f9c063d993955c8ec19c5b1ceff/docker/Dockerfile) - you can build this yourself *but* need a 'tar.xz' file - we'll try something easier.

### Implement

Creating an image is easily done through a Dockerfile and using the `docker build` command. When called, this
command [sends the Dockerfile and files in its context](https://docs.docker.com/v1.8/reference/builder/#usage)
(usually the same local directory) to the Docker service.

In this example I've created a [Dockerfile](dockerfiles/webserver/Dockerfile) and a basic
[`index.html`](dockerfiles/webserver/index.html) - create a new local directory, cd into it and copy these two files into this
directory. Done? Good! Now we give it a spin:

1. `docker build --tag=webserver .` - builds the image
1. `docker history webserver` - displays the history of an image
1. `docker run -d --name pyserv -p 8080:8000 webserver` - run up a container
1. `docker top pyserv` - shows you the process running in the container
1. `docker logs --follow pyserv` - watch the container's log (there isn't anything for this one)
1. Check out the website:
    1. `docker-machine ip` to get the host IP
    1. `docker port pyserv` to get the container port
1. Control the container:
    1. `docker stop pyserv` - stop the container
    1. `docker start pyserv` - start the container
    1. `docker rm -f pyserv` - stop and delete the container

### Homework
The webserver image pre-bakes static content into the image. Flex your skills and
[create a mount point](https://docs.docker.com/v1.8/reference/builder/#volume) that
you can then use to map a local volume your web server's directory (`/www`).

## Read

_Remember that Docker's only about 3-years old and things are moving fast. Some articles may not be current._

* [Docker Image Management](https://docs.docker.com/engine/userguide/eng-image/image_management/)
* [Are Docker containers really secure?](https://opensource.com/business/14/7/docker-security-selinux)
* [Best practices for writing Dockerfiles](https://docs.docker.com/engine/userguide/eng-image/dockerfile_best-practices/)
* [.dockerignore files](https://docs.docker.com/engine/reference/builder/#dockerignore-file)
* [Official Python image](https://hub.docker.com/_/python/)
