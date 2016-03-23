#!/usr/bin/env bash

if [ -d classout ]; then
   rm -r classout
fi

mkdir classout

export CLASSPATH=gson-2.6.2.jar:hamcrest-core-1.3.jar:junit-4.12.jar:log4j-api-2.3.jar:log4j-core-2.3.jar:$CLASSPATH

javac -d classout @sources.txt

cp log4j2.xml classout/
