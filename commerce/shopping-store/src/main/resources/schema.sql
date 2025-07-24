CREATE TABLE IF NOT EXISTS products (
    product_id VARCHAR(36) NOT NULL PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL,
    image_src VARCHAR(255) NOT NULL,
    quantity_state VARCHAR(10) NOT NULL,
    product_state VARCHAR(10) NOT NULL,
    product_category VARCHAR(10) NOT NULL,
    price DOUBLE PRECISION NOT NULL
);
