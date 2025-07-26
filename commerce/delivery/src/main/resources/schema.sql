CREATE TABLE IF NOT EXISTS address (
    address_id BIGINT NOT NULL PRIMARY KEY,
    country VARCHAR(100),
    city VARCHAR(100),
    street VARCHAR(100),
    house VARCHAR(100),
    flat VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS delivery (
    delivery_id VARCHAR(36) NOT NULL PRIMARY KEY,
    from_address_id BIGINT NOT NULL REFERENCES address(address_id),
    to_address_id BIGINT NOT NULL REFERENCES address(address_id),
    order_id VARCHAR(36) NOT NULL,
    delivery_state VARCHAR(20) NOT NULL
);
