package com.flightapp.notificationservice.messaging;

import com.flightapp.notificationservice.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EmailListenerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailListener emailListener;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleEmailMessage_success() {
        EmailMessage msg = new EmailMessage("a@a.com", "Hi", "Body");

        doNothing().when(emailService).sendEmail(msg);

        emailListener.handleEmailMessage(msg);

        verify(emailService, times(1)).sendEmail(msg);
    }

    @Test
    void handleEmailMessage_failure_throwsToDLQ() {
        EmailMessage msg = new EmailMessage("a@a.com", "Hi", "Body");

        doThrow(new RuntimeException("fail"))
                .when(emailService)
                .sendEmail(msg);

        assertThrows(RuntimeException.class, () ->
                emailListener.handleEmailMessage(msg)
        );
    }
}

