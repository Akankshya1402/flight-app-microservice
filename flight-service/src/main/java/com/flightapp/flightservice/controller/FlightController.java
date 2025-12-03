package com.flightapp.flightservice.controller;

import com.flightapp.flightservice.dto.AddInventoryRequest;
import com.flightapp.flightservice.dto.FlightSearchRequest;
import com.flightapp.flightservice.dto.FlightSummary;
import com.flightapp.flightservice.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/flight")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping(value = "/airline/inventory", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<FlightSummary>> addInventory(
            @Valid @RequestBody AddInventoryRequest request) {

        return flightService.addInventory(request)
                .map(summary -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(summary));
    }

    @PostMapping("/search")
    public Flux<FlightSummary> searchFlights(
            @Valid @RequestBody FlightSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "departureTime") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String airlineName
    ) {
        return flightService.searchFlights(
                request, page, size, sortBy, sortDir, maxPrice, airlineName
        );
    }

    @GetMapping("/internal/{id}")
    public Mono<FlightSummary> getFlightInternal(@PathVariable Long id) {
        return flightService.getFlightById(id);
    }

    @GetMapping("/{id}")
    public Mono<FlightSummary> getFlightById(@PathVariable Long id) {
        return flightService.getFlightById(id);
    }

    @GetMapping("/all")
    public Flux<FlightSummary> getAllFlights(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return flightService.getAllFlights(page, size);
    }

    @GetMapping("/{id}/seats")
    public Mono<Integer> getAvailableSeats(@PathVariable Long id) {
        return flightService.getAvailableSeats(id);
    }
}


