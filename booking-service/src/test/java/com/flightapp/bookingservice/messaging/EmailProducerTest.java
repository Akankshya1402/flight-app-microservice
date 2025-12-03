package com.flightapp.bookingservice.messaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.*;

class EmailProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private EmailProducer emailProducer;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // manually inject @Value fields since Spring is NOT running
        emailProducer.getClass()
                .getDeclaredFields();

        // reflection to set private fields
        try {
            var exchangeField = EmailProducer.class.getDeclaredField("exchange");
            exchangeField.setAccessible(true);
            exchangeField.set(emailProducer, "test-exchange");

            var routingField = EmailProducer.class.getDeclaredField("routingKey");
            routingField.setAccessible(true);
            routingField.set(emailProducer, "test.key");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void sendBookingEmail_sendsToRabbit() {

        EmailMessage message = new EmailMessage("a@a.com", "Sub", "Body");

        emailProducer.sendBookingEmail(message);

        verify(rabbitTemplate, times(1))
                .convertAndSend("test-exchange", "test.key", message);
    }
}



