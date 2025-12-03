package com.flightapp.bookingservice.exception;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void bookingNotFound_returns404() {
        BookingNotFoundException ex = new BookingNotFoundException("Not found");
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                handler.handleBookingNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not found", response.getBody().message());
    }

    @Test
    void bookingNotAllowed_returns400() {
        BookingNotAllowedException ex = new BookingNotAllowedException("Not allowed");
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                handler.handleBookingNotAllowed(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Not allowed", response.getBody().message());
    }

    @Test
    void illegalArgument_returns400() {
        IllegalArgumentException ex = new IllegalArgumentException("Bad arg");
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                handler.handleIllegalArgument(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad arg", response.getBody().message());
    }

    @Test
    void validation_returns400() {
        ConstraintViolationException ex =
                new ConstraintViolationException("Validation failed", Set.of());
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                handler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().message().contains("Validation failed"));
    }

    @Test
    void genericException_returns500() {
        Exception ex = new Exception("boom");
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                handler.handleGeneric(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal error", response.getBody().message());
    }
}

