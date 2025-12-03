package com.flightapp.notificationservice.service;

import com.flightapp.notificationservice.kafka.BookingEvent;
import com.flightapp.notificationservice.messaging.EmailMessage;

public interface EmailService {

    void sendEmail(EmailMessage message);

    void sendBookingConfirmation(BookingEvent event);
}
