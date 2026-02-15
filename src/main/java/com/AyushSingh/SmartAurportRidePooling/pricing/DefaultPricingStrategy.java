package com.AyushSingh.SmartAurportRidePooling.pricing;

import org.springframework.stereotype.Component;

@Component
public class DefaultPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(double baseFare, double distance, double perKmRate, int passengerCount, double surgeMultiplier) {
        double price = baseFare + (distance * perKmRate);
        double sharingDiscount = 1.0 - 0.1 * (passengerCount - 1); // 10% per extra
        if (sharingDiscount < 0.7) sharingDiscount = 0.7;
        return price * surgeMultiplier * sharingDiscount;
    }
}
