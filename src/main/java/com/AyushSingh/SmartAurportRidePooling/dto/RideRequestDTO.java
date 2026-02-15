package com.AyushSingh.SmartAurportRidePooling.dto;

import lombok.Data;

@Data
public class RideRequestDTO {
    private Long passengerId;
    private double pickupLat;
    private double pickupLng;
    private double dropLat;
    private double dropLng;
    private int luggageCount;
    private double detourToleranceKm;
}
