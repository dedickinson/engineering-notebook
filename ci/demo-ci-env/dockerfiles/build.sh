#!/usr/bin/env bash
eval $(docker-machine env)

builds=(basedist jenkins support)
basedist=(base base_epel)
jenkins=(jenkins jenkins_agent jenkins_agent_jdk8)
support=()

# Exit the loops if any build command fails
set -e
for build in ${builds[@]}
do
    eval arr='(${'$build'[@]})'
    for image in ${arr[@]}
    do
        echo $build/$image
        docker build $@ -t demo_ci/$image:latest $build/$image/
    done
done

