#!/usr/bin/env bash

# Exit the loops if any build command fails
set -e

for NAME in pulp pulp_apache pulp_qpid pulp_crane pulp_mongodb
do
    pushd $NAME
    docker build -t demo_ci/$NAME:latest .
    popd
done
