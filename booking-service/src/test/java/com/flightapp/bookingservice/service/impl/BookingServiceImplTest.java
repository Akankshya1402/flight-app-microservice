package com.flightapp.bookingservice.service.impl;

import com.flightapp.bookingservice.client.FlightServiceClient;
import com.flightapp.bookingservice.client.dto.FlightSummary;
import com.flightapp.bookingservice.dto.BookingRequest;
import com.flightapp.bookingservice.dto.PassengerDto;
import com.flightapp.bookingservice.dto.TicketResponse;
import com.flightapp.bookingservice.entity.Booking;
import com.flightapp.bookingservice.entity.Passenger;
import com.flightapp.bookingservice.exception.BookingNotAllowedException;
import com.flightapp.bookingservice.exception.BookingNotFoundException;
import com.flightapp.bookingservice.messaging.EmailMessage;
import com.flightapp.bookingservice.messaging.EmailProducer;
import com.flightapp.bookingservice.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FlightServiceClient flightServiceClient;

    @Mock
    private EmailProducer emailProducer;

    @InjectMocks
    private BookingServiceImpl service;

    private FlightSummary sampleFlight;
    private BookingRequest sampleRequest;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        sampleFlight = new FlightSummary(
                1L, "Airline", "logo.png", "DEL", "BLR",
                LocalDateTime.now(), LocalDateTime.now().plusHours(2),
                false, BigDecimal.TEN, BigDecimal.valueOf(15),
                100
        );

        sampleRequest = new BookingRequest(
                "John",
                "john@example.com",
                1,
                List.of(new PassengerDto("A", "M", 22, "1A")),
                false,
                LocalDateTime.now().plusDays(2)
        );
    }

    // ---------------------- BOOK -----------------------------
    @Test
    void bookTicket_success() {
        when(flightServiceClient.getFlight(1L)).thenReturn(sampleFlight);
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TicketResponse response = service.bookTicket(1L, sampleRequest);

        assertNotNull(response);
        assertEquals("John", response.customerName());
        verify(emailProducer, times(1)).sendBookingEmail(any(EmailMessage.class));
    }

    @Test
    void bookTicket_notEnoughSeats_throws() {
        FlightSummary f = new FlightSummary(
                1L, "Air", null, "A", "B",
                LocalDateTime.now(), LocalDateTime.now(),
                false, BigDecimal.ONE, BigDecimal.ONE,
                0
        );

        when(flightServiceClient.getFlight(1L)).thenReturn(f);

        assertThrows(IllegalArgumentException.class,
                () -> service.bookTicket(1L, sampleRequest));
    }

    // ---------------------- FARE ------------------------------
    @Test
    void calculateFare_success() {
        when(flightServiceClient.getFlight(1L)).thenReturn(sampleFlight);
        BigDecimal fare = service.calculateFare(1L, 3);
        assertEquals(BigDecimal.valueOf(30), fare);
    }

    @Test
    void calculateFare_invalidSeats() {
        assertThrows(IllegalArgumentException.class,
                () -> service.calculateFare(1L, 0));
    }

    // ---------------------- TICKET GET ------------------------
    @Test
    void getTicket_notFound() {
        when(bookingRepository.findByPnr("X")).thenReturn(Optional.empty());
        assertThrows(BookingNotFoundException.class,
                () -> service.getTicketByPnr("X"));
    }

    @Test
    void getTicket_success() {
        Booking b = buildSampleBooking();
        when(bookingRepository.findByPnr("PNR")).thenReturn(Optional.of(b));

        TicketResponse r = service.getTicketByPnr("PNR");

        assertEquals("PNR-TEST", r.pnr());
    }

    // ---------------------- STATUS ----------------------------
    @Test
    void getStatus_notFound() {
        when(bookingRepository.findByPnr("X")).thenReturn(Optional.empty());
        assertThrows(BookingNotFoundException.class,
                () -> service.getStatus("X"));
    }

    @Test
    void getStatus_success() {
        Booking b = buildSampleBooking();
        when(bookingRepository.findByPnr("PNR")).thenReturn(Optional.of(b));

        String s = service.getStatus("PNR");
        assertEquals("Confirmed", s);
    }

    // ---------------------- CANCEL ----------------------------
    @Test
    void cancel_notFound() {
        when(bookingRepository.findByPnr("X")).thenReturn(Optional.empty());
        assertThrows(BookingNotFoundException.class,
                () -> service.cancelBooking("X"));
    }

    @Test
    void cancel_afterCutoff_throws() {
        Booking b = buildSampleBooking();
        b.setJourneyDate(LocalDateTime.now().plusHours(10)); // too close

        when(bookingRepository.findByPnr("PNR")).thenReturn(Optional.of(b));

        assertThrows(BookingNotAllowedException.class,
                () -> service.cancelBooking("PNR"));
    }

    @Test
    void cancel_success() {
        Booking b = buildSampleBooking();
        b.setJourneyDate(LocalDateTime.now().plusDays(3));

        when(bookingRepository.findByPnr("PNR")).thenReturn(Optional.of(b));
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TicketResponse r = service.cancelBooking("PNR");

        assertTrue(r.cancelled());
        verify(emailProducer, times(1)).sendBookingEmail(any());
    }

    // ---------------------- HISTORY ---------------------------
    @Test
    void history_success() {
        Booking b = buildSampleBooking();
        when(bookingRepository.findByEmailIgnoreCaseOrderByBookingTimeDesc("john@example.com"))
                .thenReturn(List.of(b));

        List<TicketResponse> list = service.getBookingHistory("john@example.com");

        assertEquals(1, list.size());
    }

    // ---------------------- FALLBACK ---------------------------
    @Test
    void fallback_triggersException() {
        assertThrows(BookingNotAllowedException.class,
                () -> service.flightFallback(1L, new RuntimeException("down")));
    }

    // ---------------------- HELPERS ----------------------------
    private Booking buildSampleBooking() {
        Booking b = new Booking.Builder()
                .id(1L)
                .pnr("PNR-TEST")
                .flightId(1L)
                .airlineName("Airline")
                .fromCity("DEL")
                .toCity("BLR")
                .customerName("John")
                .email("john@example.com")
                .numberOfSeats(1)
                .mealRequired(false)
                .journeyDate(LocalDateTime.now().plusDays(5))
                .bookingTime(LocalDateTime.now())
                .cancelled(false)
                .totalAmount(BigDecimal.TEN)
                .build();

        Passenger p = new Passenger(b, "A", "M", 22, "1A");
        b.setPassengers(List.of(p));
        return b;
    }
}




