package com.AyushSingh.SmartAurportRidePooling.pricing;

public interface PricingStrategy {
    double calculatePrice(
            double baseFare,
            double distance,
            double perKmRate,
            int passengerCount,
            double surgeMultiplier
    );
}
