package com.hostfully.booking.controller;

import com.hostfully.booking.dto.CreateBookingDto;
import com.hostfully.booking.dto.UpdateBookingDatesDto;
import com.hostfully.booking.model.Booking;
import com.hostfully.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/{id}")
    public Booking getBooking(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @PostMapping
    public Booking createBooking(@RequestBody @Valid CreateBookingDto dto) {
        log.info("Creating booking: {}", dto);
        return bookingService.createBooking(dto);
    }

    @PutMapping("/{id}/dates")
    public Booking updateBookingDates(
            @PathVariable Long id,
            @RequestBody @Valid UpdateBookingDatesDto dto) {
        log.info("Updating booking dates for ID {}, {}", id, dto);
        return bookingService.updateBookingDates(id, dto);
    }

    @PostMapping("/{id}/cancel")
    public Booking cancelBooking(@PathVariable Long id) {
        log.info("Canceling booking with ID {}", id);
        return bookingService.cancelBooking(id);
    }

    @PostMapping("/{id}/rebook")
    public Booking rebook(@PathVariable Long id) {
        log.info("Rebooking canceled booking with ID {}", id);
        return bookingService.rebook(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id) {
        log.info("Deleting booking with ID {}", id);
        bookingService.deleteBooking(id);
    }

}

