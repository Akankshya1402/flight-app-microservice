package com.flightapp.flightservice.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FlightSummaryTest {

    @Test
    void testRecordFields() {
        LocalDateTime d1 = LocalDateTime.now();
        LocalDateTime d2 = d1.plusHours(2);

        FlightSummary fs = new FlightSummary(
                1L,
                "AirX",
                "logo.png",
                "A",
                "B",
                d1,
                d2,
                false,
                BigDecimal.ONE,
                BigDecimal.TEN,
                50
        );

        assertEquals(1L, fs.id());
        assertEquals("AirX", fs.airlineName());
        assertEquals("logo.png", fs.logoUrl());
        assertEquals("A", fs.fromCity());
        assertEquals("B", fs.toCity());
        assertEquals(d1, fs.departureTime());
        assertEquals(d2, fs.arrivalTime());
        assertFalse(fs.roundTrip());
        assertEquals(BigDecimal.ONE, fs.priceOneWay());
        assertEquals(BigDecimal.TEN, fs.priceRoundTrip());
        assertEquals(50, fs.availableSeats());
    }
}

