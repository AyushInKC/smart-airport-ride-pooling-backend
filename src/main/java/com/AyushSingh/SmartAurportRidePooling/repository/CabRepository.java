package com.AyushSingh.SmartAurportRidePooling.repository;

import com.AyushSingh.SmartAurportRidePooling.entity.Cab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.List;

@Repository
public interface CabRepository extends JpaRepository<Cab, Long> {
    List<Cab> findByStatus(String status);

    @Lock(LockModeType.OPTIMISTIC)
    Cab findCabById(Long id);

    @Query("SELECT c FROM Cab c WHERE c.status = :status AND c.availableSeats > 0 AND c.currentLat BETWEEN :minLat AND :maxLat AND c.currentLng BETWEEN :minLng AND :maxLng")
    List<Cab> findAvailableCabsInArea(@Param("status") String status, @Param("minLat") double minLat, @Param("maxLat") double maxLat, @Param("minLng") double minLng, @Param("maxLng") double maxLng);
}
