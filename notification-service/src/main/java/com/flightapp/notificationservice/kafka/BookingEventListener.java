package com.flightapp.notificationservice.kafka;

import com.flightapp.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingEventListener {

    private final EmailService emailService;

    @KafkaListener(
            topics = "${kafka.topics.booking-created}",
            containerFactory = "bookingEventKafkaListenerContainerFactory"
    )
    public void handleBookingCreated(BookingEvent event) {
        log.info("Received BookingEvent from Kafka: {}", event);

        emailService.sendBookingConfirmation(event);
    }
}
