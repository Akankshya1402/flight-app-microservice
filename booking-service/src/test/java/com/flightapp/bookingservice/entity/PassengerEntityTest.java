package com.flightapp.bookingservice.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PassengerEntityTest {

    @Test
    void constructor_andGetters_work() {
        Booking booking = new Booking.Builder()
                .pnr("PNR")
                .flightId(1L)
                .airlineName("Air")
                .fromCity("A")
                .toCity("B")
                .customerName("Cust")
                .email("c@example.com")
                .numberOfSeats(1)
                .mealRequired(false)
                .journeyDate(LocalDateTime.now())
                .bookingTime(LocalDateTime.now())
                .cancelled(false)
                .totalAmount(BigDecimal.TEN)
                .build();

        Passenger passenger = new Passenger(booking, "Name", "M", 30, "S1");

        assertEquals("Name", passenger.getName());
        assertEquals("M", passenger.getGender());
        assertEquals(30, passenger.getAge());
        assertEquals("S1", passenger.getSeatNumber());
        assertSame(booking, passenger.getBooking());
    }

    @Test
    void setters_work() {
        Passenger passenger = new Passenger();
        Booking booking = new Booking();

        passenger.setBooking(booking);
        passenger.setName("P");
        passenger.setGender("F");
        passenger.setAge(22);
        passenger.setSeatNumber("B2");

        assertSame(booking, passenger.getBooking());
        assertEquals("P", passenger.getName());
        assertEquals("F", passenger.getGender());
        assertEquals(22, passenger.getAge());
        assertEquals("B2", passenger.getSeatNumber());
    }
}
