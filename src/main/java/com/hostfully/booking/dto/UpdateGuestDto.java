package com.hostfully.booking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateGuestDto {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

}
