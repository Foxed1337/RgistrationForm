CREATE TABLE if not exists USERS (id INT, username VARCHAR(255), hash_password VARCHAR(255));

INSERT INTO USERS(id, username, hash_password) values
(1, 'ilya', '$2a$08$fNUHI3FnO3cbT6VAcClJOOsIq93f2101ud2RAKiZFAh7Y2h.oFRzC');