package com.flightapp.flightservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record FlightSearchRequest(
        @NotBlank String fromCity,
        @NotBlank String toCity,
        @NotNull @Future LocalDateTime departureFrom,
        @NotNull @Future LocalDateTime departureTo,
        boolean roundTrip
) {}


