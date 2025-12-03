package com.flightapp.flightservice.service.impl;

import com.flightapp.flightservice.dto.FlightSearchRequest;
import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

class FlightServiceCircuitBreakerTest {

    @Test
    void fallback_returnsEmptyFlux() {
        FlightServiceImpl service = new FlightServiceImpl(null);

        FlightSearchRequest req = new FlightSearchRequest(
                "A", "B",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                false
        );

        Flux<?> flux = service.searchFlightsFallback(req, 0, 10, "price", "asc",
                BigDecimal.TEN, "Air", new RuntimeException("x"));

        StepVerifier.create(flux).verifyComplete();
    }
}
