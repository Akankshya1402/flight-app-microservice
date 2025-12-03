package com.flightapp.flightservice.repository;

import com.flightapp.flightservice.entity.Flight;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface FlightRepository extends ReactiveCrudRepository<Flight, Long> {

    Flux<Flight> findByFromCityAndToCityAndDepartureTimeBetween(
            String fromCity,
            String toCity,
            LocalDateTime start,
            LocalDateTime end
    );
}

