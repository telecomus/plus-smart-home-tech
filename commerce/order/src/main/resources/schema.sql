CREATE TABLE IF NOT EXISTS orders (
    order_id VARCHAR(36) NOT NULL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    shopping_cart_id VARCHAR(36),
    payment_id VARCHAR(36),
    delivery_id VARCHAR(36),
    state VARCHAR(20) NOT NULL,
    delivery_weight DOUBLE PRECISION,
    delivery_volume DOUBLE PRECISION,
    fragile BOOLEAN NOT NULL DEFAULT FALSE,
    total_price DOUBLE PRECISION,
    delivery_price DOUBLE PRECISION,
    product_price DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS orders_products (
    order_id VARCHAR(36) NOT NULL REFERENCES orders(order_id),
    product_id VARCHAR(36) NOT NULL,
    quantity BIGINT NOT NULL,
    UNIQUE(order_id, product_id)
);
