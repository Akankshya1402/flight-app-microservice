package com.flightapp.flightservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlightNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        FlightNotFoundException ex = new FlightNotFoundException("Flight missing");
        assertEquals("Flight missing", ex.getMessage());
    }
}
