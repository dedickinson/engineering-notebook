#!/bin/bash

eval "$(docker-machine env support)"

echo $1

docker build --tag=localhost:5000/$1 $1/
docker push localhost:5000/$1
