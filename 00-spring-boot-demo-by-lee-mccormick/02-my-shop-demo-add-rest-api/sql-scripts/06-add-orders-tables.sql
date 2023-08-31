CREATE DATABASE  IF NOT EXISTS `store_directory`;
USE `store_directory`;

--
-- STEP 1: Rename table from `product` to `products` and Update columns
--
ALTER TABLE product
RENAME TO products,
RENAME COLUMN quality TO quantity,
RENAME COLUMN id TO product_id;

--
-- STEP 2: Drop orders table if exists
--
DROP TABLE IF EXISTS `order_items`;
DROP TABLE IF EXISTS `orders`;


--
-- STEP 3: Create orders table
--
CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id CHAR(36) NOT NULL,
    total_amount DECIMAL(10, 2),
    order_status VARCHAR(50),
    created_date_time DATE,
    updated_date_time DATE,
    created_by CHAR(36),
    updated_by CHAR(36)
);

--
-- STEP 4: Create order_items table
--
CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    product_id INT,
    quantity INT,
    subtotal DECIMAL(10, 2),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);