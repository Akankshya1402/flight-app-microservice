package com.flightapp.notificationservice.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleMail_returnsBadGateway() {
        MailSendException ex = new MailSendException("Mail failed");
        ResponseEntity<Map<String, Object>> res = handler.handleMail(ex);

        assertEquals(502, res.getStatusCodeValue());
    }

    @Test
    void handleBadRequest() {
        IllegalArgumentException ex = new IllegalArgumentException("Bad");
        ResponseEntity<Map<String, Object>> res = handler.handleBadRequest(ex);

        assertEquals(400, res.getStatusCodeValue());
    }

    @Test
    void handleGeneric() {
        Exception ex = new Exception("Err");
        ResponseEntity<Map<String, Object>> res = handler.handleGeneric(ex);

        assertEquals(500, res.getStatusCodeValue());
    }
}
