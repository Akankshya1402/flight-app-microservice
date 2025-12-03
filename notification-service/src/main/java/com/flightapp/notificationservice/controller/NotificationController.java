package com.flightapp.notificationservice.controller;

import com.flightapp.notificationservice.messaging.EmailMessage;
import com.flightapp.notificationservice.messaging.RabbitConfig;
import com.flightapp.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final RabbitTemplate rabbitTemplate;
    private final EmailService emailService;

    // ---- TEST EMAIL THROUGH RABBITMQ ----
    @PostMapping("/test-email")
    public ResponseEntity<String> sendTestEmail(@RequestBody EmailMessage message) {

        rabbitTemplate.convertAndSend(
                RabbitConfig.EMAIL_EXCHANGE,
                RabbitConfig.EMAIL_ROUTING_KEY,
                message
        );

        return ResponseEntity.ok("Message published to RabbitMQ");
    }

    // ---- DIRECT EMAIL SEND (NO RABBITMQ) ----
    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailMessage message) {
        emailService.sendEmail(message);
        return ResponseEntity.ok("Email processed");
    }

    // ---- FETCH EMAIL LOGS ----
    @GetMapping("/logs")
    public ResponseEntity<?> getEmailLogs() {
        return ResponseEntity.ok(emailService.getLogs());
    }
}

