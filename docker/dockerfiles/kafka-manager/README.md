
To build the distro:

    docker run -ti --rm -v $PWD/repo:/opt/app/repo dedickinson/builder-java-git https://github.com/yahoo/kafka-manager.git 1.2.7 "./sbt clean dist"

This build takes a very, very, very long time.

Then upload to <https://bintray.com/dedickinson/generic/kafka-manager/view>
