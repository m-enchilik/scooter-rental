CREATE TABLE roles
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE users
(
    id           BIGSERIAL PRIMARY KEY,
    username     VARCHAR(50) UNIQUE NOT NULL,
    password     VARCHAR(255)       NOT NULL,
    first_name   VARCHAR(50),
    last_name    VARCHAR(50),
    email        VARCHAR(100),
    phone_number VARCHAR(20),
    deposit      DECIMAL,
    user_blocked BOOLEAN,
    rent_blocked BOOLEAN
);

CREATE TABLE user_roles
(
    user_id BIGINT REFERENCES users (id) NOT NULL,
    role    VARCHAR(50)                  NOT NULL
);

CREATE TABLE rental_points
(
    id              BIGSERIAL        PRIMARY KEY,
    name            VARCHAR(100)     NOT NULL,
    address         VARCHAR(255)     NOT NULL,
    latitude        DOUBLE PRECISION           NOT NULL,
    longitude       DOUBLE PRECISION           NOT NULL,
    parent_point_id BIGINT REFERENCES rental_points (id)
);

CREATE TABLE tariffs
(
    id                      BIGSERIAL   PRIMARY KEY,
    type                    VARCHAR(50) NOT NULL,
    name                    VARCHAR(50) NOT NULL,
    description             VARCHAR(255),
    price                   DECIMAL,
    units_included          INTEGER,
    validity_period_hours   INTEGER,
    is_subscription         BOOLEAN     NOT NULL
);

CREATE TABLE scooters
(
    id              BIGSERIAL          PRIMARY KEY,
    model           VARCHAR(100)       NOT NULL,
    serial_number   VARCHAR(50)        NOT NULL,
    status          VARCHAR(20)        NOT NULL,
    charge_level    INTEGER            NOT NULL,
    mileage         DOUBLE PRECISION,
    rental_point_id BIGINT REFERENCES rental_points (id),
    tariff_id       BIGINT REFERENCES tariffs (id)
);

CREATE TABLE subscriptions
(
    id                  BIGSERIAL PRIMARY KEY,
    user_id             BIGINT REFERENCES users (id)    NOT NULL,
    tariff_id           BIGINT REFERENCES tariffs (id)  NOT NULL,
    expiration_time     TIMESTAMP,
    rest_units          INTEGER
);

CREATE TABLE rentals
(
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT REFERENCES users (id)    NOT NULL,
    scooter_id    BIGINT REFERENCES scooters (id) NOT NULL,
    start_time    TIMESTAMP                       NOT NULL,
    end_time      TIMESTAMP,
    start_mileage DOUBLE PRECISION,
    end_mileage   DOUBLE PRECISION,
    total_cost    DECIMAL,
    subscription_id BIGINT REFERENCES subscriptions (id) NOT NULL,
    expiration_time TIMESTAMP
);

