INSERT INTO appointmentscheduler.users
(username, password, first_name, last_name, email, mobile, street, city, postcode)
VALUES('admin', '$2a$10$EqKcp1WFKVQISheBxkQJoOqFbsWDzGJXRz/tjkGq85IZKJJ1IipYi', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO appointmentscheduler.users
(username, password, first_name, last_name, email, mobile, street, city, postcode)
VALUES('provider', '$2a$10$EqKcp1WFKVQISheBxkQJoOqFbsWDzGJXRz/tjkGq85IZKJJ1IipYi', 'Phuong', 'Thu', 's2thuphuongs2@gmail.com', '033333333', 'Le Van Luong', 'HCM', '24-133');
INSERT INTO appointmentscheduler.users
(username, password, first_name, last_name, email, mobile, street, city, postcode)
VALUES('customer_r', '$2a$10$EqKcp1WFKVQISheBxkQJoOqFbsWDzGJXRz/tjkGq85IZKJJ1IipYi', 'C', 'van', 'cc@gmail.com', '090990990', 'Le Van Luong', 'HaNoi', '24-133');
INSERT INTO appointmentscheduler.users
(username, password, first_name, last_name, email, mobile, street, city, postcode)
VALUES('customer_c', '$2a$10$EqKcp1WFKVQISheBxkQJoOqFbsWDzGJXRz/tjkGq85IZKJJ1IipYi', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO appointmentscheduler.users
(username, password, first_name, last_name, email, mobile, street, city, postcode)
VALUES('tuppu123', '$2a$10$kHAitLTP9qBIjfsjWfDjrOuVt9s/hAuhjngwJ77KKpHJB/aiWeRLq', 'C', 'van', 'nguyenvanc@gmail.com', '099090009', 'Nguyen Thi Thap', 'HCM', '17-444');
INSERT INTO appointmentscheduler.users
(username, password, first_name, last_name, email, mobile, street, city, postcode)
VALUES('tuppu1234', '$2a$10$Bj9w4ppIp2NOes6wwKRKYO9OQzzduaN3ph3SPM0e2ozp0SNLRdwFe', 'D', 'Nguyen', 'nguyenvand@gmail.com', '123456789', 'Nguyen Thi Thap', 'HCM', '24-133');

INSERT INTO appointmentscheduler.customers
(id_customer)
VALUES(6);

INSERT INTO appointmentscheduler.appointments
(`start`, `end`, canceled_at, status, id_canceler, id_doctor, id_customer, id_work, id_invoice)
VALUES('2023-12-25 10:15:00', '2023-12-25 11:00:00', NULL, 'REJECTION_REQUESTED', NULL, 2, 3, 2, NULL);
INSERT INTO appointmentscheduler.appointments
(`start`, `end`, canceled_at, status, id_canceler, id_doctor, id_customer, id_work, id_invoice)
VALUES('2023-12-27 03:00:00', '2023-12-27 04:00:00', NULL, 'SCHEDULED', NULL, 2, 3, 1, NULL);
INSERT INTO appointmentscheduler.appointments
(`start`, `end`, canceled_at, status, id_canceler, id_doctor, id_customer, id_work, id_invoice)
VALUES('2023-12-26 23:45:00', '2023-12-27 00:30:00', NULL, 'SCHEDULED', NULL, 2, 3, 2, NULL);
INSERT INTO appointmentscheduler.appointments
(`start`, `end`, canceled_at, status, id_canceler, id_doctor, id_customer, id_work, id_invoice)
VALUES('2023-12-27 04:00:00', '2023-12-27 05:00:00', '2023-12-25 13:37:22', 'CANCELED', 3, 2, 3, 1, NULL);
INSERT INTO appointmentscheduler.appointments
(`start`, `end`, canceled_at, status, id_canceler, id_doctor, id_customer, id_work, id_invoice)
VALUES('2023-12-27 01:15:00', '2023-12-27 02:00:00', NULL, 'SCHEDULED', NULL, 7, 4, 4, NULL);

INSERT INTO appointmentscheduler.messages
(created_at, message, id_author, id_appointment)
VALUES('2023-12-24 10:28:10', 'helo', 2, 1);
INSERT INTO appointmentscheduler.messages
(created_at, message, id_author, id_appointment)
VALUES('2023-12-25 13:30:52', 'hello', 3, 1);
INSERT INTO appointmentscheduler.messages
(created_at, message, id_author, id_appointment)
VALUES('2023-12-25 13:31:49', 'xin chao toi muon book lich nay', 3, 2);
INSERT INTO appointmentscheduler.messages
(created_at, message, id_author, id_appointment)
VALUES('2023-12-25 13:33:34', 'oke', 2, 2);
INSERT INTO appointmentscheduler.messages
(created_at, message, id_author, id_appointment)
VALUES('2023-12-25 13:37:21', 'toi muon huy lich nay', 3, 4);

-- NOTIFICATION
INSERT INTO appointmentscheduler.notifications
(title, message, created_at, url, is_read, id_user)
VALUES('New appointment scheduled', 'New appointment scheduled withnull Thu on 2023-12-25T17:15', '2023-12-24 10:00:10', '/appointments/1', 1, 2);
INSERT INTO appointmentscheduler.notifications
(title, message, created_at, url, is_read, id_user)
VALUES('New chat message', 'You have new chat message from Phuong regarding appointment scheduled at 2023-12-25T17:15', '2023-12-24 10:28:10', '/appointments/1', 1, 3);
INSERT INTO appointmentscheduler.notifications
(title, message, created_at, url, is_read, id_user)
VALUES('Appointment Finished', 'Appointment finished, you can reject that it took place until 2023-12-26T18:00', '2023-12-25 12:21:25', '/appointments/1', 1, 3);
INSERT INTO appointmentscheduler.notifications
(title, message, created_at, url, is_read, id_user)
VALUES('New chat message', 'You have new chat message from C regarding appointment scheduled at 2023-12-25T17:15', '2023-12-25 13:30:52', '/appointments/1', 0, 2);
INSERT INTO appointmentscheduler.notifications
(title, message, created_at, url, is_read, id_user)
VALUES('Appointment Rejected', 'C vanrejected an appointment. Your approval is required', '2023-12-25 13:31:00', '/appointments/1', 0, 2);
INSERT INTO appointmentscheduler.notifications
(title, message, created_at, url, is_read, id_user)
VALUES('New appointment scheduled', 'New appointment scheduled withC Thu on 2023-12-27T10:00', '2023-12-25 13:31:29', '/appointments/2', 0, 2);
INSERT INTO appointmentscheduler.notifications
(title, message, created_at, url, is_read, id_user)
VALUES('New chat message', 'You have new chat message from C regarding appointment scheduled at 2023-12-27T10:00', '2023-12-25 13:31:49', '/appointments/2', 0, 2);
INSERT INTO appointmentscheduler.notifications
(title, message, created_at, url, is_read, id_user)
VALUES('New chat message', 'You have new chat message from Phuong regarding appointment scheduled at 2023-12-27T10:00', '2023-12-25 13:33:34', '/appointments/2', 0, 3);
INSERT INTO appointmentscheduler.notifications
(title, message, created_at, url, is_read, id_user)
VALUES('New appointment scheduled', 'New appointment scheduled withC Thu on 2023-12-27T06:45', '2023-12-25 13:35:45', '/appointments/3', 0, 2);
INSERT INTO appointmentscheduler.notifications
(title, message, created_at, url, is_read, id_user)
VALUES('New appointment scheduled', 'New appointment scheduled withC Thu on 2023-12-27T11:00', '2023-12-25 13:36:08', '/appointments/4', 0, 2);
INSERT INTO appointmentscheduler.notifications
(title, message, created_at, url, is_read, id_user)
VALUES('New chat message', 'You have new chat message from C regarding appointment scheduled at 2023-12-27T11:00', '2023-12-25 13:37:21', '/appointments/4', 0, 2);
INSERT INTO appointmentscheduler.notifications
(title, message, created_at, url, is_read, id_user)
VALUES('Appointment Canceled', 'C van cancelled appointment scheduled at 2023-12-27T11:00', '2023-12-25 13:37:22', '/appointments/4', 0, 2);
INSERT INTO appointmentscheduler.notifications
(title, message, created_at, url, is_read, id_user)
VALUES('New appointment scheduled', 'New appointment scheduled withE Nguyen on 2023-12-27T08:15', '2023-12-25 13:44:42', '/appointments/5', 0, 7);

INSERT INTO appointmentscheduler.users
(username, password, first_name, last_name, email, mobile, street, city, postcode)
VALUES('doctor_c', '$2a$10$HpG89LYs67o6NdN2MtPl3OJTTgO7dDqlxOlRWfNtMX5.KrBuD4CSC', 'C', 'Nguyen', 'nguyenvancu@gmail.com', '123456789', 'Le Van Luong', 'HCM', '24-133');

INSERT INTO appointmentscheduler.doctors
(id_doctor)
VALUES(7);

INSERT INTO appointmentscheduler.retail_customers
(id_customer)
VALUES(6);

INSERT INTO appointmentscheduler.works
(name, duration, price, editable, target, description)
VALUES('English lesson', 60, 100.00, 1, 'retail', 'This is english lesson with duration 60 minutes and price 100 pln');
INSERT INTO appointmentscheduler.works
(name, duration, price, editable, target, description)
VALUES('Coding Java', 45, 3.00, 1, 'retail', 'Hello');
INSERT INTO appointmentscheduler.works
(name, duration, price, editable, target, description)
VALUES('Information Project', 90, 11.00, 1, 'retail', 'H?p nhóm d? án');
INSERT INTO appointmentscheduler.works
(name, duration, price, editable, target, description)
VALUES('Meeting', 45, 14.00, 1, 'corporate', 'Meeting with customer');

