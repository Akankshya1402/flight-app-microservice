package com.flightapp.notificationservice.messaging;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailMessageTest {

    @Test
    void testGettersAndSetters() {
        EmailMessage msg = new EmailMessage();
        msg.setTo("a@a.com");
        msg.setSubject("Hi");
        msg.setBody("Hello");

        assertEquals("a@a.com", msg.getTo());
        assertEquals("Hi", msg.getSubject());
        assertEquals("Hello", msg.getBody());
    }

    @Test
    void testConstructor() {
        EmailMessage msg = new EmailMessage("a@a.com", "Sub", "Body");

        assertEquals("a@a.com", msg.getTo());
        assertEquals("Sub", msg.getSubject());
        assertEquals("Body", msg.getBody());
    }
}
