-- Script para insertar varios datos en tablas

-- Insercion en tabla customers
INSERT INTO public.customers (id, name, email, version)
VALUES (1, 'customer1', 'customer1@mail.com', 1),
       (2, 'customer2', 'customer2@mail.com', 1),
       (3, 'customer3', 'customer3@mail.com', 1),
       (4, 'customer4', 'customer4@mail.com', 1);

-- Insercion en tabla inventory
INSERT INTO public.inventory (isbn, sold, supplied)
VALUES ('1111', 5, 10),
       ('2222', 15, 20),
       ('3333', 0, 30),
       ('4444', 7, 7);

-- Insercion en tabla purchase_orders
-- status es smallint; placed_on y delivered_on son timestamp without timezone
INSERT INTO public.purchase_orders (id, customer_id, total, status, placed_on, delivered_on)
VALUES (1, 1, 20, 1, '2025-06-01 09:30:00', '2025-06-03 14:45:00'),
       (2, 2, 150, 0, '2025-06-05 11:15:00', '2025-06-07 10:00:00'),
       (3, 3, 75, 1, '2025-06-10 16:00:00', '2025-06-12 18:20:00'),
       (4, 1, 40, 0, '2025-06-12 08:00:00', '2025-06-14 12:00:00');

-- Insercion en tabla line_items
INSERT INTO public.line_items (id, order_id, quantity, isbn)
VALUES (1, 1, 2, '1111'),
       (2, 1, 1, '2222'),
       (3, 2, 5, '3333'),
       (4, 3, 3, '4444'),
       (5, 4, 4, '1111');
