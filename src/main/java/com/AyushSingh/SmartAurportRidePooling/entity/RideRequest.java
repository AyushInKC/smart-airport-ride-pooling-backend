package com.AyushSingh.SmartAurportRidePooling.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ride_request", indexes = {
        @Index(name = "idx_ride_request_status", columnList = "status"),
        @Index(name = "idx_ride_request_pickup", columnList = "pickupLat,pickupLng")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;
    private double pickupLat;
    private double pickupLng;
    private double dropLat;
    private double dropLng;
    private int luggageCount;
    private double detourToleranceKm;
    private String status;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
