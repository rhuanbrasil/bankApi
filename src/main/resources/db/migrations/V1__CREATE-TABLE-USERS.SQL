CREATE TABLE users(
    id Serial primary key,
    login TEXT NOT NULL,
    password TEXT NOT NULL,
    role TEXT not null,
)