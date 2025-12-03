package com.flightapp.notificationservice.service;

import com.flightapp.notificationservice.messaging.EmailMessage;
import java.util.List;

public interface EmailService {

    void sendEmail(EmailMessage message);

    List<String> getLogs();
}
