package com.flightapp.apigateway.fallback;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
public class GatewayFallbackController {

    @GetMapping("/fallback/flight")
    public ResponseEntity<Map<String, Object>> flightFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "timestamp", Instant.now(),
                        "service", "flight-service",
                        "message", "Flight Service is down. Please try again later."
                ));
    }

    @GetMapping("/fallback/booking")
    public ResponseEntity<Map<String, Object>> bookingFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "timestamp", Instant.now(),
                        "service", "booking-service",
                        "message", "Booking Service is down. Please try again later."
                ));
    }
}
