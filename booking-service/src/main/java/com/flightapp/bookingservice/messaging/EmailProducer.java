package com.flightapp.bookingservice.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailProducer {

    private static final Logger log = LoggerFactory.getLogger(EmailProducer.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${booking.email.exchange}")
    private String exchange;

    @Value("${booking.email.routing-key}")
    private String routingKey;

    public EmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendBookingEmail(EmailMessage message) {
        log.info("Sending booking email to={} via RabbitMQ", message.getTo());
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
