package com.AyushSingh.SmartAurportRidePooling.entity;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RidePassengerId implements Serializable {
    private Long ride;
    private Long passenger;
}
