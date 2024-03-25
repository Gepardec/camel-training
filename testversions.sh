#!/bin/sh

VERSIONS="19 20 21 22"

for v in $VERSIONS; do
    echo Test version $v
    export INT_VERSION="3.${v}.0"
    perl -wpi -e's|<camel.version>.+</camel.version>|<camel.version>$ENV{INT_VERSION}</camel.version>|' solutions/pom.xml \
         || { echo replace $v failed; exit 1; }
    ./testall.sh || { echo Camel version $v failed; exit 2; }
    echo Version $v OK
done 
