package com.flightapp.flightservice.service.impl;

import com.flightapp.flightservice.dto.AddInventoryRequest;
import com.flightapp.flightservice.dto.FlightSearchRequest;
import com.flightapp.flightservice.dto.FlightSummary;
import com.flightapp.flightservice.entity.Flight;
import com.flightapp.flightservice.exception.FlightNotFoundException;
import com.flightapp.flightservice.mapper.FlightMapper;
import com.flightapp.flightservice.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlightServiceImplTest {

    @Mock
    private FlightRepository repo;

    @InjectMocks
    private FlightServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addInventory_success() {
        LocalDateTime t = LocalDateTime.now().plusDays(1);
        AddInventoryRequest req = new AddInventoryRequest(
                "A", "Air", null, "A", "B", t, t.plusHours(1),
                false, 100, BigDecimal.TEN, null);

        Flight saved = FlightMapper.toEntity(req);

        when(repo.save(any())).thenReturn(Mono.just(saved));

        StepVerifier.create(service.addInventory(req))
                .assertNext(r -> assertEquals("Air", r.airlineName()))
                .verifyComplete();
    }

    @Test
    void searchFlights_success() {
        LocalDateTime t = LocalDateTime.now().plusDays(1);

        Flight f = new Flight.Builder()
                .id(1L)
                .airlineName("Air")
                .fromCity("A")
                .toCity("B")
                .departureTime(t)
                .arrivalTime(t.plusHours(1))
                .availableSeats(50)
                .priceOneWay(BigDecimal.TEN)
                .build();

        when(repo.findByFromCityAndToCityAndDepartureTimeBetween(
                any(), any(), any(), any())).thenReturn(Flux.just(f));

        FlightSearchRequest req = new FlightSearchRequest("A", "B", t.minusHours(1), t.plusHours(1), false);

        StepVerifier.create(service.searchFlights(req, 0, 10, "departureTime", "asc", null, null))
                .assertNext(fs -> assertEquals("A", fs.fromCity()))
                .verifyComplete();
    }

    @Test
    void searchFlights_fallback_onException() {
        LocalDateTime t = LocalDateTime.now().plusDays(1);

        when(repo.findByFromCityAndToCityAndDepartureTimeBetween(any(), any(), any(), any()))
                .thenThrow(new RuntimeException("down"));

        FlightSearchRequest req = new FlightSearchRequest("A", "B", t.minusHours(1), t.plusHours(1), false);

        Flux<FlightSummary> flux =
                service.searchFlightsFallback(req, 0, 10, "price", "desc", null, null, new RuntimeException("x"));

        StepVerifier.create(flux).verifyComplete();
    }

    @Test
    void getFlightById_found() {
        Flight f = new Flight.Builder()
                .id(1L)
                .airlineName("Air")
                .build();

        when(repo.findById(1L)).thenReturn(Mono.just(f));

        StepVerifier.create(service.getFlightById(1L))
                .assertNext(fs -> assertEquals("Air", fs.airlineName()))
                .verifyComplete();
    }

    @Test
    void getFlightById_notFound() {
        when(repo.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(service.getFlightById(1L))
                .expectError(FlightNotFoundException.class)
                .verify();
    }

    @Test
    void getAvailableSeats_success() {
        Flight f = new Flight.Builder()
                .availableSeats(40)
                .build();

        when(repo.findById(1L)).thenReturn(Mono.just(f));

        StepVerifier.create(service.getAvailableSeats(1L))
                .assertNext(seats -> assertEquals(40, seats))
                .verifyComplete();
    }
}

