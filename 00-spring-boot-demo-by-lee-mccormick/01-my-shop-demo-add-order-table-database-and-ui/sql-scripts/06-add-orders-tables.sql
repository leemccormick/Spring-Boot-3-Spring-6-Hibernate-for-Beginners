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
-- STEP 2: Create orders table
--
CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    customer_id INT,
    order_date DATE,
    total_amount DECIMAL(10, 2),
    status VARCHAR(50)
);

--
-- STEP 3: Create order_items table
--
CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY,
    order_id INT,
    product_id INT,
    quantity INT,
    subtotal DECIMAL(10, 2),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);