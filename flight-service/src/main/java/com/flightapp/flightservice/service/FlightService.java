package com.flightapp.flightservice.service;

import com.flightapp.flightservice.dto.AddInventoryRequest;
import com.flightapp.flightservice.dto.FlightSearchRequest;
import com.flightapp.flightservice.dto.FlightSummary;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface FlightService {

    Mono<FlightSummary> addInventory(AddInventoryRequest request);

    Flux<FlightSummary> searchFlights(
            FlightSearchRequest request,
            int page,
            int size,
            String sortBy,
            String sortDir,
            BigDecimal maxPrice,
            String airlineName
    );

    Mono<FlightSummary> getFlightById(Long id);

    Flux<FlightSummary> getAllFlights(int page, int size);

    Mono<Integer> getAvailableSeats(Long id);
}
