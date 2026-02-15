-- V1__init_schema.sql
-- Flyway migration for Smart Airport Ride Pooling

CREATE TABLE IF NOT EXISTS cab (
    id BIGSERIAL PRIMARY KEY,
    driver_id BIGINT NOT NULL,
    total_seats INT NOT NULL,
    available_seats INT NOT NULL,
    max_luggage INT NOT NULL,
    status VARCHAR(32) NOT NULL,
    current_lat DOUBLE PRECISION NOT NULL,
    current_lng DOUBLE PRECISION NOT NULL,
    version INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_cab_status ON cab(status);
CREATE INDEX idx_cab_location ON cab(current_lat, current_lng);

CREATE TABLE IF NOT EXISTS passenger (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    phone VARCHAR(32) NOT NULL
);

CREATE TABLE IF NOT EXISTS ride_request (
    id BIGSERIAL PRIMARY KEY,
    passenger_id BIGINT NOT NULL REFERENCES passenger(id),
    pickup_lat DOUBLE PRECISION NOT NULL,
    pickup_lng DOUBLE PRECISION NOT NULL,
    drop_lat DOUBLE PRECISION NOT NULL,
    drop_lng DOUBLE PRECISION NOT NULL,
    luggage_count INT NOT NULL,
    detour_tolerance_km DOUBLE PRECISION NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_ride_request_status ON ride_request(status);
CREATE INDEX idx_ride_request_pickup ON ride_request(pickup_lat, pickup_lng);

CREATE TABLE IF NOT EXISTS ride (
    id BIGSERIAL PRIMARY KEY,
    cab_id BIGINT NOT NULL REFERENCES cab(id),
    total_distance DOUBLE PRECISION NOT NULL,
    total_price DOUBLE PRECISION NOT NULL,
    status VARCHAR(32) NOT NULL
);

CREATE TABLE IF NOT EXISTS ride_passenger (
    ride_id BIGINT NOT NULL REFERENCES ride(id),
    passenger_id BIGINT NOT NULL REFERENCES passenger(id),
    pickup_order INT NOT NULL,
    drop_order INT NOT NULL,
    PRIMARY KEY (ride_id, passenger_id)
);

-- Add foreign key indexes
CREATE INDEX idx_ride_cab_id ON ride(cab_id);
CREATE INDEX idx_ride_passenger_ride_id ON ride_passenger(ride_id);
CREATE INDEX idx_ride_passenger_passenger_id ON ride_passenger(passenger_id);
