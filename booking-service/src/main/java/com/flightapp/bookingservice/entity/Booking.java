package com.flightapp.bookingservice.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String pnr;

    @Column(nullable = false)
    private Long flightId;

    @Column(nullable = false)
    private String airlineName;

    @Column(nullable = false)
    private String fromCity;

    @Column(nullable = false)
    private String toCity;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Integer numberOfSeats;

    @Column(nullable = false)
    private boolean mealRequired;

    @Column(nullable = false)
    private LocalDateTime journeyDate;

    @Column(nullable = false)
    private LocalDateTime bookingTime;

    private boolean cancelled;

    private LocalDateTime cancellationTime;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @OneToMany(
            mappedBy = "booking",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<Passenger> passengers = new ArrayList<>();

    public Booking() {}

    private Booking(Builder builder) {
        this.id = builder.id;
        this.pnr = builder.pnr;
        this.flightId = builder.flightId;
        this.airlineName = builder.airlineName;
        this.fromCity = builder.fromCity;
        this.toCity = builder.toCity;
        this.customerName = builder.customerName;
        this.email = builder.email;
        this.numberOfSeats = builder.numberOfSeats;
        this.mealRequired = builder.mealRequired;
        this.journeyDate = builder.journeyDate;
        this.bookingTime = builder.bookingTime;
        this.cancelled = builder.cancelled;
        this.cancellationTime = builder.cancellationTime;
        this.totalAmount = builder.totalAmount;
        this.passengers = builder.passengers;
    }

    // Builder (creational pattern)
    public static class Builder {
        private Long id;
        private String pnr;
        private Long flightId;
        private String airlineName;
        private String fromCity;
        private String toCity;
        private String customerName;
        private String email;
        private Integer numberOfSeats;
        private boolean mealRequired;
        private LocalDateTime journeyDate;
        private LocalDateTime bookingTime;
        private boolean cancelled;
        private LocalDateTime cancellationTime;
        private BigDecimal totalAmount;
        private List<Passenger> passengers = new ArrayList<>();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder pnr(String pnr) { this.pnr = pnr; return this; }
        public Builder flightId(Long flightId) { this.flightId = flightId; return this; }
        public Builder airlineName(String airlineName) { this.airlineName = airlineName; return this; }
        public Builder fromCity(String fromCity) { this.fromCity = fromCity; return this; }
        public Builder toCity(String toCity) { this.toCity = toCity; return this; }
        public Builder customerName(String customerName) { this.customerName = customerName; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder numberOfSeats(Integer numberOfSeats) { this.numberOfSeats = numberOfSeats; return this; }
        public Builder mealRequired(boolean mealRequired) { this.mealRequired = mealRequired; return this; }
        public Builder journeyDate(LocalDateTime journeyDate) { this.journeyDate = journeyDate; return this; }
        public Builder bookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; return this; }
        public Builder cancelled(boolean cancelled) { this.cancelled = cancelled; return this; }
        public Builder cancellationTime(LocalDateTime cancellationTime) { this.cancellationTime = cancellationTime; return this; }
        public Builder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder passengers(List<Passenger> passengers) { this.passengers = passengers; return this; }

        public Booking build() {
            return new Booking(this);
        }
    }

    // getters & setters (JPA needs them)
    public Long getId() { return id; }

    public String getPnr() { return pnr; }
    public void setPnr(String pnr) { this.pnr = pnr; }

    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }

    public String getAirlineName() { return airlineName; }
    public void setAirlineName(String airlineName) { this.airlineName = airlineName; }

    public String getFromCity() { return fromCity; }
    public void setFromCity(String fromCity) { this.fromCity = fromCity; }

    public String getToCity() { return toCity; }
    public void setToCity(String toCity) { this.toCity = toCity; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getNumberOfSeats() { return numberOfSeats; }
    public void setNumberOfSeats(Integer numberOfSeats) { this.numberOfSeats = numberOfSeats; }

    public boolean isMealRequired() { return mealRequired; }
    public void setMealRequired(boolean mealRequired) { this.mealRequired = mealRequired; }

    public LocalDateTime getJourneyDate() { return journeyDate; }
    public void setJourneyDate(LocalDateTime journeyDate) { this.journeyDate = journeyDate; }

    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }

    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    public LocalDateTime getCancellationTime() { return cancellationTime; }
    public void setCancellationTime(LocalDateTime cancellationTime) { this.cancellationTime = cancellationTime; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public List<Passenger> getPassengers() { return passengers; }
    public void setPassengers(List<Passenger> passengers) { this.passengers = passengers; }
}


