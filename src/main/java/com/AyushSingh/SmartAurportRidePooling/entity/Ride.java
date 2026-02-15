package com.AyushSingh.SmartAurportRidePooling.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "ride", indexes = {
        @Index(name = "idx_ride_cab_id", columnList = "cab_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cab_id")
    private Cab cab;
    private double totalDistance;
    private double totalPrice;
    private String status;
    @OneToMany(mappedBy = "ride", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<RidePassenger> ridePassengers = new ArrayList<>();
}
