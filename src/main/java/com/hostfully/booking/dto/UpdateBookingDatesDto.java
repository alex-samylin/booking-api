package com.hostfully.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.time.LocalDate;

@Data
public class UpdateBookingDatesDto {

    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
}

