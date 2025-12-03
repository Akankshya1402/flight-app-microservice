package com.flightapp.flightservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AddInventoryRequest(
        @NotBlank String airlineCode,
        @NotBlank String airlineName,
        String logoUrl,
        @NotBlank String fromCity,
        @NotBlank String toCity,
        @NotNull @Future LocalDateTime departureTime,
        @NotNull @Future LocalDateTime arrivalTime,
        boolean roundTrip,
        @NotNull Integer totalSeats,
        @NotNull BigDecimal priceOneWay,
        BigDecimal priceRoundTrip
) {}

