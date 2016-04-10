#!/bin/bash

./gradlew clean run

rm -rf publish
mkdir publish

cp -r book/output/site/* publish/

# Create the sitemap and robots file
cd publish
find . -name "*.html"|sed -E -e 's/^[\.]*/http:\/\/www.groovy-tutorial.org/'>sitemap.txt 
echo Sitemap: http://www.groovy-tutorial.org/sitemap.txt>robots.txt
cd -

#Copy over the analytics js
cp src/main/resources/name/dickinson/duncan/book/js/analytics.js publish/resources/js/analytics.js

#Copy over the search page - one day I'll fix this.
cp src/main/resources/name/dickinson/duncan/book/template/search.html publish/search.html

rsync --delete --progress -ave ssh publish/ groovy-tutorial-site:/home/public/