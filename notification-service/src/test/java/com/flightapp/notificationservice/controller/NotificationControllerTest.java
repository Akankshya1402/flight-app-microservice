package com.flightapp.notificationservice.controller;

import com.flightapp.notificationservice.messaging.EmailMessage;
import com.flightapp.notificationservice.messaging.RabbitConfig;
import com.flightapp.notificationservice.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationControllerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendTestEmail_publishesToRabbitMQ() {
        EmailMessage msg = new EmailMessage("a@a.com", "Hi", "Body");

        ResponseEntity<String> res = controller.sendTestEmail(msg);

        verify(rabbitTemplate, times(1)).convertAndSend(
                RabbitConfig.EMAIL_EXCHANGE,
                RabbitConfig.EMAIL_ROUTING_KEY,
                msg
        );

        assertEquals("Message published to RabbitMQ", res.getBody());
    }

    @Test
    void sendEmail_direct() {
        EmailMessage msg = new EmailMessage("a@a.com", "Hi", "Body");

        ResponseEntity<String> res = controller.sendEmail(msg);

        verify(emailService, times(1)).sendEmail(msg);
        assertEquals("Email processed", res.getBody());
    }

    @Test
    void getEmailLogs_returnsList() {
        when(emailService.getLogs()).thenReturn(List.of("Log1"));

        ResponseEntity<?> res = controller.getEmailLogs();

        assertEquals(List.of("Log1"), res.getBody());
    }
}
