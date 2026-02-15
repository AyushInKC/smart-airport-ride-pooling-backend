# Smart Airport Ride Pooling Backend

Smart Airport Ride Pooling is a production-oriented Spring Boot backend system designed to group airport passengers into shared cabs using intelligent geo-based matching, dynamic pricing, and strong concurrency control mechanisms.

It demonstrates distributed system design principles including Redis-based locking, optimistic locking, transactional safety, and scalable architecture using Dockerized infrastructure.

## Tech Stack
- Java 17
- Spring Boot 3
- Spring Data JPA (Hibernate)
- PostgreSQL (Dockerized)
- Redis (Dockerized)
- Flyway (Database Migrations)
- Swagger / OpenAPI
- Maven
- Docker & Docker Compose
- JUnit

## Core Features
- Geo-based cab matching using bounding box filtering
- Shared ride support with detour tolerance constraints
- Seat and luggage validation
- Dynamic pricing with surge multiplier
- Real-time ride cancellation handling
- Distributed locking using Redis
- Optimistic locking using @Version for seat consistency
- Flyway-managed database schema
- Swagger API documentation
- High concurrency safe design

## Concurrency Design

This system prevents double booking using a two-layer protection mechanism:

1. Redis Distributed Lock  
   Before assigning a ride, a Redis key `ride:assign:{requestId}` is acquired.  
   This prevents parallel assignment of the same request across multiple threads or instances.

2. Optimistic Locking  
   The Cab entity uses a `@Version` field.  
   When multiple transactions attempt to update seat count:
   - Only one succeeds
   - Others fail safely
   - Seat count never becomes negative

## Result:
- No race conditions
- No overbooking
- Strong data consistency under concurrent requests

## Matching Logic

Matching is performed in three stages:
1. Geo-based filtering (latitude/longitude bounding box)
2. Eligibility filtering (available seats and luggage capacity)
3. Greedy scoring based on detour estimation

The matching engine is modular and extensible for future enhancements such as ML-based scoring or driver rating prioritization.

## Pricing Strategy

Pricing includes:
- Base fare
- Distance-based calculation
- Surge multiplier
- Ride-sharing discount

Implemented using the Strategy Pattern for flexibility and scalability.

## Database Design

Entities:
- Cab
- Passenger
- Ride
- RideRequest
- RidePassenger

All primary keys use BIGSERIAL.
Schema versioning is managed using Flyway migrations.

## Setup

Prerequisites
- Java 17
- Maven
- Docker & Docker Compose

Run with Docker Compose
docker-compose up --build


Run Locally

1. Start PostgreSQL and Redis using Docker:
docker run --name airport-postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=airport_pooling -p 5432:5432 -d postgres
docker run --name airport-redis -p 6379:6379 -d redis


2. Run Flyway migrations:
mvn flyway:migrate


3. Start the application:
mvn spring-boot:run


## Swagger Documentation

Swagger UI:
http://localhost:8080/swagger-ui.html

Sample cURL Commands

Request a Ride:
curl -X POST http://localhost:8080/api/rides/request
-H "Content-Type: application/json"
-d '{"passengerId":1,"pickupLat":28.555,"pickupLng":77.101,"dropLat":28.600,"dropLng":77.200,"luggageCount":1,"detourToleranceKm":5}'


Cancel a Ride:
curl -X POST http://localhost:8080/api/rides/1/cancel


Get Ride Details:
curl http://localhost:8080/api/rides/1


Get Active Rides:
curl http://localhost:8080/api/rides/active


Get Available Cabs:
curl http://localhost:8080/api/cabs/available


## Project Structure
- controller: REST API endpoints
- service: Business logic and transactional workflows
- repository: Spring Data JPA interfaces
- entity: Database models
- dto: Request and response objects
- matching: Ride matching engine implementation
- pricing: Pricing strategy implementations
- concurrency: Redis distributed lock utilities
- config: Application configuration beans

## Scalability Considerations
- Stateless service design
- Redis distributed locking for horizontal scaling
- Optimistic locking at database level
- Modular matching strategy
- Containerized infrastructure using Docker

## Future Improvements
- Replace bounding box filtering with Haversine distance
- Add retry mechanism for optimistic lock conflicts
- Add integration testing using Testcontainers
- Add driver rating-based scoring
- Add metrics and monitoring
