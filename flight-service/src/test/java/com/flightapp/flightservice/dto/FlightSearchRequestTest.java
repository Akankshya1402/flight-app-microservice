package com.flightapp.flightservice.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FlightSearchRequestTest {

    @Test
    void testRecordFields() {
        LocalDateTime d1 = LocalDateTime.now().plusDays(2);
        LocalDateTime d2 = d1.plusHours(3);

        FlightSearchRequest req = new FlightSearchRequest(
                "A",
                "B",
                d1,
                d2,
                false
        );

        assertEquals("A", req.fromCity());
        assertEquals("B", req.toCity());
        assertEquals(d1, req.departureFrom());
        assertEquals(d2, req.departureTo());
        assertFalse(req.roundTrip());
    }
}
