#!/usr/bin/env bash

port4=$(lsof -t -i:4444)
if [ ! -z "$port4" ]; then
   kill $port4
fi

port5=$(lsof -t -i:4445)
if [ ! -z "$port5" ]; then
   kill $port5
fi

port6=$(lsof -t -i:4446)
if [ ! -z "$port6" ]; then
   kill $port6
fi

echo "Restart all the servers"

java -cp gson-2.6.2.jar:hamcrest-core-1.3.jar:junit-4.12.jar:classout/ com.team6.sjtu.Server 4444 true & java -cp gson-2.6.2.jar:hamcrest-core-1.3.jar:junit-4.12.jar:classout/ com.team6.sjtu.Server 4445 false & java -cp gson-2.6.2.jar:hamcrest-core-1.3.jar:junit-4.12.jar:classout/ com.team6.sjtu.Server 4446 false &
