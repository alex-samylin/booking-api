package com.hostfully.booking.service;

import com.hostfully.booking.dto.UpdateBookingDatesDto;
import com.hostfully.booking.model.RentalProperty;
import com.hostfully.booking.repository.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.hostfully.booking.dto.CreateBookingDto;
import com.hostfully.booking.model.Booking;
import com.hostfully.booking.model.Guest;
import com.hostfully.booking.model.enums.BookingStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@RequiredArgsConstructor
@Service
@Transactional
public class BookingService {

    private final BookingAvailabilityService bookingAvailabilityService;
    private final GuestService guestRepository;
    private final RentalPropertyService rentalPropertyService;
    private final BookingRepository bookingRepository;

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found by id: " + id));
    }

    public Booking createBooking(CreateBookingDto dto) {
        Guest guest = guestRepository.getGuestById(dto.getGuestId());
        RentalProperty property = rentalPropertyService.getRentalPropertyById(dto.getRentalPropertyId());

        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before or equal to end date!");
        }

        bookingAvailabilityService.validatePeriodAvailable(property.getId(), dto.getStartDate(), dto.getEndDate());

        Booking booking = Booking.builder()
                .guestId(guest.getId())
                .rentalPropertyId(property.getId())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(BookingStatus.NEW)
                .build();

        return bookingRepository.save(booking);
    }

    public Booking updateBookingDates(Long id, UpdateBookingDatesDto dto) {
        Booking booking = getBookingById(id);

        if (booking.getStatus() != BookingStatus.NEW) {
            throw new IllegalArgumentException("Only active bookings can be updated!");
        }

        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before or equal to end date!");
        }

        bookingAvailabilityService.validatePeriodAvailable(booking.getRentalPropertyId(), dto.getStartDate(), dto.getEndDate(), of(booking.getId()), empty());

        booking.setStartDate(dto.getStartDate());
        booking.setEndDate(dto.getEndDate());

        return bookingRepository.save(booking);
    }

    public Booking cancelBooking(Long id) {
        Booking booking = getBookingById(id);

        if (booking.getStatus() == BookingStatus.CANCELED) {
            throw new IllegalArgumentException("Booking is already canceled");
        }

        booking.setStatus(BookingStatus.CANCELED);
        return bookingRepository.save(booking);
    }

    public Booking rebook(Long id) {
        Booking booking = getBookingById(id);

        if (booking.getStatus() != BookingStatus.CANCELED) {
            throw new IllegalArgumentException("Only canceled bookings can be rebooked");
        }

        bookingAvailabilityService.validatePeriodAvailable(booking.getRentalPropertyId(), booking.getStartDate(), booking.getEndDate(), of(booking.getId()), empty());

        booking.setStatus(BookingStatus.NEW);
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

}
