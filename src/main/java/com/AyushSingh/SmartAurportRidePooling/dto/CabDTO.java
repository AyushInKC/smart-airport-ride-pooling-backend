package com.AyushSingh.SmartAurportRidePooling.dto;

import lombok.Data;

@Data
public class CabDTO {
    private Long id;
    private Long driverId;
    private int totalSeats;
    private int availableSeats;
    private int maxLuggage;
    private String status;
    private double currentLat;
    private double currentLng;
}
