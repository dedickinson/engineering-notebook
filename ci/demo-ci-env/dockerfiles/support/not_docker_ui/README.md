# Use not-docker-ui

URL: <https://github.com/crosbymichael/not-dockers-ui>

    docker run -ti --privileged --rm -v /var/run/docker.sock:/var/run/docker.sock demo_ci/not_docker_ui /bin/bash

    docker-machine ssh
    wget https://nodejs.org/dist/v4.4.2/node-v4.4.2-linux-x64.tar.xz
    git clone https://github.com/crosbymichael/not-dockers-ui.git
    cd not-dockers-ui
    npm install
    npm install -g grunt-cli
    # Make sure your Docker daemon is running
    grunt run     # Takes a while, will build the image locally as dockerui:latest and run it
    # Open your browser to `http://<dockerd host ip>:9000`




    docker run -ti --privileged --rm node /bin/bash
    cd /opt
    git clone https://github.com/crosbymichael/not-dockers-ui.git
    cd not-dockers-ui
    npm install
    npm install -g grunt-cli
