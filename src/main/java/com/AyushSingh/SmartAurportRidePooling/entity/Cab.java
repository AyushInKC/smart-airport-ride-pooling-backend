package com.AyushSingh.SmartAurportRidePooling.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cab", indexes = {
        @Index(name = "idx_cab_status", columnList = "status"),
        @Index(name = "idx_cab_location", columnList = "currentLat,currentLng")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class    Cab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long id;
    private Long driverId;
    private int totalSeats;
    private int availableSeats;
    private int maxLuggage;
    private String status;
    private double currentLat;
    private double currentLng;
    @Version
    private int version;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
