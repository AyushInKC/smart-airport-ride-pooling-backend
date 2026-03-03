#!/bin/bash

echo "🛑 Stopping SmartAirportRidePooling services..."

POSTGRES_CONTAINER="airport_pooling"
REDIS_CONTAINER="airport-redis"

# Stop Postgres
if [ "$(docker ps -q -f name=$POSTGRES_CONTAINER)" ]; then
    echo "🛑 Stopping Postgres..."
    docker stop $POSTGRES_CONTAINER
else
    echo "ℹ️ Postgres is not running."
fi

# Stop Redis
if [ "$(docker ps -q -f name=$REDIS_CONTAINER)" ]; then
    echo "🛑 Stopping Redis..."
    docker stop $REDIS_CONTAINER
else
    echo "ℹ️ Redis is not running."
fi

echo "✅ All services stopped."