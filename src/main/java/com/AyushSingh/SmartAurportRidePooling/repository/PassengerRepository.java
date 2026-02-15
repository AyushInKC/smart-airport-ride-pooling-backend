package com.AyushSingh.SmartAurportRidePooling.repository;

import com.AyushSingh.SmartAurportRidePooling.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
