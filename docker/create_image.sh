#!/bin/bash
docker login
docker build -t rubenrr/webapp08:1.0 -f docker/Dockerfile .
docker push rubenrr/webapp08:1.0
