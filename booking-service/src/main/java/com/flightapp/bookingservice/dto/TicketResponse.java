package com.flightapp.bookingservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record TicketResponse(
        String pnr,
        Long flightId,
        String airlineName,
        String fromCity,
        String toCity,
        String customerName,
        String email,
        boolean mealRequired,
        LocalDateTime journeyDate,
        LocalDateTime bookingTime,
        boolean cancelled,
        LocalDateTime cancellationTime,
        BigDecimal totalAmount,
        List<PassengerDto> passengers
) {}
