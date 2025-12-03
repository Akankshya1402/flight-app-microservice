package com.flightapp.bookingservice.controller;

import com.flightapp.bookingservice.dto.BookingRequest;
import com.flightapp.bookingservice.dto.PassengerDto;
import com.flightapp.bookingservice.dto.TicketResponse;
import com.flightapp.bookingservice.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController controller;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void bookTicket_returns201() {
        BookingRequest req = new BookingRequest(
                "John",
                "a@a.com",
                1,
                List.of(new PassengerDto("A", "M", 22, "1A")), // <-- passengers in correct position
                false,
                LocalDateTime.now()
        );

        TicketResponse resp = new TicketResponse(
                "PNR",
                1L,
                "A",
                "B",
                "C",
                "John",
                "a@a.com",
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                false,
                null,
                BigDecimal.TEN,
                List.of()
        );

        when(bookingService.bookTicket(1L, req)).thenReturn(resp);

        var result = controller.bookTicket(1L, req);

        assertEquals(201, result.getStatusCodeValue());
    }

    @Test
    void getStatus_ok() {
        when(bookingService.getStatus("X")).thenReturn("Confirmed");

        var response = controller.getStatus("X");

        assertEquals("Confirmed", response.getBody());
    }

    @Test
    void calcFare_ok() {
        when(bookingService.calculateFare(1L, 2)).thenReturn(BigDecimal.TEN);

        var response = controller.calculateFare(1L, 2);

        assertEquals(BigDecimal.TEN, response.getBody());
    }
}

