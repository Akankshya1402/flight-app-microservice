package com.flightapp.flightservice.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AddInventoryRequestTest {

    @Test
    void testRecordFields() {
        LocalDateTime d1 = LocalDateTime.now().plusDays(1);
        LocalDateTime d2 = d1.plusHours(2);

        AddInventoryRequest req = new AddInventoryRequest(
                "AC",
                "Airline",
                "logo.png",
                "A",
                "B",
                d1,
                d2,
                true,
                120,
                BigDecimal.TEN,
                BigDecimal.valueOf(20)
        );

        assertEquals("AC", req.airlineCode());
        assertEquals("Airline", req.airlineName());
        assertEquals("logo.png", req.logoUrl());
        assertEquals("A", req.fromCity());
        assertEquals("B", req.toCity());
        assertEquals(d1, req.departureTime());
        assertEquals(d2, req.arrivalTime());
        assertTrue(req.roundTrip());
        assertEquals(120, req.totalSeats());
        assertEquals(BigDecimal.TEN, req.priceOneWay());
        assertEquals(BigDecimal.valueOf(20), req.priceRoundTrip());
    }
}
