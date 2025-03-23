CREATE TABLE guests
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    email      VARCHAR(255) NOT NULL
);

CREATE TABLE rental_properties
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(100) NOT NULL,
    location VARCHAR(255) NOT NULL
);

CREATE TABLE bookings
(
    id                 BIGINT PRIMARY KEY AUTO_INCREMENT,
    guest_id           BIGINT      NOT NULL,
    rental_property_id BIGINT      NOT NULL,
    start_date         DATE        NOT NULL,
    end_date           DATE        NOT NULL,
    status             VARCHAR(20) NOT NULL,
    CONSTRAINT fk_booking_guest FOREIGN KEY (guest_id) REFERENCES guests (id),
    CONSTRAINT fk_booking_rental FOREIGN KEY (rental_property_id) REFERENCES rental_properties (id)
);

CREATE TABLE blocks
(
    id                 BIGINT PRIMARY KEY AUTO_INCREMENT,
    rental_property_id BIGINT       NOT NULL,
    start_date         DATE         NOT NULL,
    end_date           DATE         NOT NULL,
    reason             VARCHAR(255) NOT NULL,
    source             VARCHAR(20)  NOT NULL,
    CONSTRAINT fk_block_rental FOREIGN KEY (rental_property_id) REFERENCES rental_properties (id)
);
