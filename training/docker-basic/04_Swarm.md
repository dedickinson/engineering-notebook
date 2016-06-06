# Docker Swarm

[Swarm](https://docs.docker.com/swarm/overview/) provides you with a clustered Docker environment. It's an easy-to-use
approach that works with the Docker client (e.g. the `docker` command). What's more, you can easily get a Swarm
on your laptop.

## Roll up the sleeves

Setting up Docker Swarm across a number of VMs is made easy with Docker Machine. The process is:

1. Generate a Swarm discovery token
1. Create the Swarm master
1. Create 1..n Swarm agents

Note: the script below is [Li | U]nix-based - Windows users need to make minor modifications.

    # Step 1. Create a Swarm token by starting
    docker-machine start default
    eval $(docker-machine env default)
    DOCKER_DISCOVERY_TOKEN=$(docker run --rm swarm create)
    echo $DOCKER_DISCOVERY_TOKEN
    docker-machine stop default

    # Step 2: Create the Swarm master:
    docker-machine create -d virtualbox --swarm --swarm-image "swarm:latest" --swarm-master --swarm-discovery token://$DOCKER_DISCOVERY_TOKEN swarm-master

    # This next bit uses the master as our Docker client's environment
    eval $(docker-machine env --swarm swarm-master)

    # Step 3: Create 1..n Swarm agents
    docker-machine create -d virtualbox --swarm --swarm-image "swarm:latest" --swarm-addr=$(docker-machine ip swarm-master):3376 --swarm-discovery token://$DOCKER_DISCOVERY_TOKEN swarm-agent-01
    docker-machine create -d virtualbox --swarm --swarm-image "swarm:latest" --swarm-addr=$(docker-machine ip swarm-master):3376 --swarm-discovery token://$DOCKER_DISCOVERY_TOKEN swarm-agent-02


Once you're swarming, take a look at the environment:

* `docker-machine ls` should list three running machines and indicate that they're part of the swarm:

````
    NAME             ACTIVE      DRIVER       STATE     URL                         SWARM                   DOCKER    ERRORS
    default          -           virtualbox   Stopped                                                       Unknown   
    swarm-agent-01   -           virtualbox   Running   tcp://192.168.99.101:2376   swarm-master            v1.11.2   
    swarm-agent-02   -           virtualbox   Running   tcp://192.168.99.102:2376   swarm-master            v1.11.2   
    swarm-master     * (swarm)   virtualbox   Running   tcp://192.168.99.100:2376   swarm-master (master)   v1.11.2   
````

* `docker info` will reflect that you're connecting to a Swarm that has 3 nodes.

````
    Containers: 4
     Running: 4
     Paused: 0
     Stopped: 0
    Images: 3
    Server Version: swarm/1.2.3
    Role: primary
    Strategy: spread
    Filters: health, port, containerslots, dependency, affinity, constraint
    Nodes: 3
     swarm-agent-01: 192.168.99.101:2376
      └ ID: WU6R:IYSH:XLIS:JEQ2:26MR:CO6F:LFQD:XYSC:3PSZ:2VX2:EZU5:GRR5
      └ Status: Healthy
      └ Containers: 1
      └ Reserved CPUs: 0 / 1
      └ Reserved Memory: 0 B / 1.021 GiB
      └ Labels: executiondriver=, kernelversion=4.4.12-boot2docker, operatingsystem=Boot2Docker 1.11.2 (TCL 7.1); HEAD : a6645c3 - Wed Jun  1 22:59:51 UTC 2016, provider=virtualbox, storagedriver=aufs
      └ UpdatedAt: 2016-06-06T02:15:30Z
      └ ServerVersion: 1.11.2
     swarm-agent-02: 192.168.99.102:2376
      └ ID: DRAH:2HI4:QSLX:I7B4:RHID:7VIL:PGLI:AKL7:XWFA:VW24:3XXA:ZM4L
      └ Status: Healthy
      └ Containers: 1
      └ Reserved CPUs: 0 / 1
      └ Reserved Memory: 0 B / 1.021 GiB
      └ Labels: executiondriver=, kernelversion=4.4.12-boot2docker, operatingsystem=Boot2Docker 1.11.2 (TCL 7.1); HEAD : a6645c3 - Wed Jun  1 22:59:51 UTC 2016, provider=virtualbox, storagedriver=aufs
      └ UpdatedAt: 2016-06-06T02:15:21Z
      └ ServerVersion: 1.11.2
     swarm-master: 192.168.99.100:2376
      └ ID: 67OK:5GKY:KKVJ:O6GQ:VJSU:A3V4:GW37:EKOF:T7VG:R4XW:CZEE:XED7
      └ Status: Healthy
      └ Containers: 2
      └ Reserved CPUs: 0 / 1
      └ Reserved Memory: 0 B / 1.021 GiB
      └ Labels: executiondriver=, kernelversion=4.4.12-boot2docker, operatingsystem=Boot2Docker 1.11.2 (TCL 7.1); HEAD : a6645c3 - Wed Jun  1 22:59:51 UTC 2016, provider=virtualbox, storagedriver=aufs
      └ UpdatedAt: 2016-06-06T02:15:46Z
      └ ServerVersion: 1.11.2
    Plugins:
     Volume:
     Network:
    Kernel Version: 4.4.12-boot2docker
    Operating System: linux
    Architecture: amd64
    CPUs: 3
    Total Memory: 3.063 GiB
    Name: 9cd9302fd1c2
    Docker Root Dir:
    Debug mode (client): false
    Debug mode (server): false
````

### Use the Swarm

We won't get too complex here so just start up 3 nginx-based webservers:

    docker run -d --name webserver-01 -p 8001:80 nginx
    docker run -d --name webserver-02 -p 8002:80 nginx
    docker run -d --name webserver-03 -p 8003:80 nginx

When they've all deployed, check out the details provided by `docker ps` - you'll see your servers are spread across the
swarm.

````
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                                  NAMES
726b90915ca3        nginx               "nginx -g 'daemon off"   12 minutes ago      Up 12 minutes       443/tcp, 192.168.99.102:8003->80/tcp   swarm-agent-02/webserver-03
9b0032bb216c        nginx               "nginx -g 'daemon off"   13 minutes ago      Up 13 minutes       443/tcp, 192.168.99.101:8002->80/tcp   swarm-agent-01/webserver-02
78565290ab46        nginx               "nginx -g 'daemon off"   13 minutes ago      Up 13 minutes       443/tcp, 192.168.99.102:8001->80/tcp   swarm-agent-02/webserver-01
````

## Read

* [Docker clusters from the ground up: front-end reverse proxy](https://developer.atlassian.com/blog/2016/01/docker-cluster-reverse-proxy-1/)
* [Kubernetes](http://kubernetes.io/)
* [Apache Mesos](http://mesos.apache.org/)
