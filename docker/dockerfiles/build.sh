#!/bin/bash
# Downloads all archive files required for building

set -e

function download () {
    local dest
    local url
    local outfile

    dest=$1
    url=$2
    outfile=${url##*\/}

    if [ -e "$dest/$outfile" ]
        then
            echo File already exists: $dest/$outfile
        else
            echo Downloading $url to $dest/$outfile
            curl -s -L -o $dest/$outfile $url

            #Convert zips to tar.gz so Docker can extract them with ADD
            local ext=${outfile##*\.}
            if [ "$ext" == "zip" ]; then
                local tarfile=${outfile%\.zip}.tar.gz

                echo "Converting $outfile to $tarfile"

                unzip -d .tmp $dest/$outfile
                cd .tmp
                tar cvzf ../$dest/$tarfile *
                cd -
                rm -rf .tmp
            fi
    fi
}

download "archiva" "https://mirror.aarnet.edu.au/pub/apache/archiva/2.2.0/binaries/apache-archiva-2.2.0-bin.tar.gz" &
download "zookeeper" "https://mirror.aarnet.edu.au/pub/apache/zookeeper/zookeeper-3.4.6/zookeeper-3.4.6.tar.gz" &
download "kafka" "https://mirror.aarnet.edu.au/pub/apache/kafka/0.8.2.2/kafka_2.10-0.8.2.2.tgz" &
download "base-groovy" "http://central.maven.org/maven2/org/codehaus/groovy/groovy-binary/2.4.5/groovy-binary-2.4.5.zip" &
download "kafka-manager" "https://dl.bintray.com/dedickinson/generic/kafka-manager-1.2.7.tar.gz" &
wait

if [ "$1" == "" ];
    then
        docker build -t dedickinson/base base
        docker build -t dedickinson/base-java base-java
        docker build -t dedickinson/base-java-git base-java-git
        docker build -t dedickinson/base-groovy base-groovy
        docker build -t dedickinson/archiva archiva
        docker build -t dedickinson/zookeeper zookeeper
        docker build -t dedickinson/kafka kafka
        docker build -t dedickinson/kafka-manager kafka-manager
        #docker build -t dedickinson/jenkins-local jenkins-local
    else
        if [ -e "$1/" ];
            then
                echo "Building $1"
                docker build -t dedickinson/$1 $1
            else
                echo "Can only build subdirs ($1 not found)"
        fi
fi
