#!/usr/bin/env bash

git pull
npm update
npm update -g grunt-cli
grunt build
