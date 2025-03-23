package com.hostfully.booking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateGuestDto {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    private String email;

}
