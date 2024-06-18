set search_path = salon;

create table if not exists clients
(
    id         bigint generated always as identity primary key,
    first_name TEXT NOT NULL,
    last_name  TEXT,
    contact    TEXT NOT NULL,
    dob        DATE
);