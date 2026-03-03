#!/bin/bash

echo "🚀 Setting up SmartAirportRidePooling dependencies..."

# -------------------------------
# Check Docker
# -------------------------------
if ! command -v docker &> /dev/null
then
    echo "❌ Docker not installed. Please install Docker first."
    exit 1
fi

echo "✅ Docker found"

# -------------------------------
# POSTGRES SETUP
# -------------------------------
POSTGRES_CONTAINER="airport_pooling"

if [ "$(docker ps -aq -f name=$POSTGRES_CONTAINER)" ]; then
    echo "✅ Postgres container exists. Starting..."
    docker start $POSTGRES_CONTAINER
else
    echo "📦 Creating Postgres container..."
    docker run -d \
      --name airport_pooling \
      -e POSTGRES_USER=postgres \
      -e POSTGRES_PASSWORD=postgres \
      -e POSTGRES_DB=airport_pooling \
      -p 5432:5432 \
      -v airport_pg_data:/var/lib/postgresql/data \
      postgres
fi

# -------------------------------
# REDIS SETUP
# -------------------------------
REDIS_CONTAINER="airport-redis"

if [ "$(docker ps -aq -f name=$REDIS_CONTAINER)" ]; then
    echo "✅ Redis container exists. Starting..."
    docker start $REDIS_CONTAINER
else
    echo "📦 Creating Redis container..."
    docker run -d \
      --name airport-redis \
      -p 6379:6379 \
      redis:7
fi

echo "✅ Setup completed successfully!"