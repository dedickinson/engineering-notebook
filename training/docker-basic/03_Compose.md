# Docker Compose

We've learnt how to create an image and the run containers based on the image but trying to get an application
that consists of multiple containers is tricky. [Docker Compose](https://docs.docker.com/compose/overview/)
provides a simple approach to deploying a multi-container app such as a PHP + MySQL web application. When

Key features of Compose:

1. Define which containers you need and their configuration
1. Determine startup sequence and easily start/stop the application
1. Take advantage of [easy networking](https://docs.docker.com/compose/networking/) facilities between the containers.

## Roll up the sleeves

We'll start off by running the [Docker Compose WordPress example](https://docs.docker.com/compose/wordpress/)

1. Start Docker:
    1. `docker-machine start default`
    1. `eval $(docker-machine env)`
1. Create a directory named `wordpress` and add a new file named `docker-compose.yaml`
    1. You'll find a version of this in the `compose` directory
    1. Copy the contents in `compose/wordpress/docker-compose.yaml` to your new file
        1. Note that my copy comments out the volume setting
    1. Note that the `yaml` extension signals that we're using [YAML syntax](http://yaml.org/)
1. Change into your `wordpress` directory and:
    1. Validate your configuration with `docker-compose config`
    1. Start it all up with `docker-compose up -d`
        1. You'll see that the required images are downloaded and the application started
    1. Check that the containers are running: `docker ps`
        1. Then compare the output with `docker-compose ps`
    1. Check the logs with `docker-compose logs`
1. Take a look at your WordPress site:
    1. Determine the IP address: `docker-machine ip`
    1. Determine the port: `docker port wordpress_wordpress_1`
        1. Where did `wordpress_wordpress_1` come from? Try `docker-compose ps`
1. Shut down and clear away everything with `docker-compose down`
    1. Note that `docker-compose stop` and `docker-compose start` are used if you actually want to keep the environment.

When you `down` the system it all gets destroyed, including the data. The next section uses a volume in the compose
configuration but be warned that this data will be lost if you `down` the system - at least the data is kept if
you get a container failure as it's stored in a volume and not locally.

1. Create a new directory named `wordpress_vol` and add a new file named `docker-compose.yaml`
    1. You'll find a version of this in the `compose` directory - copy the compose file contents to your new file
1. Start it all up with `docker-compose up -d` and then check out the volume info:
    1. `docker volume ls` - list all volumes
    1. `docker volume ls|grep wordpressvol_datavolume` - for our specific volume
    1. `docker volume inspect wordpressvol_datavolume` - for details about the volume
1. Go to your Wordpress site and set it up
1. Now run `docker-compose stop` followed by `docker-compose start` - the database data is still there
1. Let's get harsh:
    1. `docker-compose kill db` will kill off the database service
    1. `docker-compose ps` and it should be dead (also try your browser)
    1. `docker-compose start db` will start up the db service again
    1. `docker-compose ps` and check your browser
1. Shut it all down: `docker-compose down`

## Read

* [Docker Volumes and Networks with Compose](https://www.linux.com/learn/docker-volumes-and-networks-compose)
