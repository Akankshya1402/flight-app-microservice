package com.flightapp.bookingservice.service;

import com.flightapp.bookingservice.dto.BookingRequest;
import com.flightapp.bookingservice.dto.TicketResponse;

import java.math.BigDecimal;
import java.util.List;

public interface BookingService {

    TicketResponse bookTicket(Long flightId, BookingRequest request);

    TicketResponse getTicketByPnr(String pnr);

    List<TicketResponse> getBookingHistory(String email);

    TicketResponse cancelBooking(String pnr);

    // NEW
    BigDecimal calculateFare(Long flightId, int seats);

    // NEW
    String getStatus(String pnr);
}
