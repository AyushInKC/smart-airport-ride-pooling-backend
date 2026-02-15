package com.AyushSingh.SmartAurportRidePooling.matching;

import com.AyushSingh.SmartAurportRidePooling.entity.*;
import java.util.*;

public class MatchingEngine {
    /**
     * Match ride requests to cabs using spatial filtering and greedy insertion.
     * Time complexity: O(N*M*logM) where N=requests, M=cabs in area.
     */
    public List<Cab> filterCabsByBoundingBox(List<Cab> cabs, double lat, double lng, double radiusKm) {
        // Simple bounding box filter (not Haversine for speed)
        double delta = radiusKm / 111.0; // ~1 deg = 111km
        double minLat = lat - delta, maxLat = lat + delta;
        double minLng = lng - delta, maxLng = lng + delta;
        List<Cab> result = new ArrayList<>();
        for (Cab cab : cabs) {
            if (cab.getCurrentLat() >= minLat && cab.getCurrentLat() <= maxLat &&
                cab.getCurrentLng() >= minLng && cab.getCurrentLng() <= maxLng) {
                result.add(cab);
            }
        }
        return result;
    }

    /**
     * Greedy insertion: try to insert request into best cab/ride.
     * Time complexity: O(M*logM) per request.
     */
    public Cab selectBestCab(List<Cab> cabs, RideRequest req, Map<Long, Ride> activeRides) {

        PriorityQueue<CabScore> pq =
                new PriorityQueue<>(Comparator.comparingDouble(c -> c.score));

        for (Cab cab : cabs) {

            if (cab.getAvailableSeats() < 1) continue;
            if (cab.getMaxLuggage() < req.getLuggageCount()) continue;

            Ride ride = activeRides.get(cab.getId());

            double detour = 0;

            if (ride != null) {
                detour = estimateDetour(ride, req);
                if (detour > req.getDetourToleranceKm()) continue;
            }

            // If ride == null → new ride → detour = 0
            pq.add(new CabScore(cab, detour));
        }

        return pq.isEmpty() ? null : pq.poll().cab;
    }


    private double estimateDetour(Ride ride, RideRequest req) {
        // Dummy: Euclidean distance from last drop to new drop
        if (ride.getRidePassengers().isEmpty()) return 0;
        RidePassenger last = ride.getRidePassengers().get(ride.getRidePassengers().size() - 1);
        double lastLat = ride.getCab().getCurrentLat();
        double lastLng = ride.getCab().getCurrentLng();
        return Math.sqrt(Math.pow(lastLat - req.getDropLat(), 2) + Math.pow(lastLng - req.getDropLng(), 2)) * 111;
    }

    private static class CabScore {
        Cab cab;
        double score;
        CabScore(Cab cab, double score) { this.cab = cab; this.score = score; }
    }
}
