package com.flightapp.flightservice.mapper;

import com.flightapp.flightservice.dto.AddInventoryRequest;
import com.flightapp.flightservice.dto.FlightSummary;
import com.flightapp.flightservice.entity.Flight;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FlightMapperTest {

    @Test
    void toEntity_mapsCorrectly() {
        LocalDateTime now = LocalDateTime.now().plusDays(1);

        AddInventoryRequest req = new AddInventoryRequest(
                "AC",
                "X Air",
                "logo.png",
                "A",
                "B",
                now,
                now.plusHours(2),
                false,
                100,
                BigDecimal.TEN,
                null
        );

        Flight f = FlightMapper.toEntity(req);

        assertEquals("AC", f.getAirlineCode());
        assertEquals("X Air", f.getAirlineName());
        assertEquals("logo.png", f.getLogoUrl());
        assertEquals("A", f.getFromCity());
        assertEquals("B", f.getToCity());
        assertEquals(100, f.getAvailableSeats());
        assertEquals(BigDecimal.TEN, f.getPriceOneWay());
        assertEquals(BigDecimal.TEN.multiply(BigDecimal.valueOf(2)), f.getPriceRoundTrip());
    }

    @Test
    void toSummary_mapsCorrectly() {
        Flight f = new Flight.Builder()
                .id(1L)
                .airlineCode("AC")
                .airlineName("X Air")
                .logoUrl("lg.png")
                .fromCity("A")
                .toCity("B")
                .departureTime(LocalDateTime.now())
                .arrivalTime(LocalDateTime.now().plusHours(1))
                .roundTrip(false)
                .availableSeats(20)
                .priceOneWay(BigDecimal.ONE)
                .priceRoundTrip(BigDecimal.TEN)
                .build();

        FlightSummary fs = FlightMapper.toSummary(f);

        assertEquals("X Air", fs.airlineName());
        assertEquals(20, fs.availableSeats());
    }
}
