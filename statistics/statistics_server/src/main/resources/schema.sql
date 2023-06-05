CREATE TABLE IF NOT EXISTS requests
(
    "request_id" BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app          VARCHAR(255)                            NOT NULL,
    uri          VARCHAR(2000)                           NOT NULL,
    ip           VARCHAR(39)                             NOT NULL,
    "created_at" timestamp default now()                 NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1;