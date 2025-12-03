package com.flightapp.notificationservice.messaging;

import com.flightapp.notificationservice.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailListener {

    private static final Logger log = LoggerFactory.getLogger(EmailListener.class);

    private final EmailService emailService;

    public EmailListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${booking.email.queue}")
    public void handleEmail(@Payload EmailMessage message) {
        log.info("Received email message for={} subject={}", message.getTo(), message.getSubject());
        emailService.sendEmail(message);
    }
}
