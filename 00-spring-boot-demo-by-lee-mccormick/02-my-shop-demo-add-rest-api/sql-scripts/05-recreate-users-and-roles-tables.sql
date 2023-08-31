--
-- STEP 1 : Drop tables
--
CREATE DATABASE  IF NOT EXISTS `store_directory`;
USE `store_directory`;
DROP TABLE IF EXISTS `roles`;
DROP TABLE IF EXISTS `members`;
DROP TABLE IF EXISTS `users`;

--
-- STEP 2 : Create user table
--
CREATE TABLE `users` (
  `user_id` CHAR(36) NOT NULL,
  `username` varchar(50) NOT NULL,
  `pw` char(68) NOT NULL,
  `active` tinyint NOT NULL,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- STEP 3 : Inserting data for table `users`
--
-- NOTE: The passwords are encrypted using BCrypt
--
-- A generation tool is avail at: https://www.luv2code.com/generate-bcrypt-password
--
-- Default passwords here are: fun123
--
INSERT INTO `users`
VALUES
(uuid(), 'lee','{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q',1,'Lee','McCormick','lee@lee.com', '2119 N Sedgwick St. Chicago IL 60623', '312-098-0090'),
(uuid(), 'sally','{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q',1,'Sally','Queen','sally@sally.com', '9086 N South24 Ave St. Chicago IL 98930', '890-098-0090'),
(uuid(), 'admin','{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q',1,'John','Doe','john@john.com', '89 N North St. Norman OK 98745', '987-098-0090')

--
-- STEP 4 : Create roles table
--
CREATE TABLE `roles` (
  `user_id` CHAR(36) NOT NULL,
  `role` varchar(50) NOT NULL,
  UNIQUE KEY `authorities5_idx_1` (`user_id`,`role`),
  CONSTRAINT `authorities5_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- STEP 5 : Inserting data for table `roles` based on UUID()
--

INSERT INTO `roles`
VALUES
('4f3587de-42ce-11ee-8c3f-2841cb38b8f1','ROLE_CUSTOMER'),
('4f3598dc-42ce-11ee-8c3f-2841cb38b8f1','ROLE_CUSTOMER'),
('4f3598dc-42ce-11ee-8c3f-2841cb38b8f1','ROLE_SALE'),
('4f359a12-42ce-11ee-8c3f-2841cb38b8f1','ROLE_CUSTOMER'),
('4f359a12-42ce-11ee-8c3f-2841cb38b8f1','ROLE_SALE'),
('4f359a12-42ce-11ee-8c3f-2841cb38b8f1','ROLE_ADMIN');