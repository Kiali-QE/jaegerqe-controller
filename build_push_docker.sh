#!/bin/bash

# docker organization
DOCKER_ORG='jkandasa' 

# tag version
TAG='1.0.3'

# compile java project
mvn clean package -Dmaven.test.skip=true

# build docker image
docker build -t ${DOCKER_ORG}/jaegerqe-controller:${TAG} .

# push new image to docker hub
docker push ${DOCKER_ORG}/jaegerqe-controller:${TAG}
