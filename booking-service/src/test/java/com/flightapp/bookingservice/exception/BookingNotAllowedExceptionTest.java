package com.flightapp.bookingservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingNotAllowedExceptionTest {

    @Test
    void messageIsPropagated() {
        BookingNotAllowedException ex = new BookingNotAllowedException("Not allowed");
        assertEquals("Not allowed", ex.getMessage());
    }
}
