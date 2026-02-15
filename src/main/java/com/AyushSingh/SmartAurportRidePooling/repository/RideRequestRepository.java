package com.AyushSingh.SmartAurportRidePooling.repository;

import com.AyushSingh.SmartAurportRidePooling.entity.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RideRequestRepository extends JpaRepository<RideRequest, Long> {
    List<RideRequest> findByStatus(String status);
}
