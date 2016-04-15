#!/usr/bin/env bash

set +e
for dir in basic
do
    mkdir -p $dir/{BUILD,RPMS,SRPMS}
    docker run -v $dir:/home/rpmbuild  demo_ci/rpmbuild
done
