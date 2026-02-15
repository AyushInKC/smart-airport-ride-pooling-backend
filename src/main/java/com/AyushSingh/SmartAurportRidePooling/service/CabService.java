package com.AyushSingh.SmartAurportRidePooling.service;

import com.AyushSingh.SmartAurportRidePooling.dto.CabDTO;
import com.AyushSingh.SmartAurportRidePooling.entity.Cab;
import com.AyushSingh.SmartAurportRidePooling.repository.CabRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CabService {
    private final CabRepository cabRepo;
    public CabDTO addCab(CabDTO dto) {
        Cab cab = Cab.builder()
                .driverId(dto.getDriverId())
                .totalSeats(dto.getTotalSeats())
                .availableSeats(dto.getAvailableSeats())
                .maxLuggage(dto.getMaxLuggage())
                .status(dto.getStatus())
                .currentLat(dto.getCurrentLat())
                .currentLng(dto.getCurrentLng())
                .build();
        cabRepo.save(cab);
        dto.setId(cab.getId());
        return dto;
    }
}
