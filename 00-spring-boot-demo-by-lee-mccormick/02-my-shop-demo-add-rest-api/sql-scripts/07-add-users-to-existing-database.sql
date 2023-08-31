CREATE DATABASE  IF NOT EXISTS `store_directory`;
USE `store_directory`;

--
-- STEP 1 : Add a few users to database by inserting data for table `users`
--
-- NOTE: The passwords are encrypted using BCrypt
--
-- A generation tool is avail at: https://www.luv2code.com/generate-bcrypt-password
--
-- Default passwords here are: fun123
--
-- There are user data for testing...
--
-- '4f3587de-42ce-11ee-8c3f-2841cb38b8f1', 'lee', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q', '1', 'Lee', 'McCormick', 'lee@lee.com', '2119 N Sedgwick St. Chicago IL 60623', '312-098-0090'
-- '4f3598dc-42ce-11ee-8c3f-2841cb38b8f1', 'sally', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q', '1', 'Sally', 'Queen', 'sally@sally.com', '9086 N South24 Ave St. Chicago IL 98930', '890-098-0090'
-- '4f359a12-42ce-11ee-8c3f-2841cb38b8f1', 'admin', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q', '1', 'John', 'Doe', 'john@john.com', '89 N North St. Norman OK 98745', '987-098-0090'
-- '5e12777e-4839-11ee-887b-8673b11c790c', 'donny', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q', '1', 'Donny', 'McCormick', 'donny@donny.com', '9808 N Sedgwick St. Chicago IL 09876', '312-098-0986'
-- '5e129bc8-4839-11ee-887b-8673b11c790c', 'hanna', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q', '1', 'Hanna', 'Lee', 'hanna@hanna.com', '9808 N Chicago St. Chicago IL 09876', '908-098-0986'
-- '5e129db2-4839-11ee-887b-8673b11c790c', 'justin', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q', '1', 'Justin', 'Walker', 'justin@justin.com', '9808 N Los Angel St. LosAgle CA 09876', '980-098-098'
-- '5e129e5c-4839-11ee-887b-8673b11c790c', 'sam', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q', '1', 'Sam', 'Wang', 'sam@sam.com', '9086 N State Ave St. Chicago IL 98930', '890-098-6785'
-- '5e12a96a-4839-11ee-887b-8673b11c790c', 'jim', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q', '1', 'Jimmy', 'Test', 'john@john.com', '90 N North St. Park OK 98745', '123-098-0090'
--

--
-- STEP 2 : Drop and recreate roles table with roles data based on UUID()
--
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `user_id` CHAR(36) NOT NULL,
  `role` varchar(50) NOT NULL,
  UNIQUE KEY `authorities5_idx_1` (`user_id`,`role`),
  CONSTRAINT `authorities5_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `roles`
VALUES
('5e12777e-4839-11ee-887b-8673b11c790c','ROLE_CUSTOMER'), -- donny
('5e129bc8-4839-11ee-887b-8673b11c790c','ROLE_CUSTOMER'), -- hanna
('5e129db2-4839-11ee-887b-8673b11c790c','ROLE_CUSTOMER'), -- justin
('5e129db2-4839-11ee-887b-8673b11c790c','ROLE_SALE'), -- justin
('5e129e5c-4839-11ee-887b-8673b11c790c','ROLE_CUSTOMER'), -- sam
('5e129e5c-4839-11ee-887b-8673b11c790c','ROLE_SALE'), -- sam
('5e12a96a-4839-11ee-887b-8673b11c790c','ROLE_CUSTOMER'), -- jim
('5e12a96a-4839-11ee-887b-8673b11c790c','ROLE_SALE'), -- jim
('5e12a96a-4839-11ee-887b-8673b11c790c','ROLE_ADMIN'), -- jim
('4f3587de-42ce-11ee-8c3f-2841cb38b8f1','ROLE_CUSTOMER'), -- lee
('4f3598dc-42ce-11ee-8c3f-2841cb38b8f1','ROLE_CUSTOMER'), -- sally
('4f3598dc-42ce-11ee-8c3f-2841cb38b8f1','ROLE_SALE'),  -- sally
('4f359a12-42ce-11ee-8c3f-2841cb38b8f1','ROLE_CUSTOMER'), -- admin
('4f359a12-42ce-11ee-8c3f-2841cb38b8f1','ROLE_SALE'),  -- admin
('4f359a12-42ce-11ee-8c3f-2841cb38b8f1','ROLE_ADMIN');  -- admin
