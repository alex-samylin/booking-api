package com.hostfully.booking.service;

import com.hostfully.booking.model.Block;
import com.hostfully.booking.model.Booking;
import com.hostfully.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import com.hostfully.booking.error.BookingConflictException;
import com.hostfully.booking.model.enums.BookingStatus;
import com.hostfully.booking.repository.BlockRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;

@Service
@RequiredArgsConstructor
public class BookingAvailabilityService {

    private final BookingRepository bookingRepository;
    private final BlockRepository blockRepository;

    public void validatePeriodAvailable(Long rentalPropertyId, LocalDate start, LocalDate end) {
        validatePeriodAvailable(rentalPropertyId, start, end, empty(), empty());
    }

    public void validatePeriodAvailable(Long rentalPropertyId, LocalDate start, LocalDate end, Optional<Long> excludeBookingId, Optional<Long> excludeBlockId) {
        validateBookingConflict(rentalPropertyId, start, end, excludeBookingId);
        validateBlockConflict(rentalPropertyId, start, end, excludeBlockId);
    }

    private void validateBookingConflict(Long rentalPropertyId, LocalDate start, LocalDate end, Optional<Long> excludeBookingId) {
        List<Booking> bookings = bookingRepository.findBookings(
                rentalPropertyId,
                BookingStatus.NEW,
                start,
                end
        );

        boolean hasConflict = bookings.stream()
                .anyMatch(b -> excludeBookingId.map(id -> !b.getId().equals(id)).orElse(true));

        if (hasConflict) {
            throw new BookingConflictException("Period overlaps with active booking!");
        }
    }

    private void validateBlockConflict(Long rentalPropertyId, LocalDate start, LocalDate end, Optional<Long> excludeBlockId) {
        List<Block> conflictingBlocks = blockRepository.findBlocks(
                rentalPropertyId,
                start,
                end
        );

        boolean hasConflict = conflictingBlocks.stream()
                .anyMatch(b -> excludeBlockId.map(id -> !b.getId().equals(id)).orElse(true));

        if (hasConflict) {
            throw new BookingConflictException("Period overlaps with a block!");
        }
    }

}

