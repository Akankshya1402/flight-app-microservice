package com.flightapp.flightservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("flights")
public class Flight {

    @Id
    private Long id;

    private String airlineCode;
    private String airlineName;
    private String logoUrl;
    private String fromCity;
    private String toCity;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private boolean roundTrip;
    private Integer availableSeats;
    private BigDecimal priceOneWay;
    private BigDecimal priceRoundTrip;

    public Flight() {}

    private Flight(Builder builder) {
        this.id = builder.id;
        this.airlineCode = builder.airlineCode;
        this.airlineName = builder.airlineName;
        this.logoUrl = builder.logoUrl;
        this.fromCity = builder.fromCity;
        this.toCity = builder.toCity;
        this.departureTime = builder.departureTime;
        this.arrivalTime = builder.arrivalTime;
        this.roundTrip = builder.roundTrip;
        this.availableSeats = builder.availableSeats;
        this.priceOneWay = builder.priceOneWay;
        this.priceRoundTrip = builder.priceRoundTrip;
    }

    // ===========================
    // Builder Pattern
    // ===========================

    public static class Builder {

        private Long id;
        private String airlineCode;
        private String airlineName;
        private String logoUrl;
        private String fromCity;
        private String toCity;
        private LocalDateTime departureTime;
        private LocalDateTime arrivalTime;
        private boolean roundTrip;
        private Integer availableSeats;
        private BigDecimal priceOneWay;
        private BigDecimal priceRoundTrip;

        public Builder id(Long id) {
            this.id = id; return this;
        }

        public Builder airlineCode(String airlineCode) {
            this.airlineCode = airlineCode; return this;
        }

        public Builder airlineName(String airlineName) {
            this.airlineName = airlineName; return this;
        }

        public Builder logoUrl(String logoUrl) {
            this.logoUrl = logoUrl; return this;
        }

        public Builder fromCity(String fromCity) {
            this.fromCity = fromCity; return this;
        }

        public Builder toCity(String toCity) {
            this.toCity = toCity; return this;
        }

        public Builder departureTime(LocalDateTime departureTime) {
            this.departureTime = departureTime; return this;
        }

        public Builder arrivalTime(LocalDateTime arrivalTime) {
            this.arrivalTime = arrivalTime; return this;
        }

        public Builder roundTrip(boolean roundTrip) {
            this.roundTrip = roundTrip; return this;
        }

        public Builder availableSeats(Integer availableSeats) {
            this.availableSeats = availableSeats; return this;
        }

        public Builder priceOneWay(BigDecimal priceOneWay) {
            this.priceOneWay = priceOneWay; return this;
        }

        public Builder priceRoundTrip(BigDecimal priceRoundTrip) {
            this.priceRoundTrip = priceRoundTrip; return this;
        }

        public Flight build() {
            return new Flight(this);
        }
    }

    // ===========================
    // Getters
    // ===========================

    public Long getId() {
        return id;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getFromCity() {
        return fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public boolean isRoundTrip() {
        return roundTrip;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public BigDecimal getPriceOneWay() {
        return priceOneWay;
    }

    public BigDecimal getPriceRoundTrip() {
        return priceRoundTrip;
    }

    // ===========================
    // Setters (needed for R2DBC)
    // ===========================

    public void setId(Long id) {
        this.id = id;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setRoundTrip(boolean roundTrip) {
        this.roundTrip = roundTrip;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public void setPriceOneWay(BigDecimal priceOneWay) {
        this.priceOneWay = priceOneWay;
    }

    public void setPriceRoundTrip(BigDecimal priceRoundTrip) {
        this.priceRoundTrip = priceRoundTrip;
    }
}


