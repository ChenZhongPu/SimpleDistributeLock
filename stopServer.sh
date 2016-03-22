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

echo "Stopped all the servers"