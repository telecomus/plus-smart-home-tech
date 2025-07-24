CREATE TABLE IF NOT EXISTS shopping_carts (
    shopping_cart_id VARCHAR(36) NOT NULL PRIMARY KEY,
    owner VARCHAR(50) NOT NULL UNIQUE,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS shopping_cart_products (
    shopping_cart_id VARCHAR(36) NOT NULL REFERENCES shopping_carts(shopping_cart_id),
    product_id VARCHAR(36) NOT NULL,
    quantity BIGINT NOT NULL,
    UNIQUE(shopping_cart_id, product_id)
);
