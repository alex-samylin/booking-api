package com.hostfully.booking.controller;

import com.hostfully.booking.dto.CreateGuestDto;
import com.hostfully.booking.dto.UpdateGuestDto;
import com.hostfully.booking.service.GuestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.hostfully.booking.model.Guest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
@Slf4j
public class GuestController {

    private final GuestService guestService;

    @GetMapping("/{id}")
    public Guest getGuest(@PathVariable Long id) {
        return guestService.getGuestById(id);
    }

    @PostMapping
    public Guest createGuest(@RequestBody @Valid CreateGuestDto dto) {
        log.info("Creating new guest: {}", dto);
        return guestService.createGuest(dto);
    }

    @PutMapping("/{id}")
    public Guest updateGuest(@PathVariable Long id, @RequestBody @Valid UpdateGuestDto dto) {
        log.info("Updating guest with ID {}, {}", id, dto);
        return guestService.updateGuest(id, dto);
    }
}


