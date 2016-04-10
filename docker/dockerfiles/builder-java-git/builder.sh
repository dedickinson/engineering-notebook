#!/bin/bash
set -e

git clone $1 repo
cd repo
git checkout $2
$3
