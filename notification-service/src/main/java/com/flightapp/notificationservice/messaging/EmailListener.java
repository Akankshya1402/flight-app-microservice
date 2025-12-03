package com.flightapp.notificationservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailListener {

    private final com.flightapp.notificationservice.service.EmailService emailService;

    @RabbitListener(queues = RabbitConfig.EMAIL_QUEUE)
    public void handleEmailMessage(EmailMessage message) {
        log.info("Received email message from queue: {}", message);
        try {
            emailService.sendEmail(message);
        } catch (Exception ex) {
            log.error("Failed to process email message. Sending to DLQ…", ex);
            // throwing lets container reject message -> DLQ via x-dead-letter-exchange
            throw ex;
        }
    }
}
