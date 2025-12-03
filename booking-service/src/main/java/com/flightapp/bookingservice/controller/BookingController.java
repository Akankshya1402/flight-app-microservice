package com.flightapp.bookingservice.controller;

import com.flightapp.bookingservice.dto.BookingRequest;
import com.flightapp.bookingservice.dto.TicketResponse;
import com.flightapp.bookingservice.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // 201 CREATED
    @PostMapping("/{flightId}")
    public ResponseEntity<TicketResponse> bookTicket(
            @PathVariable Long flightId,
            @Valid @RequestBody BookingRequest request
    ) {
        TicketResponse response = bookingService.bookTicket(flightId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // BEFORE BOOKING — CALCULATE FARE
    @PostMapping("/calc-fare/{flightId}")
    public ResponseEntity<BigDecimal> calculateFare(
            @PathVariable Long flightId,
            @RequestParam int seats
    ) {
        BigDecimal fare = bookingService.calculateFare(flightId, seats);
        return ResponseEntity.ok(fare);
    }

    // CHECK STATUS
    @GetMapping("/status/{pnr}")
    public ResponseEntity<String> getStatus(@PathVariable String pnr) {
        return ResponseEntity.ok(bookingService.getStatus(pnr));
    }

    // GET TICKET BY PNR
    @GetMapping("/ticket/{pnr}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable String pnr) {
        return ResponseEntity.ok(bookingService.getTicketByPnr(pnr));
    }

    // BOOKING HISTORY
    @GetMapping("/history/{email}")
    public ResponseEntity<List<TicketResponse>> history(@PathVariable String email) {
        return ResponseEntity.ok(bookingService.getBookingHistory(email));
    }

    // 200
    @DeleteMapping("/cancel/{pnr}")
    public ResponseEntity<TicketResponse> cancel(@PathVariable String pnr) {
        TicketResponse response = bookingService.cancelBooking(pnr);
        return ResponseEntity.ok(response);
    }
}
