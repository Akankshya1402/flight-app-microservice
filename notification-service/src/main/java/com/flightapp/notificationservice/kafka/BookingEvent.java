package com.flightapp.notificationservice.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingEvent {

    private Long bookingId;
    private String customerEmail;
    private String flightNumber;

    // MUST BE STRING
    private String bookingTime;

    private double amount;
}
