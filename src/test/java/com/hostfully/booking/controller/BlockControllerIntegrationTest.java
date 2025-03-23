package com.hostfully.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hostfully.booking.model.enums.BlockSource;
import com.hostfully.booking.dto.CreateBlockDto;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BlockControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String endpoint = "/api/blocks";

    @Test
    @Sql("/test-data/booking-test-data.sql")
    void shouldCreateBlockWhenDatesAreFree() throws Exception {
        CreateBlockDto dto = createBlockDto(
                LocalDate.of(2025, 5, 12),
                LocalDate.of(2025, 5, 14)
        );
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.startDate").value("2025-05-12"));
    }

    @Test
    @Sql("/test-data/booking-test-data.sql")
    void shouldFailToCreateBlockDueToBookingConflict() throws Exception {
        CreateBlockDto dto = createBlockDto(
                LocalDate.of(2025, 5, 6),
                LocalDate.of(2025, 5, 7)
        );
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Period overlaps with active booking!"));
    }

    @Test
    @Sql("/test-data/booking-test-data.sql")
    void shouldFailToCreateBlockDueToBlockConflict() throws Exception {
        CreateBlockDto dto = createBlockDto(
                LocalDate.of(2025, 4, 22),
                LocalDate.of(2025, 4, 23)
        );

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Period overlaps with a block!"));
    }

    private CreateBlockDto createBlockDto(LocalDate start, LocalDate end) {
        CreateBlockDto dto = new CreateBlockDto();
        dto.setRentalPropertyId(1L);
        dto.setReason("test");
        dto.setSource(BlockSource.MANAGER);
        dto.setStartDate(start);
        dto.setEndDate(end);
        return dto;
    }

}
