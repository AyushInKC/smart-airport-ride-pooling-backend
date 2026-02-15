package com.AyushSingh.SmartAurportRidePooling.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ride_passenger", indexes = {
        @Index(name = "idx_ride_passenger_ride_id", columnList = "ride_id"),
        @Index(name = "idx_ride_passenger_passenger_id", columnList = "passenger_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(RidePassengerId.class)
public class RidePassenger {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ride_id")
    private Ride ride;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;
    private int pickupOrder;
    private int dropOrder;
}
