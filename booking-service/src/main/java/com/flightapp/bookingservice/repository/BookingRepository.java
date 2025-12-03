package com.flightapp.bookingservice.repository;

import com.flightapp.bookingservice.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByPnr(String pnr);

    List<Booking> findByEmailIgnoreCaseOrderByBookingTimeDesc(String email);
}
