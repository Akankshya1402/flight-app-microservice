package com.flightapp.flightservice.service.impl;

import com.flightapp.flightservice.dto.AddInventoryRequest;
import com.flightapp.flightservice.dto.FlightSearchRequest;
import com.flightapp.flightservice.dto.FlightSummary;
import com.flightapp.flightservice.entity.Flight;
import com.flightapp.flightservice.exception.FlightNotFoundException;
import com.flightapp.flightservice.mapper.FlightMapper;
import com.flightapp.flightservice.repository.FlightRepository;
import com.flightapp.flightservice.service.FlightService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Comparator;

@Service
public class FlightServiceImpl implements FlightService {

    private static final Logger log = LoggerFactory.getLogger(FlightServiceImpl.class);
    private static final String FLIGHT_SEARCH_CB = "flightSearchCB";

    private final FlightRepository flightRepository;

    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Mono<FlightSummary> addInventory(AddInventoryRequest request) {
        log.info("Adding inventory for airline={} from={} to={}",
                request.airlineName(), request.fromCity(), request.toCity());

        Flight entity = FlightMapper.toEntity(request);
        return flightRepository.save(entity)
                .map(FlightMapper::toSummary);
    }

    @Override
    @Cacheable(
            cacheNames = "flightSearch",
            key = "#request.fromCity() + '-' + #request.toCity() + '-' + " +
                   "#request.departureFrom() + '-' + #request.departureTo() + '-' + " +
                   "#page + '-' + #size + '-' + #sortBy + '-' + #sortDir + '-' + " +
                   "#maxPrice + '-' + #airlineName"
    )
    @CircuitBreaker(name = FLIGHT_SEARCH_CB, fallbackMethod = "searchFlightsFallback")
    public Flux<FlightSummary> searchFlights(
            FlightSearchRequest request,
            int page,
            int size,
            String sortBy,
            String sortDir,
            BigDecimal maxPrice,
            String airlineName
    ) {
        log.info("Searching flights from={} to={} page={} size={} sortBy={} sortDir={} maxPrice={} airlineName={}",
                request.fromCity(), request.toCity(), page, size, sortBy, sortDir, maxPrice, airlineName);

        return flightRepository
                .findByFromCityAndToCityAndDepartureTimeBetween(
                        request.fromCity(),
                        request.toCity(),
                        request.departureFrom(),
                        request.departureTo()
                )
                .filter(flight -> airlineName == null
                        || flight.getAirlineName().equalsIgnoreCase(airlineName))
                .filter(flight -> maxPrice == null
                        || (flight.getPriceOneWay() != null
                        && flight.getPriceOneWay().compareTo(maxPrice) <= 0))
                .map(FlightMapper::toSummary)
                .sort(getComparator(sortBy, sortDir))
                .skip((long) page * size)
                .take(size);
    }

    @SuppressWarnings("unused")
    public Flux<FlightSummary> searchFlightsFallback(
            FlightSearchRequest request,
            int page,
            int size,
            String sortBy,
            String sortDir,
            BigDecimal maxPrice,
            String airlineName,
            Throwable throwable
    ) {
        log.error("Flight search fallback triggered due to: {}", throwable.getMessage(), throwable);
        return Flux.empty();
    }

    private Comparator<FlightSummary> getComparator(String sortBy, String sortDir) {
        Comparator<FlightSummary> comparator;
        switch (sortBy) {
            case "price":
            case "priceOneWay":
                comparator = Comparator.comparing(
                        FlightSummary::priceOneWay,
                        Comparator.nullsLast(BigDecimal::compareTo)
                );
                break;
            case "airlineName":
                comparator = Comparator.comparing(
                        FlightSummary::airlineName,
                        Comparator.nullsLast(String::compareToIgnoreCase)
                );
                break;
            case "departureTime":
            default:
                comparator = Comparator.comparing(FlightSummary::departureTime);
        }
        if ("desc".equalsIgnoreCase(sortDir)) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    @Override
    public Mono<FlightSummary> getFlightById(Long id) {
        log.info("Fetching flight by id={}", id);

        return flightRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new FlightNotFoundException("Flight not found with id: " + id)
                ))
                .map(FlightMapper::toSummary);
    }

    @Override
    public Flux<FlightSummary> getAllFlights(int page, int size) {
        log.info("Fetching all flights page={} size={}", page, size);

        return flightRepository.findAll()
                .map(FlightMapper::toSummary)
                .sort(Comparator.comparing(FlightSummary::departureTime))
                .skip((long) page * size)
                .take(size);
    }

    @Override
    public Mono<Integer> getAvailableSeats(Long id) {
        log.info("Fetching available seats for flight id={}", id);

        return getFlightById(id)
                .map(FlightSummary::availableSeats);
    }
}

