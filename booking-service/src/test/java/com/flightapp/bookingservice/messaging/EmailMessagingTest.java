package com.flightapp.bookingservice.messaging;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailMessagingTest {

    @Test
    void allArgsConstructor_andGetters_work() {
        EmailMessage message = new EmailMessage("to@example.com", "Subject", "Body");
        assertEquals("to@example.com", message.getTo());
        assertEquals("Subject", message.getSubject());
        assertEquals("Body", message.getBody());
    }

    @Test
    void defaultConstructor_andSetters_work() {
        EmailMessage message = new EmailMessage();
        message.setTo("x@example.com");
        message.setSubject("S");
        message.setBody("B");

        assertEquals("x@example.com", message.getTo());
        assertEquals("S", message.getSubject());
        assertEquals("B", message.getBody());
    }
}
