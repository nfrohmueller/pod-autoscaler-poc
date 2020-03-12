#!/usr/bin/env bash

./gradlew clean assemble
docker build -t test/podscaling:0.0.1 .
kubectl apply -f metrics-adapter-deployment
kubectl apply -f service-deployment
