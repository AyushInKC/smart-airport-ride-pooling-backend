package com.AyushSingh.SmartAurportRidePooling.controller;

import com.AyushSingh.SmartAurportRidePooling.dto.*;
import com.AyushSingh.SmartAurportRidePooling.service.PassengerService;
import com.AyushSingh.SmartAurportRidePooling.service.RideService;
import com.AyushSingh.SmartAurportRidePooling.service.CabService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api")

@RequiredArgsConstructor
public class RideController {
    private final RideService rideService;
    private final CabService cabService;
   private final PassengerService passengerService;

    @PostMapping("/cabs")
    public ResponseEntity<ApiResponse<?>> addCab(@RequestBody CabDTO cabDTO) {
        CabDTO created = cabService.addCab(cabDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Cab created", created));
    }
    @PostMapping("/passengers")
    public ResponseEntity<ApiResponse<?>> addPassenger(@RequestBody PassengerDTO dto) {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Passenger created", passengerService.addPassenger(dto))
        );
    }
    @GetMapping("/passengers/{id}")
    public ResponseEntity<ApiResponse<?>> getPassenger(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Passenger details", passengerService.getPassenger(id))
        );
    }


    @PostMapping("/rides/request")
    public ResponseEntity<ApiResponse<?>> requestRide(@RequestBody RideRequestDTO dto) {
        RideResponseDTO resp = rideService.requestRide(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Ride requested", resp));
    }


    @PostMapping("/rides/{id}/cancel")
    public ResponseEntity<ApiResponse<?>> cancelRide(@PathVariable Long id) {
        rideService.cancelRide(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Ride cancelled", null));
    }


    @GetMapping("/rides/{id}")
    public ResponseEntity<ApiResponse<?>> getRide(@PathVariable Long id) {
        RideResponseDTO resp = rideService.getRide(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Ride details", resp));
    }


    @GetMapping("/rides/active")
    public ResponseEntity<ApiResponse<?>> getActiveRides() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Active rides", rideService.getActiveRides()));
    }


    @GetMapping("/cabs/available")
    public ResponseEntity<ApiResponse<?>> getAvailableCabs() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Available cabs", rideService.getAvailableCabs()));
    }

}
