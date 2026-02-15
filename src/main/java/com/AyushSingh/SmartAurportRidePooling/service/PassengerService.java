package com.AyushSingh.SmartAurportRidePooling.service;

import com.AyushSingh.SmartAurportRidePooling.dto.PassengerDTO;
import com.AyushSingh.SmartAurportRidePooling.entity.Passenger;
import com.AyushSingh.SmartAurportRidePooling.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PassengerService {
    private final PassengerRepository passengerRepository;

    @Transactional
    public PassengerDTO addPassenger(PassengerDTO dto) {

        Passenger passenger = Passenger.builder()
                .name(dto.getName())
                .phone(dto.getPhone())
                .build();

        Passenger saved = passengerRepository.save(passenger);

        PassengerDTO response = new PassengerDTO();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setPhone(saved.getPhone());

        return response;
    }

    @Transactional(readOnly = true)
    public PassengerDTO getPassenger(Long id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Passenger not found"));

        PassengerDTO dto = new PassengerDTO();
        dto.setId(passenger.getId());
        dto.setName(passenger.getName());
        dto.setPhone(passenger.getPhone());

        return dto;
    }

}
