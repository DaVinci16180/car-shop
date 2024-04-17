#!/bin/bash

docker run --name pg_master -e POSTGRES_PASSWORD=postgres -v pg_master-data=/var/lib/postgresql/data -p 127.0.0.1:3000:5432/tcp -d postgres
docker run --name pg_replica1 -e POSTGRES_PASSWORD=postgres -v pg_replica1-data=/var/lib/postgresql/data -d postgres
docker run --name pg_replica2 -e POSTGRES_PASSWORD=postgres -v pg_replica2-data=/var/lib/postgresql/data -d postgres

docker cp "./schema.sql" "pg_master:/tmp/schema.sql"
docker exec -i pg_master psql -U postgres -d postgres -f /tmp/schema.sql
docker exec -i pg_master rm /tmp/schema.sql

docker cp "./schema.sql" "pg_replica1:/tmp/schema.sql"
docker exec -i pg_replica1 psql -U postgres -d postgres -f /tmp/schema.sql
docker exec -i pg_replica1 rm /tmp/schema.sql

docker cp "./schema.sql" "pg_replica2:/tmp/schema.sql"
docker exec -i pg_replica2 psql -U postgres -d postgres -f /tmp/schema.sql
docker exec -i pg_replica2 rm /tmp/schema.sql
