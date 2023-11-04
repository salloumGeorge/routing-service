#!/bin/sh

PROFILE=$1

set -e
cd "$(dirname "$0")"


docker-compose --project-name=routing-service down --remove-orphans
docker-compose rm

if [ -z "$PROFILE" ];
then
  docker-compose --project-name=routing-service up -d
else
  echo "Starting up profile $PROFILE"
    docker-compose --project-name=routing-service up -d $PROFILE up -d
fi
