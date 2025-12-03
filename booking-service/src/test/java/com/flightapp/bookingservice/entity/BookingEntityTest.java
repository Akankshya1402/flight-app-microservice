package com.flightapp.bookingservice.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingEntityTest {

    @Test
    void builder_populatesAllFields_andPassengers() {
        LocalDateTime journeyDate = LocalDateTime.now().plusDays(3);
        LocalDateTime bookingTime = LocalDateTime.now();
        LocalDateTime cancellationTime = bookingTime.plusHours(1);

        Booking booking = new Booking.Builder()
                .id(1L)
                .pnr("PNR-12345678")
                .flightId(10L)
                .airlineName("Test Airline")
                .fromCity("CITY_A")
                .toCity("CITY_B")
                .customerName("John Doe")
                .email("john@example.com")
                .numberOfSeats(2)
                .mealRequired(true)
                .journeyDate(journeyDate)
                .bookingTime(bookingTime)
                .cancelled(true)
                .cancellationTime(cancellationTime)
                .totalAmount(new BigDecimal("1234.50"))
                .build();

        Passenger p1 = new Passenger(booking, "P1", "M", 25, "A1");
        Passenger p2 = new Passenger(booking, "P2", "F", 23, "A2");

        booking.setPassengers(List.of(p1, p2));

        assertEquals("PNR-12345678", booking.getPnr());
        assertEquals(10L, booking.getFlightId());
        assertEquals("Test Airline", booking.getAirlineName());
        assertEquals("CITY_A", booking.getFromCity());
        assertEquals("CITY_B", booking.getToCity());
        assertEquals("John Doe", booking.getCustomerName());
        assertEquals("john@example.com", booking.getEmail());
        assertEquals(2, booking.getNumberOfSeats());
        assertTrue(booking.isMealRequired());
        assertEquals(journeyDate, booking.getJourneyDate());
        assertEquals(bookingTime, booking.getBookingTime());
        assertTrue(booking.isCancelled());
        assertEquals(cancellationTime, booking.getCancellationTime());
        assertEquals(new BigDecimal("1234.50"), booking.getTotalAmount());
        assertEquals(2, booking.getPassengers().size());
        assertSame(booking, booking.getPassengers().get(0).getBooking());
    }

    @Test
    void setters_updateFields() {
        Booking booking = new Booking();

        LocalDateTime jd = LocalDateTime.now();
        LocalDateTime bt = LocalDateTime.now();
        LocalDateTime ct = bt.plusHours(2);

        booking.setPnr("PNR-AAAA1111");
        booking.setFlightId(99L);
        booking.setAirlineName("Another Airline");
        booking.setFromCity("X");
        booking.setToCity("Y");
        booking.setCustomerName("Jane");
        booking.setEmail("jane@example.com");
        booking.setNumberOfSeats(1);
        booking.setMealRequired(false);
        booking.setJourneyDate(jd);
        booking.setBookingTime(bt);
        booking.setCancelled(true);
        booking.setCancellationTime(ct);
        booking.setTotalAmount(new BigDecimal("500.00"));

        assertEquals("PNR-AAAA1111", booking.getPnr());
        assertEquals(99L, booking.getFlightId());
        assertEquals("Another Airline", booking.getAirlineName());
        assertEquals("X", booking.getFromCity());
        assertEquals("Y", booking.getToCity());
        assertEquals("Jane", booking.getCustomerName());
        assertEquals("jane@example.com", booking.getEmail());
        assertEquals(1, booking.getNumberOfSeats());
        assertFalse(booking.isMealRequired());
        assertEquals(jd, booking.getJourneyDate());
        assertEquals(bt, booking.getBookingTime());
        assertTrue(booking.isCancelled());
        assertEquals(ct, booking.getCancellationTime());
        assertEquals(new BigDecimal("500.00"), booking.getTotalAmount());
    }
}

