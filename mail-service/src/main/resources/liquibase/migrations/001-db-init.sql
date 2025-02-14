CREATE TABLE mails
(
    id bigserial PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    email   VARCHAR(255) NOT NULL
);
