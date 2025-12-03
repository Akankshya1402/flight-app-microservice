package com.flightapp.bookingservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingNotFoundExceptionTest {

    @Test
    void messageIsPropagated() {
        BookingNotFoundException ex = new BookingNotFoundException("Missing booking");
        assertEquals("Missing booking", ex.getMessage());
    }
}
