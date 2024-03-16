CREATE DATABASE  IF NOT EXISTS `appointmentscheduler`;
USE `appointmentscheduler`;

CREATE TABLE IF NOT EXISTS `roles` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`name` nvarchar(50) DEFAULT NULL,
	PRIMARY KEY (`id`)
)
	ENGINE=InnoDB
	AUTO_INCREMENT=1
	DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `users` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`username` nvarchar(50) NOT NULL,
	`password` char(80) NOT NULL,
	`first_name` nvarchar(50),
	`last_name` nvarchar(50),
	`email` varchar(50),
	`mobile` varchar(50),
	`street` varchar(50),
	`city` varchar(50),
  PRIMARY KEY (`id`)
)
	ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE IF NOT EXISTS `users_roles` (
	`user_id` int(11) NOT NULL,
	`role_id` int(11) NOT NULL,
	PRIMARY KEY (`user_id`,`role_id`),
	KEY `FK_ROLE_idx` (`role_id`),

	CONSTRAINT `FK_users_user` FOREIGN KEY (`user_id`)
	REFERENCES `users` (`id`)
	ON DELETE NO ACTION ON UPDATE NO ACTION,

	CONSTRAINT `FK_roles_role` FOREIGN KEY (`role_id`)
	REFERENCES `roles` (`id`)
	ON DELETE NO ACTION ON UPDATE NO ACTION
)
	ENGINE=InnoDB
  DEFAULT CHARSET=utf8;
	SET FOREIGN_KEY_CHECKS = 1;


