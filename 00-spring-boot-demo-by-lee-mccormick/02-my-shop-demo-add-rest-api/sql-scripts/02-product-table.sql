CREATE DATABASE  IF NOT EXISTS `store_directory`;
USE `store_directory`;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;

CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_name` varchar(45) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

--
-- Data for table `product`
--

INSERT INTO `product` VALUES
	(1,'Phone','My Iphone test',200.89),
	(2,'Food','Yummy in my tummy',23.09),
	(3,'Bag','Let go some where',10.98),
	(4,'Ice cream','test my ice cream',12.90),
	(5,'Apple','the best fruit ever,,,',12324.80);

