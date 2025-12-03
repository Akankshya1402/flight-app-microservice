package com.flightapp.bookingservice.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public record BookingRequest(
        @NotBlank String customerName,
        @Email @NotBlank String email,
        @NotNull @Positive Integer numberOfSeats,
        @NotNull @Size(min = 1) List<PassengerDto> passengers,
        boolean mealRequired,
        @NotNull LocalDateTime journeyDate
) {}
