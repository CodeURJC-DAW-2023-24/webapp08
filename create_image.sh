#!/bin/bash
docker login
docker build -t rubenrr/webapp08:1.0 .
docker push rubenrr/webapp08:1.0
