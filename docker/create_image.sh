#!/bin/bash
docker login
docker build -t rubenrr/webapp08 -f docker/Dockerfile .
docker push rubenrr/webapp08
