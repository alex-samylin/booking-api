package com.hostfully.booking.service;

import com.hostfully.booking.dto.CreateGuestDto;
import com.hostfully.booking.dto.UpdateGuestDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.hostfully.booking.model.Guest;
import com.hostfully.booking.repository.GuestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GuestService {

    private final GuestRepository guestRepository;

    public Guest getGuestById(Long id) {
        return guestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found by id: " + id));
    }

    public Guest createGuest(CreateGuestDto dto) {
        Guest guest = Guest.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .build();

        return guestRepository.save(guest);
    }

    public Guest updateGuest(Long id, UpdateGuestDto dto) {
        Guest guest = getGuestById(id);

        guest.setFirstName(dto.getFirstName());
        guest.setLastName(dto.getLastName());

        return guestRepository.save(guest);
    }

}

