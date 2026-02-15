-- V2__seed_data.sql
-- Sample seed data for cabs, passengers, and ride requests

INSERT INTO cab (driver_id, total_seats, available_seats, max_luggage, status, current_lat, current_lng, version)
VALUES
  (101, 4, 4, 4, 'AVAILABLE', 28.556, 77.100, 0),
  (102, 4, 4, 4, 'AVAILABLE', 28.558, 77.102, 0),
  (103, 6, 6, 6, 'AVAILABLE', 28.560, 77.104, 0);

INSERT INTO passenger (name, phone)
VALUES
  ('Alice', '9991112222'),
  ('Bob', '9991113333'),
  ('Charlie', '9991114444');

INSERT INTO ride_request (passenger_id, pickup_lat, pickup_lng, drop_lat, drop_lng, luggage_count, detour_tolerance_km, status)
VALUES
  (1, 28.555, 77.101, 28.600, 77.200, 1, 5, 'PENDING'),
  (2, 28.557, 77.103, 28.610, 77.210, 2, 7, 'PENDING'),
  (3, 28.559, 77.105, 28.620, 77.220, 1, 6, 'PENDING');