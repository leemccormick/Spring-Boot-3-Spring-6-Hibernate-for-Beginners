CREATE DATABASE  IF NOT EXISTS `store_directory`;
USE `store_directory`;

--
-- Update Table structure for table `product`
--

ALTER TABLE product
ADD created_date_time DATE DEFAULT NULL;
ALTER TABLE product
ADD updated_date_time DATE DEFAULT NULL;
ALTER TABLE product
ADD created_by varchar(45) DEFAULT NULL;
ALTER TABLE product
ADD updated_by varchar(45) DEFAULT NULL;
ALTER TABLE product
ADD quality int DEFAULT 0;
