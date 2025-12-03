package com.flightapp.bookingservice.client.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FlightSummary(
        Long id,
        String airlineName,
        String logoUrl,
        String fromCity,
        String toCity,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        boolean roundTrip,
        BigDecimal priceOneWay,
        BigDecimal priceRoundTrip,
        Integer availableSeats
) {}
