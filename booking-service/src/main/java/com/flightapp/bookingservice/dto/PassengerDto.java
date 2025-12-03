package com.flightapp.bookingservice.dto;

import jakarta.validation.constraints.*;

public record PassengerDto(
        @NotBlank String name,
        @NotBlank String gender,
        @Min(0) @Max(120) int age,
        @NotBlank String seatNumber
) {}
