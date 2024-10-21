set search_path = salon;

create table if not exists services
(
    id          bigint generated always as identity primary key,
    title       text    not null,
    price       numeric not null,
    duration    integer not null,
    description text    not null
);