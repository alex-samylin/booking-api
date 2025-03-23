package com.hostfully.booking.dto;

import com.hostfully.booking.model.enums.BlockSource;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateBlockDto {
    @NotNull
    private Long rentalPropertyId;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    private String reason;
    @NotNull
    private BlockSource source;
}