CREATE TABLE IF NOT EXISTS `works` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` NVARCHAR(256),
  `duration` INT(11),
  `price` DECIMAL(10, 2),
	`editable` BOOLEAN,
	`target` VARCHAR(256),
  `description` NVARCHAR(256),
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `invoices` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`number` VARCHAR(256),
	`status` VARCHAR(256),
	`total_amount` DECIMAL(10, 2),
	`issued` DATETIME,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE IF NOT EXISTS `appointments` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `start` DATETIME,
  `end` DATETIME,
	`canceled_at` DATETIME,
	`status` VARCHAR(20),
	`id_canceler` INT(11),
  `id_doctor` INT(11),
  `id_customer` INT(11),
  `id_work` INT(11),
	`id_invoice` INT(11),
  PRIMARY KEY (`id`),
	KEY `id_canceler` (`id_canceler`),
  KEY `id_doctor` (`id_doctor`),
  KEY `id_customer` (`id_customer`),
  KEY `id_work` (`id_work`),
	KEY `id_invoice` (`id_invoice`),
	CONSTRAINT `appointments_users_canceler` FOREIGN KEY (`id_canceler`) REFERENCES `users` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  CONSTRAINT `appointments_users_customer` FOREIGN KEY (`id_customer`) REFERENCES `users` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  CONSTRAINT `appointments_works` FOREIGN KEY (`id_work`) REFERENCES `works` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  CONSTRAINT `appointments_users_doctor` FOREIGN KEY (`id_doctor`) REFERENCES `users` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
	CONSTRAINT `appointments_invoices` FOREIGN KEY (`id_invoice`) REFERENCES `invoices` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE

)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;



CREATE TABLE IF NOT EXISTS `works_doctors` (
  `id_user` INT(11) NOT NULL,
  `id_work` INT(11) NOT NULL,
  PRIMARY KEY (`id_user`, `id_work`),
  KEY `id_work` (`id_work`),
  CONSTRAINT `works_doctors_users_doctor` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  CONSTRAINT `works_doctors_works` FOREIGN KEY (`id_work`) REFERENCES `works` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `working_plans` (
	`id_doctor` int(11) NOT NULL,
  `monday` TEXT,
	`tuesday` TEXT,
	`wednesday` TEXT,
	`thursday` TEXT,
	`friday` TEXT,
	`saturday` TEXT,
	`sunday` TEXT,

  PRIMARY KEY (`id_doctor`),
	KEY `id_doctor` (`id_doctor`),

	CONSTRAINT `FK_appointments_doctor` FOREIGN KEY (`id_doctor`)
	REFERENCES `users` (`id`)

	ON DELETE NO ACTION
  ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE IF NOT EXISTS `messages` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`created_at` DATETIME,
	`message` TEXT,
	`id_author` INT(11),
  `id_appointment` INT(11),
  PRIMARY KEY (`id`),
	KEY `id_author` (`id_author`),
	KEY `id_appointment` (`id_appointment`),

	CONSTRAINT `FK_notes_author` FOREIGN KEY (`id_author`)
  REFERENCES `users` (`id`)
	ON DELETE NO ACTION
  ON UPDATE NO ACTION,

	CONSTRAINT `FK_notes_appointment` FOREIGN KEY (`id_appointment`)
	REFERENCES `appointments` (`id`)
	ON DELETE NO ACTION
  ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;



CREATE TABLE IF NOT EXISTS `corporate_customers` (
	`id_customer` int(11) NOT NULL,
	`vat_number` VARCHAR(256),
	`company_name` NVARCHAR(256),
  PRIMARY KEY (`id_customer`),
	KEY `id_customer` (`id_customer`),
	CONSTRAINT `FK_corporate_customer_user` FOREIGN KEY (`id_customer`)
	REFERENCES `users` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `doctors` (
	`id_doctor` int(11) NOT NULL,
  PRIMARY KEY (`id_doctor`),
	KEY `id_doctor` (`id_doctor`),
	CONSTRAINT `FK_doctor_user` FOREIGN KEY (`id_doctor`)
	REFERENCES `users` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE IF NOT EXISTS  `retail_customers` (
	`id_customer` int(11) NOT NULL,
  PRIMARY KEY (`id_customer`),
	KEY `id_customer` (`id_customer`),
	CONSTRAINT `FK_retail_customer_user` FOREIGN KEY (`id_customer`)
	REFERENCES `users` (`id`)
)
	ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `customers` (
	`id_customer` int(11) NOT NULL,
  PRIMARY KEY (`id_customer`),
	KEY `id_customer` (`id_customer`),

	CONSTRAINT `FK_customer_user` FOREIGN KEY (`id_customer`)
	REFERENCES `users` (`id`)
)
	ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `notifications` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`title` NVARCHAR(256),
	`message` NVARCHAR(256),
  `created_at` DATETIME,
  `url` VARCHAR(256),
	`is_read` BOOLEAN,
  `id_user` INT(11),
  PRIMARY KEY (`id`),
	KEY `id_user` (`id_user`),

	CONSTRAINT `FK_notification_user` FOREIGN KEY (`id_user`)
  REFERENCES `users` (`id`)
	ON DELETE NO ACTION
  ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;



-- INSERT available roles
INSERT INTO `roles` (id,name) VALUES
  (1,'ROLE_ADMIN'),
  (2,'ROLE_PROVIDER'),
  (3,'ROLE_CUSTOMER'),
  (4,'ROLE_CUSTOMER_CORPORATE'),
  (5,'ROLE_CUSTOMER_RETAIL');

-- INSERT admin account with username: 'admin' and password 'qwerty123'
INSERT INTO `users` (id, username, password)
VALUES (1, 'admin', '$2a$10$EqKcp1WFKVQISheBxkQJoOqFbsWDzGJXRz/tjkGq85IZKJJ1IipYi');
INSERT INTO `users_roles` (user_id, role_id)
VALUES (1, 1);

-- INSERT doctor account with username: 'doctor' and password 'qwerty123'
INSERT INTO `users` (id, username, password)
VALUES (2, 'doctor', '$2a$10$EqKcp1WFKVQISheBxkQJoOqFbsWDzGJXRz/tjkGq85IZKJJ1IipYi');
INSERT INTO `doctors` (id_doctor)
VALUES (2);
INSERT INTO `users_roles` (user_id, role_id)
VALUES (2, 2);


-- INSERT retail customer account with username: 'customer_r' and password 'qwerty123'
INSERT INTO `users` (id, username, password)
VALUES (3, 'customer_r', '$2a$10$EqKcp1WFKVQISheBxkQJoOqFbsWDzGJXRz/tjkGq85IZKJJ1IipYi');
INSERT INTO `customers` (id_customer)
VALUES (3);
INSERT INTO `retail_customers` (id_customer)
VALUES (3);
INSERT INTO `users_roles` (user_id, role_id)
VALUES (3, 3);
INSERT INTO `users_roles` (user_id, role_id)
VALUES (3, 5);

-- INSERT corporate customer account with username: 'customer_c' and password 'qwerty123'
INSERT INTO `users` (id, username, password)
VALUES (4, 'customer_c', '$2a$10$EqKcp1WFKVQISheBxkQJoOqFbsWDzGJXRz/tjkGq85IZKJJ1IipYi');
INSERT INTO `customers` (id_customer)
VALUES (4);
INSERT INTO `corporate_customers` (id_customer, vat_number, company_name)
VALUES (4, '123456789', 'Company name');
INSERT INTO `users_roles` (user_id, role_id)
VALUES (4, 3);
INSERT INTO `users_roles` (user_id, role_id)
VALUES (4, 4);

INSERT INTO `works` (id, name, duration, price, editable, target, description)
VALUES (1, 'Khám răng hàm mặt', 60, 100.00, true, 'retail',
        'Hẹn kiểm tra tổng quát răng hàm mặt'),
    (2, 'Nhổ răng', 60, 100.00, true, 'retail',
        'Hẹn nhổ răng'),
    (3, 'Chữa trị răng', 60, 100.00, true, 'retail',
        'Hẹn chữa trị răng'),
    (4, 'Chụp X-quang', 60, 100.00, true, 'retail',
        'Hẹn chụp X-quang'),
    (5, 'Chụp CT', 60, 100.00, true, 'retail',
        'Hẹn chụp CT'),
    (6, 'Chụp MRI', 60, 100.00, true, 'retail',
        'Hẹn chụp MRI'),
    (7, 'Chụp siêu âm', 60, 100.00, true, 'retail',
        'Hẹn chụp siêu âm'),
    (8, 'Chụp cắt lớp', 60, 100.00, true, 'retail',
        'Hẹn chụp cắt lớp');

INSERT INTO works_doctors
VALUES (2, 1);
INSERT INTO working_plans
VALUES (2,
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}',
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}',
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}',
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}',
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}',
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}',
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}');

-- Thêm cột barcode_id vào bảng appointments
ALTER TABLE appointments
    ADD COLUMN barcode_id BIGINT UNSIGNED NOT NULL;
-- Đặt giá trị mặc định là giá trị ngẫu nhiên
ALTER TABLE appointments
    ALTER COLUMN barcode_id SET DEFAULT 0;
-- Tạo trigger để sinh barcode_id ngẫu nhiên
# DELIMITER //
#
# CREATE TRIGGER before_insert_appointments
#     BEFORE INSERT ON appointments FOR EACH ROW
# BEGIN
#     IF NEW.barcode_id = 0 OR NEW.barcode_id IS NULL THEN
#         SET NEW.barcode_id = FLOOR(100000000 + RAND() * 900000000);
#     END IF;
# END;
# //
#
# DELIMITER ;
-- Thêm cột TEXT để lưu chuỗi Base64 của barcode
ALTER TABLE appointments
    ADD COLUMN barcode_image TEXT;



# ALTER TABLE appointments
#     DROP COLUMN barcode_id;

INSERT INTO `appointments`
(`start`, `end`, canceled_at, status, id_canceler, id_doctor, id_customer, id_work, id_invoice, barcode_id, barcode_image)
VALUES ('2024-03-11 04:00:00', '2024-03-11 05:00:00', NULL, 'CONFIRMED', NULL, 6, 3, 1, NULL, 121667259, 'src/main/resources/static/img/barcodes/null.png'),

('2024-03-11 03:00:00', '2024-03-11 04:00:00', NULL, 'CONFIRMED', NULL, 7, 3, 1, NULL, 999512313, 'src/main/resources/static/img/barcodes/null.png'),
('2024-03-11 08:00:00', '2024-03-11 09:30:00', NULL, 'CONFIRMED', NULL, 7, 3, 2, NULL, 224156466, 'src/main/resources/static/img/barcodes/null.png'),
('2024-03-11 09:30:00', '2024-03-11 11:00:00', NULL, 'CONFIRMED', NULL, 7, 3, 2, NULL, 253885803, 'src/main/resources/static/img/barcodes/null.png'),
('2024-03-13 06:30:00', '2024-03-13 08:00:00', NULL, 'CONFIRMED', NULL, 7, 3, 2, NULL, 600439328, 'src/main/resources/static/img/barcodes/null.png'),
('2024-03-13 01:00:00', '2024-03-13 02:00:00', NULL, 'CONFIRMED', NULL, 7, 3, 1, NULL, 700848280, 'src/main/resources/static/img/barcodes/null.png'),
('2024-03-14 04:00:00', '2024-03-14 05:00:00', NULL, 'CONFIRMED', NULL, 7, 3, 1, NULL, 657419302, 'src/main/resources/static/img/barcodes/null.png'),
('2024-03-12 05:00:00', '2024-03-12 07:00:00', NULL, 'CONFIRMED', NULL, 7, 4, 3, NULL, 753920410, 'src/main/resources/static/img/barcodes/null.png'),
('2024-03-12 03:00:00', '2024-03-12 05:00:00', NULL, 'CONFIRMED', NULL, 7, 4, 3, NULL, 123456789, 'src/main/resources/static/img/barcodes/123456789.png'),
('2024-03-12 08:00:00', '2024-03-12 09:00:00', NULL, 'CONFIRMED', NULL, 7, 3, 1, NULL, 19794, 'src/main/resources/static/img/barcodes/19794.png'),
('2024-03-14 06:30:00', '2024-03-14 08:00:00', NULL, 'CONFIRMED', NULL, 7, 3, 2, NULL, 673726580, 'src/main/resources/static/img/barcodes/673726580.png'),
('2024-03-13 09:30:00', '2024-03-13 11:00:00', NULL, 'CONFIRMED', NULL, 7, 3, 2, NULL, 531111044, 'src/main/resources/static/img/barcodes/531111044.png'),
('2024-03-13 08:00:00', '2024-03-13 09:00:00', NULL, 'CONFIRMED', NULL, 7, 3, 1, NULL, 586107094, 'src/main/resources/static/img/barcodes/586107094.png'),
('2024-03-12 09:00:00', '2024-03-12 10:00:00', NULL, 'CONFIRMED', NULL, 7, 3, 1, NULL, 824082625, 'src/main/resources/static/img/barcodes/824082625.png'),
('2024-03-14 23:00:00', '2024-03-15 00:00:00', NULL, 'FINISHED', NULL, 7, 3, 1, NULL, 782169823, 'src/main/resources/static/img/barcodes/782169823.png'),
('2024-03-15 02:00:00', '2024-03-15 03:00:00', NULL, 'FINISHED', NULL, 7, 3, 1, NULL, 763108761, 'src/main/resources/static/img/barcodes/763108761.png'),
('2024-03-16 00:30:00', '2024-03-16 02:00:00', NULL, 'SCHEDULED', NULL, 7, 3, 2, NULL, 108003337, 'src/main/resources/static/img/barcodes/108003337.png'),
('2024-03-14 08:00:00', '2024-03-14 09:30:00', NULL, 'CONFIRMED', NULL, 7, 3, 2, NULL, 596450663, 'src/main/resources/static/img/barcodes/596450663.png'),
('2024-03-19 09:00:00', '2024-03-19 10:30:00', NULL, 'SCHEDULED', NULL, 7, 3, 2, NULL, 937801552, 'src/main/resources/static/img/barcodes/937801552.png'),
('2024-03-15 01:00:00', '2024-03-15 02:00:00', NULL, 'FINISHED', NULL, 7, 3, 1, NULL, 733196436, 'src/main/resources/static/img/barcodes/733196436.png'),
('2024-03-15 04:30:00', '2024-03-15 06:00:00', NULL, 'FINISHED', NULL, 7, 3, 2, NULL, 363074563, 'src/main/resources/static/img/barcodes/363074563.png'),
('2024-03-15 03:00:00', '2024-03-15 04:00:00', NULL, 'FINISHED', NULL, 7, 3, 1, NULL, 252542459, 'src/main/resources/static/img/barcodes/252542459.png'),
('2024-03-12 23:00:00', '2024-03-13 00:00:00', NULL, 'CONFIRMED', NULL, 6, 3, 1, NULL, 8858741713411, 'src/main/resources/static/img/barcodes/813643756.png');
