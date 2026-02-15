package com.AyushSingh.SmartAurportRidePooling.service;

import com.AyushSingh.SmartAurportRidePooling.dto.*;
import com.AyushSingh.SmartAurportRidePooling.entity.*;
import com.AyushSingh.SmartAurportRidePooling.repository.*;
import com.AyushSingh.SmartAurportRidePooling.matching.MatchingEngine;
import com.AyushSingh.SmartAurportRidePooling.pricing.PricingStrategy;
import com.AyushSingh.SmartAurportRidePooling.concurrency.RedisDistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class RideService {
    private final CabRepository cabRepo;
    private final PassengerRepository passengerRepo;
    private final RideRequestRepository rideRequestRepo;
    private final RideRepository rideRepo;
    private final RidePassengerRepository ridePassengerRepo;
    private final MatchingEngine matchingEngine;
    private final PricingStrategy pricingStrategy;
    private final RedisDistributedLock redisLock;
    private final StringRedisTemplate redisTemplate;
    private final ThreadPoolTaskExecutor matchingExecutor;

    @Transactional
    public RideResponseDTO requestRide(RideRequestDTO dto) {
        Passenger passenger = passengerRepo.findById(dto.getPassengerId()).orElseThrow();
        RideRequest req = RideRequest.builder()
                .passenger(passenger)
                .pickupLat(dto.getPickupLat())
                .pickupLng(dto.getPickupLng())
                .dropLat(dto.getDropLat())
                .dropLng(dto.getDropLng())
                .luggageCount(dto.getLuggageCount())
                .detourToleranceKm(dto.getDetourToleranceKm())
                .status("PENDING")
                .build();
        rideRequestRepo.save(req);

        // Distributed lock for ride assignment
        String lockKey = "ride:assign:" + req.getId();
        String lockVal = UUID.randomUUID().toString();
        boolean locked = redisLock.tryLock(lockKey, lockVal, 2000);
        if (!locked) throw new RuntimeException("System busy, try again");
        try {
            // Find available cabs in area
            List<Cab> cabs = cabRepo.findAvailableCabsInArea("AVAILABLE", dto.getPickupLat()-0.05, dto.getPickupLat()+0.05, dto.getPickupLng()-0.05, dto.getPickupLng()+0.05);
            // Get active rides from cache/db
            Map<Long, Ride> activeRides = new HashMap<>();
            for (Cab cab : cabs) {
                Ride ride = getActiveRideByCabId(cab.getId());
                if (ride != null) activeRides.put(cab.getId(), ride);
            }
            Cab bestCab = matchingEngine.selectBestCab(cabs, req, activeRides);
            if (bestCab == null) throw new RuntimeException("No suitable cab found");
            // Optimistic lock for seat update
            Cab cab = cabRepo.findCabById(bestCab.getId());
            if (cab.getAvailableSeats() < 1) throw new RuntimeException("No seats left");
            cab.setAvailableSeats(cab.getAvailableSeats() - 1);
            cabRepo.save(cab);
            // Create or update ride
            Ride ride = activeRides.getOrDefault(cab.getId(), null);
            if (ride == null) {
                ride = Ride.builder().cab(cab).totalDistance(0).totalPrice(0).status("ACTIVE").build();
                rideRepo.save(ride);
            }
            // Add passenger to ride
            RidePassenger rp = RidePassenger.builder().ride(ride).passenger(passenger).pickupOrder(ride.getRidePassengers().size()+1).dropOrder(ride.getRidePassengers().size()+1).build();
            ridePassengerRepo.save(rp);
            // Calculate price
            double distance = Math.sqrt(Math.pow(dto.getPickupLat()-dto.getDropLat(),2)+Math.pow(dto.getPickupLng()-dto.getDropLng(),2))*111;
            double surge = 1.0 + (double)rideRequestRepo.findByStatus("PENDING").size() / (cabRepo.findByStatus("AVAILABLE").size()+1);
            double price = pricingStrategy.calculatePrice(100, distance, 20, ride.getRidePassengers().size(), surge);
            ride.setTotalDistance(ride.getTotalDistance() + distance);
            ride.setTotalPrice(ride.getTotalPrice() + price);
            rideRepo.save(ride);
            // Cache active ride
            redisTemplate.opsForValue().set("ride:active:"+ride.getId(), ride.getId().toString());
            // Update request status
            req.setStatus("ASSIGNED");
            rideRequestRepo.save(req);
            // Response
            RideResponseDTO resp = new RideResponseDTO();
            resp.setRideId(ride.getId());
            resp.setCabId(cab.getId());
            resp.setPassengerIds(Collections.singletonList(passenger.getId()));
            resp.setTotalDistance(ride.getTotalDistance());
            resp.setTotalPrice(ride.getTotalPrice());
            resp.setStatus(ride.getStatus());
            return resp;
        } finally {
            redisLock.unlock(lockKey, lockVal);
        }
    }

    @Transactional
    public void cancelRide(Long rideId) {
        Ride ride = rideRepo.findById(rideId).orElseThrow();
        ride.setStatus("CANCELLED");
        rideRepo.save(ride);
        // Free up cab seat
        Cab cab = ride.getCab();
        cab.setAvailableSeats(cab.getAvailableSeats() + 1);
        cabRepo.save(cab);
        // Remove from cache
        redisTemplate.delete("ride:active:"+rideId);
    }

    @Transactional(readOnly = true)
    public RideResponseDTO getRide(Long rideId) {
        Ride ride = rideRepo.findById(rideId).orElseThrow();
        RideResponseDTO resp = new RideResponseDTO();
        resp.setRideId(ride.getId());
        resp.setCabId(ride.getCab().getId());
        List<Long> pids = new ArrayList<>();
        for (RidePassenger rp : ride.getRidePassengers()) pids.add(rp.getPassenger().getId());
        resp.setPassengerIds(pids);
        resp.setTotalDistance(ride.getTotalDistance());
        resp.setTotalPrice(ride.getTotalPrice());
        resp.setStatus(ride.getStatus());
        return resp;
    }

    @Transactional(readOnly = true)
    public List<RideResponseDTO> getActiveRides() {
        List<Ride> rides = rideRepo.findByStatus("ACTIVE");
        List<RideResponseDTO> list = new ArrayList<>();
        for (Ride ride : rides) list.add(getRide(ride.getId()));
        return list;
    }

    @Transactional(readOnly = true)
    public List<CabDTO> getAvailableCabs() {
        List<Cab> cabs = cabRepo.findByStatus("AVAILABLE");
        List<CabDTO> list = new ArrayList<>();
        for (Cab cab : cabs) {
            CabDTO dto = new CabDTO();
            dto.setId(cab.getId());
            dto.setDriverId(cab.getDriverId());
            dto.setTotalSeats(cab.getTotalSeats());
            dto.setAvailableSeats(cab.getAvailableSeats());
            dto.setMaxLuggage(cab.getMaxLuggage());
            dto.setStatus(cab.getStatus());
            dto.setCurrentLat(cab.getCurrentLat());
            dto.setCurrentLng(cab.getCurrentLng());
            list.add(dto);
        }
        return list;
    }

    private Ride getActiveRideByCabId(Long cabId) {
        // Try cache first
        Object cached = redisTemplate.opsForValue().get("ride:active:"+cabId);
        if (cached instanceof Ride) return (Ride) cached;
        List<Ride> rides = rideRepo.findByStatus("ACTIVE");
        for (Ride ride : rides) if (ride.getCab().getId().equals(cabId)) return ride;
        return null;
    }
}
