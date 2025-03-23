package com.hostfully.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hostfully.booking.dto.CreateBookingDto;
import com.hostfully.booking.dto.UpdateBookingDatesDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String endpoint = "/api/bookings";

    @Test
    @Sql("/test-data/booking-test-data.sql")
    void shouldCreateBookingWhenPeriodIsAvailable() throws Exception {
        CreateBookingDto dto = createBookingDto(
                1L,
                1L,
                LocalDate.of(2025, 4, 16),
                LocalDate.of(2025, 4, 18)
        );
        String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @Sql("/test-data/booking-test-data.sql")
    void shouldFailToCreateBookingWhenDatesOverlapWithBooking() throws Exception {
        CreateBookingDto dto = createBookingDto(
                1L,
                1L,
                LocalDate.of(2025, 4, 12),
                LocalDate.of(2025, 4, 14)
        );
        String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Period overlaps with active booking!"));
    }

    @Test
    @Sql("/test-data/booking-test-data.sql")
    void shouldFailToCreateBookingWhenDatesOverlapWithBlock() throws Exception {
        CreateBookingDto dto = createBookingDto(
                1L,
                1L,
                LocalDate.of(2025, 4, 22),
                LocalDate.of(2025, 4, 23)
        );
        String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Period overlaps with a block!"));
    }

    @Test
    @Sql("/test-data/booking-test-data.sql")
    void shouldUpdateBookingDatesSuccessfully() throws Exception {
        UpdateBookingDatesDto dto = updateBookingDto(
                LocalDate.of(2025, 4, 26),
                LocalDate.of(2025, 4, 29)
        );
        String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/bookings/100/dates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.startDate").value("2025-04-26"))
                .andExpect(jsonPath("$.endDate").value("2025-04-29"));
    }

    @Test
    @Sql("/test-data/booking-test-data.sql")
    void shouldFailToUpdateBookingDatesDueToConflict() throws Exception {
        UpdateBookingDatesDto dto = updateBookingDto(
                LocalDate.of(2025, 5, 6),
                LocalDate.of(2025, 5, 9)
        );
        String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/bookings/100/dates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Period overlaps with active booking!"));
    }

    @Test
    @Sql("/test-data/booking-test-data.sql")
    void shouldCancelBookingSuccessfully() throws Exception {
        mockMvc.perform(post("/api/bookings/100/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.status").value("CANCELED"));
    }

    @Test
    @Sql("/test-data/booking-test-data.sql")
    void shouldRebookWhenPeriodIsAvailable() throws Exception {
        mockMvc.perform(post("/api/bookings/102/rebook"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(102))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    @Sql("/test-data/booking-test-data.sql")
    void shouldFailToRebookDueToConflict() throws Exception {
        mockMvc.perform(post("/api/bookings/103/rebook"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Period overlaps with active booking!"));
    }

    private CreateBookingDto createBookingDto(Long guestId, Long rentalPropertyId, LocalDate startDate, LocalDate endDate) {
        CreateBookingDto dto = new CreateBookingDto();
        dto.setGuestId(guestId);
        dto.setRentalPropertyId(rentalPropertyId);
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);
        return dto;
    }

    private UpdateBookingDatesDto updateBookingDto(LocalDate start, LocalDate end) {
        UpdateBookingDatesDto dto = new UpdateBookingDatesDto();
        dto.setStartDate(start);
        dto.setEndDate(end);
        return dto;
    }

}
