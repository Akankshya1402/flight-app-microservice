package com.flightapp.flightservice.mapper;

import com.flightapp.flightservice.dto.AddInventoryRequest;
import com.flightapp.flightservice.dto.FlightSummary;
import com.flightapp.flightservice.entity.Flight;

import java.math.BigDecimal;

public final class FlightMapper {

    private FlightMapper() {}

    public static Flight toEntity(AddInventoryRequest request) {
        return new Flight.Builder()
                .airlineCode(request.airlineCode())
                .airlineName(request.airlineName())
                .logoUrl(request.logoUrl())
                .fromCity(request.fromCity())
                .toCity(request.toCity())
                .departureTime(request.departureTime())
                .arrivalTime(request.arrivalTime())
                .roundTrip(request.roundTrip())
                .availableSeats(request.totalSeats())
                .priceOneWay(request.priceOneWay())
                .priceRoundTrip(
                        request.priceRoundTrip() != null
                                ? request.priceRoundTrip()
                                : request.priceOneWay().multiply(BigDecimal.valueOf(2))
                )
                .build();
    }

    public static FlightSummary toSummary(Flight flight) {
        return new FlightSummary(
                flight.getId(),
                flight.getAirlineName(),
                flight.getLogoUrl(),
                flight.getFromCity(),
                flight.getToCity(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.isRoundTrip(),
                flight.getPriceOneWay(),
                flight.getPriceRoundTrip(),
                flight.getAvailableSeats()
        );
    }
}
