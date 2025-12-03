package com.flightapp.notificationservice.service.impl;

import com.flightapp.notificationservice.kafka.BookingEvent;
import com.flightapp.notificationservice.messaging.EmailMessage;
import com.flightapp.notificationservice.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(EmailMessage message) {
        log.info("Sending email to={} subject={}", message.getTo(), message.getSubject());

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(message.getTo());
        mail.setSubject(message.getSubject());
        mail.setText(message.getBody());

        mailSender.send(mail);
    }

    @Override
    public void sendBookingConfirmation(BookingEvent event) {
        log.info("Sending booking confirmation email to={} bookingId={}",
                event.getCustomerEmail(), event.getBookingId());

        String body = "Your booking is confirmed!\n\n"
                + "Booking ID: " + event.getBookingId() + "\n"
                + "Flight: " + event.getFlightNumber() + "\n"
                + "Amount Paid: " + event.getAmount() + "\n"
                + "Booking Time: " + event.getBookingTime() + "\n\n"
                + "Thank you for choosing us!";

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(event.getCustomerEmail());
        mail.setSubject("Flight Booking Confirmation");
        mail.setText(body);

        mailSender.send(mail);
    }
}
