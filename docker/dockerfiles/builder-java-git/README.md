Used to checkout a repository and build it. Requires that building is a single-line command.

`./builer.sh` usage:

    ./builer.sh <git repo url> <branch/tag> <command>

Example usage:

    mkdir repo

    docker run -ti --rm -v $PWD/repo:/opt/app/repo dedickinson/builder-java-git https://github.com/yahoo/kafka-manager.git 1.2.7 "./sbt clean dist"

To poke around the image without the image's entrypoint running:

    docker run -ti --rm -v $PWD/repo:/opt/app/repo --entrypoint="/bin/bash" dedickinson/builder-java-git
