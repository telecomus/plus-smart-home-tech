CREATE TABLE IF NOT EXISTS payments (
    payment_id VARCHAR(36) NOT NULL PRIMARY KEY,
    status VARCHAR(10) NOT NULL,
    order_id VARCHAR(36),
    total_payment DOUBLE PRECISION,
    delivery_price DOUBLE PRECISION,
    fee_total DOUBLE PRECISION
);
