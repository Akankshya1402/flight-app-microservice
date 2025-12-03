package com.flightapp.flightservice.controller;

import com.flightapp.flightservice.dto.AddInventoryRequest;
import com.flightapp.flightservice.dto.FlightSearchRequest;
import com.flightapp.flightservice.dto.FlightSummary;
import com.flightapp.flightservice.service.FlightService;
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

class FlightControllerTest {

    @Mock
    private FlightService service;

    @InjectMocks
    private FlightController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addInventory_created() {
        LocalDateTime t = LocalDateTime.now().plusDays(1);

        AddInventoryRequest req = new AddInventoryRequest(
                "AC", "Air", null, "A", "B",
                t, t.plusHours(1), false, 50,
                BigDecimal.TEN, BigDecimal.ONE
        );

        FlightSummary fs = new FlightSummary(
                1L, "Air", null, "A", "B", t, t.plusHours(1),
                false, BigDecimal.TEN, BigDecimal.ONE, 50
        );

        when(service.addInventory(req)).thenReturn(Mono.just(fs));

        StepVerifier.create(controller.addInventory(req))
                .assertNext(res -> {
                    assertEquals(201, res.getStatusCode().value());
                    assertEquals("Air", res.getBody().airlineName());
                })
                .verifyComplete();
    }

    @Test
    void searchFlights_ok() {
        LocalDateTime t = LocalDateTime.now().plusDays(1);
        FlightSearchRequest req = new FlightSearchRequest("A", "B", t, t.plusHours(1), false);

        when(service.searchFlights(any(), anyInt(), anyInt(), anyString(), anyString(), any(), any()))
                .thenReturn(Flux.just(
                        new FlightSummary(1L, "Air", null, "A", "B", t, t.plusHours(1),
                                false, BigDecimal.TEN, BigDecimal.ONE, 10)
                ));

        StepVerifier.create(controller.searchFlights(req, 0, 10, "departureTime", "asc", null, null))
                .assertNext(fs -> assertEquals("A", fs.fromCity()))
                .verifyComplete();
    }

    @Test
    void getFlightById_ok() {
        FlightSummary fs = new FlightSummary(
                1L, "Air", null, "A", "B",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                false, BigDecimal.TEN, BigDecimal.ONE, 10
        );

        when(service.getFlightById(1L)).thenReturn(Mono.just(fs));

        StepVerifier.create(controller.getFlightById(1L))
                .assertNext(r -> assertEquals("Air", r.airlineName()))
                .verifyComplete();
    }
}

