#!/bin/bash

echo "🧹 Cleaning SmartAirportRidePooling environment..."

POSTGRES_CONTAINER="airport_pooling"
REDIS_CONTAINER="airport-redis"
POSTGRES_VOLUME="airport_pg_data"

# -------------------------------
# Stop Containers (if running)
# -------------------------------
docker stop $POSTGRES_CONTAINER 2>/dev/null
docker stop $REDIS_CONTAINER 2>/dev/null

# -------------------------------
# Remove Containers
# -------------------------------
docker rm $POSTGRES_CONTAINER 2>/dev/null
docker rm $REDIS_CONTAINER 2>/dev/null

# -------------------------------
# Remove Volume (⚠️ deletes DB data)
# -------------------------------
docker volume rm $POSTGRES_VOLUME 2>/dev/null

echo "✅ Cleanup completed!"
echo "⚠️ All containers and database data removed."