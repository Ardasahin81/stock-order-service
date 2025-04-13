
INSERT INTO s_user (created_date, id, modified_date, password, username) VALUES ('2025-04-10 21:05:56.000000', s_user_seq.nextval, '2025-04-10 21:06:02.000000', '$2a$10$5DHROvg/kz7UQ5tzcjy9OOs9Hrnrv1CbMGmgWNLnfw9vgz7cDrV2m', 'admin');

INSERT INTO s_user_role (created_date, id, modified_date, user_id, role) VALUES ('2025-04-10 21:05:56.000000', s_user_role_seq.nextval, '2025-04-10 21:06:02.000000', 1, 'ADMIN');
INSERT INTO s_user_role (created_date, id, modified_date, user_id, role) VALUES ('2025-04-10 21:05:56.000000', s_user_role_seq.nextval, '2025-04-10 21:06:02.000000', 1, 'USER');

--$2a$10$lWXRiuhptk0fLSzU18nk1.3CsUioKC44U85bs15DUHYmSTKuYIAZG