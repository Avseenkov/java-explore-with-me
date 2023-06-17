CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY      NOT NULL,
    name VARCHAR(50)
        CONSTRAINT minLengthName CHECK ( char_length(name) >= 2 ) NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY         NOT NULL,
    email VARCHAR(254)
        CONSTRAINT minLength CHECK ( char_length(email) >= 6 ) UNIQUE NOT NULL,
    name  VARCHAR(255)                                                NOT NULL
);

CREATE TABLE IF NOT EXISTS events
(
    id                   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    annotation           VARCHAR(2000)
        CONSTRAINT "minLengthAnnotation" CHECK (char_length(annotation) >= 20),
    "category_id"        BIGINT REFERENCES categories (id)                   NOT NULL,
    description          VARCHAR(7000)
        CONSTRAINT "minLengthDescription" CHECK (char_length(description) >= 20),
    "event_date"         TIMESTAMP WITHOUT TIME ZONE                         NOT NULL,
    lat                  FLOAT                                               NOT NULL,
    lon                  FLOAT                                               NOT NULL,
    paid                 BOOLEAN                     DEFAULT FALSE,
    "participant_limit"  INTEGER                     DEFAULT 0               NOT NULL,
    "request_moderation" BOOLEAN                     DEFAULT FALSE,
    title                VARCHAR(120)
        CONSTRAINT "minLengthTitle" CHECK ( char_length(title) >= 3 )        NOT NULL,
    "created_on"         TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()           NOT NULL,
    "published_on"       TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()           NOT NULL,
    "initiator_id"       BIGINT REFERENCES users (id) ON DELETE CASCADE      NOT NULL,
    state                VARCHAR(15)                                         NOT NULL
);

CREATE TABLE IF NOT EXISTS requests
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    "event_id"     BIGINT REFERENCES events (id)                       NOT NULL,
    created        TIMESTAMP WITHOUT TIME ZONE                         NOT NULL,
    "requester_id" BIGINT REFERENCES users (id)                        NOT NULL,
    status         VARCHAR(10)                                         NOT NULL
);

CREATE TABLE IF NOT EXISTS compilation
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    pinned BOOLEAN DEFAULT FALSE                               NOT NULL,
    title  VARCHAR(50)
        CONSTRAINT minTitle CHECK ( char_length(title) >= 1 )  NOT NULL
);

CREATE TABLE IF NOT EXISTS compilation_event
(
    "compilation_id" BIGINT REFERENCES compilation (id),
    "event_id"       BIGINT REFERENCES events (id),
    PRIMARY KEY ("compilation_id", "event_id")
);

CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START WITH 1;