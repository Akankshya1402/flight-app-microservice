package com.flightapp.flightservice.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FlightEntityTest {

    @Test
    void builder_populatesAllFields() {
        LocalDateTime dep = LocalDateTime.now().plusDays(1);
        LocalDateTime arr = dep.plusHours(2);

        Flight flight = new Flight.Builder()
                .id(1L)
                .airlineCode("AI")
                .airlineName("Air India")
                .logoUrl("logo.png")
                .fromCity("DEL")
                .toCity("BLR")
                .departureTime(dep)
                .arrivalTime(arr)
                .roundTrip(true)
                .availableSeats(100)
                .priceOneWay(BigDecimal.valueOf(5000))
                .priceRoundTrip(BigDecimal.valueOf(9000))
                .build();

        assertEquals(1L, flight.getId());
        assertEquals("AI", flight.getAirlineCode());
        assertEquals("Air India", flight.getAirlineName());
        assertEquals("logo.png", flight.getLogoUrl());
        assertEquals("DEL", flight.getFromCity());
        assertEquals("BLR", flight.getToCity());
        assertEquals(dep, flight.getDepartureTime());
        assertEquals(arr, flight.getArrivalTime());
        assertTrue(flight.isRoundTrip());
        assertEquals(100, flight.getAvailableSeats());
        assertEquals(BigDecimal.valueOf(5000), flight.getPriceOneWay());
        assertEquals(BigDecimal.valueOf(9000), flight.getPriceRoundTrip());
    }

    @Test
    void setters_updateFields() {
        Flight flight = new Flight();

        LocalDateTime dep = LocalDateTime.now().plusDays(2);
        LocalDateTime arr = dep.plusHours(3);

        flight.setId(2L);
        flight.setAirlineCode("6E");
        flight.setAirlineName("IndiGo");
        flight.setLogoUrl("indigo.png");
        flight.setFromCity("HYD");
        flight.setToCity("MAA");
        flight.setDepartureTime(dep);
        flight.setArrivalTime(arr);
        flight.setRoundTrip(false);
        flight.setAvailableSeats(50);
        flight.setPriceOneWay(BigDecimal.valueOf(3000));
        flight.setPriceRoundTrip(BigDecimal.valueOf(5500));

        assertEquals(2L, flight.getId());
        assertEquals("6E", flight.getAirlineCode());
        assertEquals("IndiGo", flight.getAirlineName());
        assertEquals("indigo.png", flight.getLogoUrl());
        assertEquals("HYD", flight.getFromCity());
        assertEquals("MAA", flight.getToCity());
        assertEquals(dep, flight.getDepartureTime());
        assertEquals(arr, flight.getArrivalTime());
        assertFalse(flight.isRoundTrip());
        assertEquals(50, flight.getAvailableSeats());
        assertEquals(BigDecimal.valueOf(3000), flight.getPriceOneWay());
        assertEquals(BigDecimal.valueOf(5500), flight.getPriceRoundTrip());
    }
}
