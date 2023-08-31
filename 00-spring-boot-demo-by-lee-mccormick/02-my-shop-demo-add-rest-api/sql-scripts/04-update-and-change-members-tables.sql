CREATE DATABASE  IF NOT EXISTS `store_directory`;
USE `store_directory`;

--
-- STEP 1: Rename table from `members` to `user`
--
ALTER TABLE members
RENAME TO users;

--
-- STEP 2: Add new uuid_id column
--
ALTER TABLE users
ADD user_uuid CHAR(36) DEFAULT NULL;

--
-- STEP 3: Assign UUID() to each user
--
UPDATE users
SET user_uuid = uuid() WHERE user_id = 'admin';

UPDATE users
SET user_uuid = uuid() WHERE user_id = 'lee';

UPDATE users
SET user_uuid = uuid() WHERE user_id = 'sally';

--
-- STEP 4: Rename column
--
ALTER TABLE users
RENAME COLUMN user_id TO username;

--
-- STEP 5:  Rename column again
--
ALTER TABLE users
RENAME COLUMN user_uuid TO user_id;

--
-- STEP 6:  Add new columns to user table
--
ALTER TABLE users
ADD first_name varchar(45) DEFAULT NULL;
ALTER TABLE users
ADD last_name varchar(45) DEFAULT NULL;
ALTER TABLE users
ADD email varchar(45) DEFAULT NULL;
ALTER TABLE users
ADD address varchar(200) DEFAULT NULL;
ALTER TABLE users
ADD phone_number varchar(20) DEFAULT NULL;

--
-- STEP 7:  Add new columns to user table
--
UPDATE users
SET first_name = 'Lee' WHERE username = 'lee';
UPDATE users
SET last_name = 'McCormick'  WHERE username = 'lee';
UPDATE users
SET email = "lee@lee.com"  WHERE username = 'lee';
UPDATE users
SET address = "2119 N Sedgwick St. Chicago IL 60623" WHERE username = 'lee';
UPDATE users
SET phone_number = "312-098-0090"  WHERE username = 'lee';

UPDATE users
SET first_name = 'Sally' WHERE username = 'sally';
UPDATE users
SET last_name = 'Queen'  WHERE username = 'sally';
UPDATE users
SET email = "sally@sally.com"  WHERE username = 'sally';
UPDATE users
SET address = "9086 N Sedgwick St. Norman IL 10990" WHERE username = 'sally';
UPDATE users
SET phone_number = "879-098-0090"  WHERE username = 'sally';

UPDATE users
SET first_name = 'John' WHERE username = 'admin';
UPDATE users
SET last_name = 'Doe'  WHERE username = 'admin';
UPDATE users
SET email = "admin@admin.com"  WHERE username = 'admin';
UPDATE users
SET address = "98 N North St. Norman OK 90870" WHERE username = 'admin';
UPDATE users
SET phone_number = "963-098-0090"  WHERE username = 'admin';
