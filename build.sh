#!/usr/bin/env bash

if [ -d classout ]; then
   rm -r classout
fi

mkdir bbout

export CLASSPATH=gson-2.6.2.jar:hamcrest-core-1.3.jar:junit-4.12.jar:$CLASSPATH

javac -d classout @sources.txt
