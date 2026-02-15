package com.AyushSingh.SmartAurportRidePooling.dto;

import lombok.Data;
import java.util.List;

@Data
public class RideResponseDTO {
    private Long rideId;
    private Long cabId;
    private List<Long> passengerIds;
    private double totalDistance;
    private double totalPrice;
    private String status;
}
