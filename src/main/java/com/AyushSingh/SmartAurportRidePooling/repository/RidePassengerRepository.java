package com.AyushSingh.SmartAurportRidePooling.repository;

import com.AyushSingh.SmartAurportRidePooling.entity.RidePassenger;
import com.AyushSingh.SmartAurportRidePooling.entity.RidePassengerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RidePassengerRepository extends JpaRepository<RidePassenger, RidePassengerId> {
}
