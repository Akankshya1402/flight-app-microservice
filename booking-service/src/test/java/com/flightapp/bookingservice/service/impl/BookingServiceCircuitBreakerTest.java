package com.flightapp.bookingservice.service.impl;

import com.flightapp.bookingservice.client.FlightServiceClient;
import com.flightapp.bookingservice.exception.BookingNotAllowedException;
import com.flightapp.bookingservice.messaging.EmailProducer;
import com.flightapp.bookingservice.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class BookingServiceCircuitBreakerTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FlightServiceClient flightServiceClient;

    @Mock
    private EmailProducer emailProducer;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void flightFallback_throwsBookingNotAllowedException() {
        assertThatThrownBy(() ->
                bookingService.flightFallback(1L, new RuntimeException("Service down"))
        ).isInstanceOf(BookingNotAllowedException.class)
         .hasMessageContaining("Flight service unavailable");
    }
}
