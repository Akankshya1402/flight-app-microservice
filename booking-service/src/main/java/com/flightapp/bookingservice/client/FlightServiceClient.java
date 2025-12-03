package com.flightapp.bookingservice.client;

import com.flightapp.bookingservice.client.dto.FlightSummary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "flight-service",
        path = "/api/flight"
)
public interface FlightServiceClient {

    @GetMapping("/internal/{id}")
    FlightSummary getFlight(@PathVariable("id") Long id);
}

