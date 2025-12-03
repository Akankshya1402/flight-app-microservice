CREATE TABLE IF NOT EXISTS flights (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    airline_code VARCHAR(20) NOT NULL,
    airline_name VARCHAR(100) NOT NULL,
    logo_url VARCHAR(255),
    from_city VARCHAR(100) NOT NULL,
    to_city VARCHAR(100) NOT NULL,
    departure_time DATETIME NOT NULL,
    arrival_time DATETIME NOT NULL,
    round_trip BOOLEAN NOT NULL,
    available_seats INT NOT NULL,
    price_one_way DECIMAL(10,2) NOT NULL,
    price_round_trip DECIMAL(10,2)
);
