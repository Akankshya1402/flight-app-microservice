package com.flightapp.notificationservice.controller;

import com.flightapp.notificationservice.messaging.EmailMessage;
import com.flightapp.notificationservice.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final EmailService emailService;

    // In-memory log list – good for debugging and assignment demo
    private final List<String> logs = Collections.synchronizedList(new ArrayList<>());

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Simple health endpoint for notification-service
     * GET /api/notification/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("notification-service is up");
    }

    /**
     * Test email endpoint – used by Postman/Newman
     * POST /api/notification/test-email?to=test@example.com
     */
    @PostMapping("/test-email")
    public ResponseEntity<String> sendTestEmail(@RequestParam String to) {
        EmailMessage message = new EmailMessage(
                to,
                "Test Email - Notification Service",
                "This is a test email from notification-service."
        );

        emailService.sendEmail(message);
        logs.add("Test email sent to: " + to);

        return ResponseEntity.ok("Email sent to " + to);
    }

    /**
     * View internal notification logs
     * GET /api/notification/logs
     */
    @GetMapping("/logs")
    public ResponseEntity<List<String>> getLogs() {
        return ResponseEntity.ok(new ArrayList<>(logs));
    }
}

