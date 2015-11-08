#!/bin/bash

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
