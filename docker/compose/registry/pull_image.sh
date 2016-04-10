#!/bin/bash

eval "$(docker-machine env support)"

source=$1

echo Pulling: $source

dest=localhost:5000/${source#*\/}

echo Pushing: $dest

docker pull $source
docker tag $source $dest
docker push $dest
