package com.flightapp.bookingservice.service.impl;

import com.flightapp.bookingservice.client.FlightServiceClient;
import com.flightapp.bookingservice.client.dto.FlightSummary;
import com.flightapp.bookingservice.dto.BookingRequest;
import com.flightapp.bookingservice.dto.PassengerDto;
import com.flightapp.bookingservice.dto.TicketResponse;
import com.flightapp.bookingservice.entity.Booking;
import com.flightapp.bookingservice.entity.Passenger;
import com.flightapp.bookingservice.exception.BookingNotAllowedException;
import com.flightapp.bookingservice.exception.BookingNotFoundException;
import com.flightapp.bookingservice.messaging.EmailMessage;
import com.flightapp.bookingservice.messaging.EmailProducer;
import com.flightapp.bookingservice.repository.BookingRepository;
import com.flightapp.bookingservice.service.BookingService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);
    private static final String FLIGHT_CLIENT_CB = "flightServiceClient";

    private final BookingRepository bookingRepository;
    private final FlightServiceClient flightServiceClient;
    private final EmailProducer emailProducer;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            FlightServiceClient flightServiceClient,
            EmailProducer emailProducer
    ) {
        this.bookingRepository = bookingRepository;
        this.flightServiceClient = flightServiceClient;
        this.emailProducer = emailProducer;
    }

    // ------------------------------------------------------------
    // BOOK TICKET
    // ------------------------------------------------------------
    @Override
    public TicketResponse bookTicket(Long flightId, BookingRequest request) {

        if (!request.numberOfSeats().equals(request.passengers().size())) {
            throw new IllegalArgumentException("numberOfSeats must match passenger count");
        }

        // Fetch flight from Flight-Service (with circuit breaker)
        FlightSummary flight = getFlightWithCircuitBreaker(flightId);

        if (flight.availableSeats() < request.numberOfSeats()) {
            throw new IllegalArgumentException("Not enough seats available");
        }

        String pnr = generatePnr();
        LocalDateTime now = LocalDateTime.now();

        BigDecimal pricePerSeat = flight.priceOneWay();
        BigDecimal totalAmount = pricePerSeat.multiply(BigDecimal.valueOf(request.numberOfSeats()));

        Booking booking = new Booking.Builder()
                .pnr(pnr)
                .flightId(flightId)
                .airlineName(flight.airlineName())
                .fromCity(flight.fromCity())
                .toCity(flight.toCity())
                .customerName(request.customerName())
                .email(request.email())
                .numberOfSeats(request.numberOfSeats())
                .mealRequired(request.mealRequired())
                .journeyDate(request.journeyDate())
                .bookingTime(now)
                .cancelled(false)
                .totalAmount(totalAmount)
                .build();

        // Passenger entities
        List<Passenger> passengers = request.passengers().stream()
                .map(p -> new Passenger(booking, p.name(), p.gender(), p.age(), p.seatNumber()))
                .collect(Collectors.toList());
        booking.setPassengers(passengers);

        bookingRepository.save(booking);

        // 🔔 Send RabbitMQ email notification
        sendBookingEmail(booking);

        return toTicketResponse(booking);
    }

    // ------------------------------------------------------------
    // CIRCUIT BREAKER CALL TO FLIGHT-SERVICE
    // ------------------------------------------------------------
    @CircuitBreaker(name = FLIGHT_CLIENT_CB, fallbackMethod = "flightFallback")
    protected FlightSummary getFlightWithCircuitBreaker(Long flightId) {
        log.info("Calling Flight-Service for id={}", flightId);
        return flightServiceClient.getFlight(flightId);
    }

    @SuppressWarnings("unused")
    protected FlightSummary flightFallback(Long id, Throwable throwable) {
        log.error("Flight-Service unavailable for id={} : {}", id, throwable.getMessage());
        throw new BookingNotAllowedException("Flight service unavailable. Please try later.");
    }

    // ------------------------------------------------------------
    // GET TICKET BY PNR
    // ------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public TicketResponse getTicketByPnr(String pnr) {
        Booking booking = bookingRepository.findByPnr(pnr)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found for PNR: " + pnr));

        return toTicketResponse(booking);
    }

    // ------------------------------------------------------------
    // BOOKING HISTORY
    // ------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<TicketResponse> getBookingHistory(String email) {
        return bookingRepository.findByEmailIgnoreCaseOrderByBookingTimeDesc(email)
                .stream()
                .map(this::toTicketResponse)
                .collect(Collectors.toList());
    }

    // ------------------------------------------------------------
    // CANCEL BOOKING
    // ------------------------------------------------------------
    @Override
    public TicketResponse cancelBooking(String pnr) {
        Booking booking = bookingRepository.findByPnr(pnr)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found for PNR: " + pnr));

        if (booking.isCancelled()) {
            return toTicketResponse(booking);
        }

        LocalDateTime now = LocalDateTime.now();

        if (booking.getJourneyDate().minusHours(24).isBefore(now)) {
            throw new BookingNotAllowedException("Cancellation allowed only 24 hours before journey.");
        }

        booking.setCancelled(true);
        booking.setCancellationTime(now);
        bookingRepository.save(booking);

        sendCancellationEmail(booking);

        return toTicketResponse(booking);
    }

    // ------------------------------------------------------------
    // CALCULATE FARE BEFORE BOOKING
    // ------------------------------------------------------------
    @Override
    public BigDecimal calculateFare(Long flightId, int seats) {
        if (seats <= 0) {
            throw new IllegalArgumentException("Seats must be > 0");
        }

        FlightSummary flight = getFlightWithCircuitBreaker(flightId);

        if (flight.priceOneWay() == null) {
            throw new IllegalArgumentException("Price unavailable for this flight");
        }

        return flight.priceOneWay().multiply(BigDecimal.valueOf(seats));
    }

    // ------------------------------------------------------------
    // GET BOOKING STATUS
    // ------------------------------------------------------------
    @Override
    public String getStatus(String pnr) {
        Booking booking = bookingRepository.findByPnr(pnr)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found for PNR: " + pnr));

        return booking.isCancelled() ? "Cancelled" : "Confirmed";
    }

    // ------------------------------------------------------------
    // HELPERS
    // ------------------------------------------------------------
    private TicketResponse toTicketResponse(Booking booking) {
        List<PassengerDto> passengers = booking.getPassengers().stream()
                .map(p -> new PassengerDto(p.getName(), p.getGender(), p.getAge(), p.getSeatNumber()))
                .collect(Collectors.toList());

        return new TicketResponse(
                booking.getPnr(),
                booking.getFlightId(),
                booking.getAirlineName(),
                booking.getFromCity(),
                booking.getToCity(),
                booking.getCustomerName(),
                booking.getEmail(),
                booking.isMealRequired(),
                booking.getJourneyDate(),
                booking.getBookingTime(),
                booking.isCancelled(),
                booking.getCancellationTime(),
                booking.getTotalAmount(),
                passengers
        );
    }

    private void sendBookingEmail(Booking booking) {
        String subject = "Flight Booking Confirmed - PNR " + booking.getPnr();
        String body = "Dear " + booking.getCustomerName() + ",\n\n"
                + "Your booking is confirmed.\n"
                + "PNR: " + booking.getPnr() + "\n"
                + "Flight: " + booking.getFromCity() + " -> " + booking.getToCity() + "\n"
                + "Journey Date: " + booking.getJourneyDate() + "\n"
                + "Seats: " + booking.getNumberOfSeats() + "\n"
                + "Total: " + booking.getTotalAmount() + "\n\n"
                + "Thank you.";

        emailProducer.sendBookingEmail(new EmailMessage(
                booking.getEmail(), subject, body
        ));
    }

    private void sendCancellationEmail(Booking booking) {
        String subject = "Flight Booking Cancelled - PNR " + booking.getPnr();
        String body = "Dear " + booking.getCustomerName() + ",\n\n"
                + "Your ticket has been cancelled.\n"
                + "PNR: " + booking.getPnr() + "\n"
                + "Flight: " + booking.getFromCity() + " -> " + booking.getToCity() + "\n"
                + "Journey Date: " + booking.getJourneyDate() + "\n\n"
                + "We hope to serve you again.";

        emailProducer.sendBookingEmail(new EmailMessage(
                booking.getEmail(), subject, body
        ));
    }

    private String generatePnr() {
        return "PNR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
