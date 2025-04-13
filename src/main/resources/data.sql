INSERT INTO s_asset_definition (created_date, id, modified_date, name) VALUES ('2025-04-10 20:52:33.000000', s_asset_definition_seq.nextval, '2025-04-10 20:52:33.000000', 'TRY');
INSERT INTO s_asset_definition (created_date, id, modified_date, name) VALUES ('2025-04-10 20:52:33.000000', s_asset_definition_seq.nextval, '2025-04-10 20:52:33.000000', 'SISE');

INSERT INTO s_customer (created_date, id, modified_date, name) VALUES ('2025-04-10 21:05:56.000000', s_customer_seq.nextval, '2025-04-10 21:06:02.000000', 'Arda');

INSERT INTO s_asset (size, usable_size, created_date, customer_id, id, modified_date, asset_name) VALUES (10000.00, 9939.70, '2025-04-10 21:15:04.000000', 1, s_asset_seq.nextval, '2025-04-10 21:15:04.000000', 'TRY');

INSERT INTO s_order (price, size, created_date, customer_id, id, modified_date, asset_name, order_side, status) VALUES (20.10, 3.00, '2025-04-10 21:38:51.000000', 1, s_order_seq.nextval, '2025-04-10 21:38:51.000000', 'SISE', 'BUY', 'PENDING');

INSERT INTO s_user (created_date, id, modified_date, password, username) VALUES ('2025-04-10 21:05:56.000000', s_user_seq.nextval, '2025-04-10 21:06:02.000000', '$2a$10$X.f5kiIpwdX5tKKz4z6JPOexckE52UDI5CEBKk5RTVo2kH7tsRNY6', 'trader');
INSERT INTO s_user_role (created_date, id, modified_date, user_id, role) VALUES ('2025-04-10 21:05:56.000000', s_user_role_seq.nextval, '2025-04-10 21:06:02.000000', (select id from s_user where username = 'trader'), 'USER');

