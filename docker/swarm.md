# Basic swarm w/docker-machine

## Creating
    docker-machine start default
    eval $(docker-machine env default)
    DOCKER_DISCOVERY_TOKEN=$(docker run --rm swarm create)
    echo $DOCKER_DISCOVERY_TOKEN

    docker-machine create -d virtualbox --swarm --swarm-image "swarm:latest" --swarm-master --swarm-discovery token://$DOCKER_DISCOVERY_TOKEN swarm-master

    eval $(docker-machine env --swarm swarm-master)
    docker info

    docker-machine create -d virtualbox --swarm --swarm-image "swarm:latest" --swarm-addr=$(docker-machine ip swarm-master):3376 --swarm-discovery token://$DOCKER_DISCOVERY_TOKEN swarm-agent-01

    docker-machine create -d virtualbox --swarm --swarm-image "swarm:latest" --swarm-addr=$(docker-machine ip swarm-master):3376 --swarm-discovery token://$DOCKER_DISCOVERY_TOKEN swarm-agent-02

    docker info

## Starting

    docker-machine start swarm-master
    eval $(docker-machine env --swarm swarm-master)
    docker-machine start swarm-agent-01
    docker-machine start swarm-agent-02
    docker info

## Stopping

    docker-machine stop swarm-agent-02
    docker-machine stop swarm-agent-01
    docker-machine stop swarm-master

## `swarm` commands

- List the swarm nodes: `docker run swarm list token://$DOCKER_DISCOVERY_TOKEN`
- Manage the swarm: `docker run swarm manage token://$DOCKER_DISCOVERY_TOKEN`
- Determine the discovery token: `docker inspect swarm-agent-master|grep token`

## Setting up Consul

[IN PROGRESS]

See:

- <https://docs.docker.com/swarm/install-manual/>
- <https://www.consul.io/intro/index.html>
- <https://docs.docker.com/swarm/scheduler/filter/>


    docker run -d -p 8500:8500 --name=consul progrium/consul -server -bootstrap

## References

- <https://blog.codeship.com/docker-machine-compose-and-swarm-how-they-work-together/>
