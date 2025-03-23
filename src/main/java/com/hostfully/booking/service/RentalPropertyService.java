package com.hostfully.booking.service;

import com.hostfully.booking.repository.RentalPropertyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.hostfully.booking.model.RentalProperty;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RentalPropertyService {

    private final RentalPropertyRepository rentalPropertyRepository;

    public RentalProperty getRentalPropertyById(Long id) {
        return rentalPropertyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Property not found by id: " + id));
    }

}
