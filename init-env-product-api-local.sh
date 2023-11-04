#!/bin/sh

PROFILE=$1

set -e
cd "$(dirname "$0")"


if [ -z "$PROFILE" ];
then
  docker-compose -f products-api-local.yml --project-name=products-api up -d
else
  echo "Starting up profile $PROFILE"
    docker-compose -f products-api-local.yml --project-name=products-api up -d $PROFILE up -d
fi
