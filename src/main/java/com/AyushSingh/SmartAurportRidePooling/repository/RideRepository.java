package com.AyushSingh.SmartAurportRidePooling.repository;

import com.AyushSingh.SmartAurportRidePooling.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findByStatus(String status);
}
