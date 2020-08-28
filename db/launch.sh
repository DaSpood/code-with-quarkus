#!/bin/sh
docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 --name todoDB -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=todoDB -p 5432:5432 postgres:10.5