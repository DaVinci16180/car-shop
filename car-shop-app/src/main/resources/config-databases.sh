#!/bin/bash

docker exec -i pg_master psql -U postgres -d postgres -c "ALTER SYSTEM SET wal_level = logical;"
docker exec -i pg_replica1 psql -U postgres -d postgres -c "ALTER SYSTEM SET wal_level = logical;"
docker exec -i pg_replica2 psql -U postgres -d postgres -c "ALTER SYSTEM SET wal_level = logical;"

docker exec pg_master psql -U postgres -d postgres -c "DROP PUBLICATION IF EXISTS my_publication;"
docker exec pg_master psql -U postgres -d postgres -c "CREATE PUBLICATION my_publication FOR ALL TABLES;"

docker exec pg_replica1 psql -U postgres -d postgres -c "DROP SUBSCRIPTION IF EXISTS pg_replica1_subscription;"
docker exec pg_replica1 psql -U postgres -d postgres -c "CREATE SUBSCRIPTION pg_replica1_subscription CONNECTION 'host=localhost port=5432 user=postgres password=postgres dbname=car-shop' PUBLICATION my_publication;"

docker exec pg_replica2 psql -U postgres -d postgres -c "DROP SUBSCRIPTION IF EXISTS pg_replica2_subscription;"
docker exec pg_replica2 psql -U postgres -d postgres -c "CREATE SUBSCRIPTION pg_replica2_subscription CONNECTION 'host=localhost port=5432 user=postgres password=postgres dbname=postgres' PUBLICATION my_publication;"