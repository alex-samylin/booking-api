DELETE
FROM bookings;
DELETE
FROM blocks;
DELETE
FROM guests;
DELETE
FROM rental_properties;

INSERT INTO guests (id, first_name, last_name, email)
VALUES (1, 'Test', 'User', 'test@example.com');

INSERT INTO rental_properties (id, name, location)
VALUES (1, 'Test Property', 'Test Address');

INSERT INTO bookings (id, guest_id, rental_property_id, start_date, end_date, status)
VALUES (100, 1, 1, '2025-04-10', '2025-04-15', 'NEW'),
       (101, 1, 1, '2025-05-05', '2025-05-10', 'NEW'),
       (102, 1, 1, '2025-05-20', '2025-05-25', 'CANCELED'),
       (103, 1, 1, '2025-05-06', '2025-05-10', 'CANCELED');

INSERT INTO blocks (id, rental_property_id, start_date, end_date, reason, source)
VALUES (200, 1, '2025-04-20', '2025-04-25', 'Test Block', 'OWNER');

