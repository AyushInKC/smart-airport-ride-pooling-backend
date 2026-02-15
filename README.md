# Smart Airport Ride Pooling Backend

## Overview
A Spring Boot backend for grouping airport passengers into shared cabs, optimizing routes, handling constraints, and supporting high concurrency.

## Tech Stack
- Java 17
- Spring Boot 3
- PostgreSQL
- Spring Data JPA
- Redis
- Flyway
- Swagger/OpenAPI
- Maven
- Docker
- JUnit

## Features
- Group passengers into shared cabs
- Seat/luggage/detour constraints
- Real-time cancellations
- High concurrency (10,000 users, 100 RPS)
- Optimized matching & dynamic pricing

## Setup

### Prerequisites
- Java 17
- Maven
- Docker & Docker Compose

### Run with Docker Compose
```
docker-compose up --build
```

### Run Locally
1. Start PostgreSQL and Redis (see docker-compose.yml for config)
2. Update DB credentials in `src/main/resources/application.yaml` if needed
3. Run migrations:
```
mvn flyway:migrate
```
4. Start app:
```
mvn spring-boot:run
```

## API Docs
Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Sample cURL Commands

**Request a Ride:**
```
curl -X POST http://localhost:8080/api/rides/request -H "Content-Type: application/json" -d '{"passengerId":1,"pickupLat":28.555,"pickupLng":77.101,"dropLat":28.600,"dropLng":77.200,"luggageCount":1,"detourToleranceKm":5}'
```

**Cancel a Ride:**
```
curl -X POST http://localhost:8080/api/rides/1/cancel
```

**Get Ride Details:**
```
curl http://localhost:8080/api/rides/1
```

**Get Active Rides:**
```
curl http://localhost:8080/api/rides/active
```

**Get Available Cabs:**
```
curl http://localhost:8080/api/cabs/available
```

## Project Structure
- controller: REST endpoints
- service: business logic
- repository: JPA interfaces
- entity: DB models
- dto: request/response objects
- matching: ride matching engine
- pricing: pricing strategies
- concurrency: Redis locks, thread pools
- config: app/config beans

## License
MIT
