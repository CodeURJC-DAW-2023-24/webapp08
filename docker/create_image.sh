#!/bin/bash
if [ $# -eq 0 ]; then
    echo "Por favor, proporciona un nombre de versión como argumento."
    exit 1
else
    VERSION=$1
fi
docker login
docker build -t rubenrr/webapp08:$VERSION -f docker/Dockerfile .
docker push rubenrr/webapp08:$VERSION
