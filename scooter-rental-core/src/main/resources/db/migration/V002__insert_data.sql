INSERT INTO roles (id, name)
VALUES (-1, 'ROLE_USER'),
       (-2, 'ROLE_MANAGER'),
       (-3, 'ROLE_ADMIN');

INSERT INTO users (id, username, password, first_name, last_name, email, phone_number, deposit, user_blocked, rent_blocked)
VALUES (-1, 'admin', '$2a$12$/9LH.RZqfutLDiThniVCa.tPCsrEg8P8XWEZSYStFy.0NoGTeuQCm', 'Admin', 'Adminov', 'admin@mail.ru', '0123456789', 0, false, false),
       (-2, 'user2', '$2a$12$/9LH.RZqfutLDiThniVCa.tPCsrEg8P8XWEZSYStFy.0NoGTeuQCm', 'User', 'Two', 'user2@mail.ru', '1234567890', 0, false, false),
       (-3, 'user3', '$2a$12$/9LH.RZqfutLDiThniVCa.tPCsrEg8P8XWEZSYStFy.0NoGTeuQCm', 'User', 'Three', 'user3@mail.ru', '2345678901', 0, false, false),
       (-4, 'user4', '$2a$12$/9LH.RZqfutLDiThniVCa.tPCsrEg8P8XWEZSYStFy.0NoGTeuQCm', 'User', 'Four', 'user4@mail.ru', '3456789012', 0, false, false),
       (-5, 'user5', '$2a$12$/9LH.RZqfutLDiThniVCa.tPCsrEg8P8XWEZSYStFy.0NoGTeuQCm', 'User', 'Five', 'user5@mail.ru', '0987654321', 0, false, false);

INSERT INTO user_roles (user_id, role)
VALUES (-1, 'ADMIN'),
       (-2, 'USER'),
       (-3, 'USER'),
       (-4, 'USER'),
       (-5, 'USER');

INSERT INTO tariffs (id, type, name, description, price, units_included, validity_period_hours, is_subscription)
VALUES (-1, 'BASIC', 'Почасовой', 'Оплата за каждый час использования', 5.0, NULL, NULL, FALSE),
       (-2, 'BASIC', 'Дневной абонемент', 'Абонемент на целый день', NULL, 20.0, NULL, TRUE),
       (-3, 'BASIC', 'Студенческий', 'Скидка для студентов', 4.0, NULL, 0.10, FALSE),
       (-4, 'BASIC', 'Ночной', 'Тариф для ночного катания', 7.0, NULL, NULL, FALSE),
       (-5, 'BASIC', 'Выходного дня', 'Специальный тариф на выходные', 6.0, NULL, NULL, FALSE);

INSERT INTO rental_points (id, name, address, latitude, longitude, parent_point_id)
VALUES (-1, 'Главный офис Калуга - Правый берег', 'ул. Генерала Попова, 1А', 54.491796, 36.224693, NULL),
       (-2, 'Точка отель Квань', 'ул. Трамплинная, 1', 54.503433, 36.200701, -1),
       (-3, 'Точка Шопино', 'ул. Центральная,3В', 54.494997, 36.158781, -1),
       (-4, 'Главный офис Калуга - центр', 'ул. Кирова, 1', 54.515999, 36.249731, NULL),
       (-5, 'Точка пл. Победы', 'ул. Маршала Жукова, 1', 54.513156, 36.274812, -4),
       (-6, 'Точка пл. Московской', 'ул. Московская, 212', 54.530985, 36.269047, -4);

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
