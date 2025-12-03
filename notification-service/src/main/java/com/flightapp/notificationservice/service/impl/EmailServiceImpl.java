package com.flightapp.notificationservice.service.impl;

import com.flightapp.notificationservice.messaging.EmailMessage;
import com.flightapp.notificationservice.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final List<String> logs = new ArrayList<>();

    @Override
    public void sendEmail(EmailMessage message) {

        String logEntry = "Email processed for: " + message.getTo()
                + " | Subject: " + message.getSubject();

        logs.add(logEntry);
        log.info(logEntry);
    }

    @Override
    public List<String> getLogs() {
        return logs;
    }
}

