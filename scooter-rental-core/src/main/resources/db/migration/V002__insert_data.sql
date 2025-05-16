INSERT INTO roles (id, name)
VALUES (-1, 'ROLE_USER'),
       (-2, 'ROLE_MANAGER'),
       (-3, 'ROLE_ADMIN');

INSERT INTO users (id, username, password, role, first_name, last_name, email, phone_number)
VALUES (-1, 'admin', '$2a$12$/9LH.RZqfutLDiThniVCa.tPCsrEg8P8XWEZSYStFy.0NoGTeuQCm', 'ADMIN', 'Admin', 'Adminov', 'admin@mail.ru', '+375293040036'),
       (-2, 'user2', '$2a$12$/9LH.RZqfutLDiThniVCa.tPCsrEg8P8XWEZSYStFy.0NoGTeuQCm', 'USER', 'User', 'Two', 'user2@mail.ru', '+375291111111'),
       (-3, 'user3', '$2a$12$/9LH.RZqfutLDiThniVCa.tPCsrEg8P8XWEZSYStFy.0NoGTeuQCm', 'USER', 'User', 'Three', 'user3@mail.ru', '+375292222222'),
       (-4, 'user4', '$2a$12$/9LH.RZqfutLDiThniVCa.tPCsrEg8P8XWEZSYStFy.0NoGTeuQCm', 'USER', 'User', 'Four', 'user4@mail.ru', '+375293333333'),
       (-5, 'user5', '$2a$12$/9LH.RZqfutLDiThniVCa.tPCsrEg8P8XWEZSYStFy.0NoGTeuQCm', 'USER', 'User', 'Five', 'user5@mail.ru', '+375294444444');

INSERT INTO tariffs (id, type, name, description, price, units_included, validity_period_hours, is_subscription)
VALUES (-1, 'BASIC', 'Почасовой', 'Оплата за каждый час использования', 5.0, NULL, NULL, FALSE),
       (-2, 'BASIC', 'Дневной абонемент', 'Абонемент на целый день', NULL, 20.0, NULL, TRUE),
       (-3, 'BASIC', 'Студенческий', 'Скидка для студентов', 4.0, NULL, 0.10, FALSE),
       (-4, 'BASIC', 'Ночной', 'Тариф для ночного катания', 7.0, NULL, NULL, FALSE),
       (-5, 'BASIC', 'Выходного дня', 'Специальный тариф на выходные', 6.0, NULL, NULL, FALSE);

INSERT INTO rental_points (id, name, address, latitude, longitude, parent_point_id)
VALUES (-1, 'Главный офис Гомель', 'просп. Независимости, 10', 53.9025, 27.5615, NULL),
       (-2, 'Филиал в ТЦ "Столица"', 'пл. Независимости', 53.8968, 27.5508, -1),
       (-3, 'Точка у Национальной библиотеки', 'просп. Независимости, 116', 53.9248, 27.6477, -1),
       (-4, 'Точка в парке Горького', 'ул. Фрунзе, 2', 53.9083, 27.5728, -1),
       (-5, 'Главный офис Брест', 'ул. Советская, 33', 52.0975, 23.6883, NULL),
       (-6, 'Точка на набережной', 'Набережная Франциска Скорины', 52.0922, 23.6944, -5);

INSERT INTO scooters (id, model, serial_number, status, charge_level, mileage, rental_point_id, tariff_id)
VALUES (-1, 'Xiaomi M365', 'SN0001', 'AVAILABLE', 85, 150.5, -1, -1),
       (-2, 'Ninebot ES2', 'SN0002', 'IN_USE', 60, 80.2, -1, -1),
       (-3, 'Xiaomi M365 Pro', 'SN0003', 'AVAILABLE', 95, 200.0, -2, -2),
       (-4, 'Ninebot ES4', 'SN0004', 'MAINTENANCE', 20, 500.7, -2, -1),
       (-5, 'Xiaomi M365', 'SN0005', 'AVAILABLE', 70, 120.3, -3, -3),
       (-6, 'Ninebot ES2', 'SN0006', 'AVAILABLE', 90, 180.8, -3, -4),
       (-7, 'Xiaomi M365 Pro', 'SN0007', 'IN_USE', 45, 300.1, -4, -1),
       (-8, 'Ninebot ES4', 'SN0008', 'AVAILABLE', 80, 250.6, -5, -5),
       (-9, 'Xiaomi M365', 'SN0009', 'AVAILABLE', 65, 90.9, -5, -1),
       (-10, 'Ninebot ES2', 'SN0010', 'UNAVAILABLE', 10, 400.4, -6, -1),
       (-11, 'Xiaomi M365', 'SN0011', 'AVAILABLE', 75, 110.1, -6, -3),
       (-12, 'Ninebot ES2', 'SN0012', 'IN_USE', 55, 70.7, -1, -1);

INSERT INTO subscriptions (id, user_id, tariff_id, expiration_time, rest_units)
VALUES (-1, -1, -1, '2025-12-31 23:59:59', 10),
       (-2, -1, -2, '2025-06-30 23:59:59', 100);

INSERT INTO rentals (user_id, scooter_id, start_time, end_time, start_mileage, end_mileage, total_cost, subscription_id, expiration_time)
VALUES (-1, -2, '2024-01-20 10:00:00', '2024-01-20 11:30:00', 80.2, 95.2, 7.5, -1, '2026-01-20 11:30:00'),
       (-4, -7, '2024-01-21 14:00:00', '2024-01-21 15:00:00', 300.1, 315.1, 5.0, -1, '2026-01-20 11:30:00'),
       (-5, -12, '2024-01-22 16:30:00', NULL, 70.7, NULL, NULL, -1, '2026-01-20 11:30:00'),
       (-1, -1, '2024-01-23 09:00:00', '2024-01-23 10:00:00', 150.5, 160.5, 5.0, -1, '2026-01-20 11:30:00');
