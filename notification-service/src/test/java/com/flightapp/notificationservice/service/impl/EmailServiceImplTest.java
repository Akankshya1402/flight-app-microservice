package com.flightapp.notificationservice.service.impl;

import com.flightapp.notificationservice.messaging.EmailMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailServiceImplTest {

    @Test
    void testSendEmail_addsLog() {
        EmailServiceImpl service = new EmailServiceImpl();
        EmailMessage msg = new EmailMessage("x@y.com", "Hello", "Body");

        service.sendEmail(msg);

        assertEquals(1, service.getLogs().size());
        assertTrue(service.getLogs().get(0).contains("x@y.com"));
    }

    @Test
    void testGetLogs_returnsList() {
        EmailServiceImpl service = new EmailServiceImpl();
        assertNotNull(service.getLogs());
        assertEquals(0, service.getLogs().size());
    }
}
