-- set search_path = salon;

        set search_path to salon;

        create schema IF NOT EXISTS salon;

        create table if not exists salon.services
        (
            id          bigint generated always as identity primary key,
            title       text    not null,
            price       numeric not null,
            duration    integer not null,
            description text    not null
        );

        create table if not exists salon.consumables
        (
            id    bigint generated always as identity primary key,
            title text NOT NULL,
            unit  text DEFAULT 'PIECE',
            price DECIMAL
        );

        create table if not exists salon.clients
        (
            id         bigint generated always as identity primary key,
            first_name TEXT NOT NULL,
            last_name  TEXT,
            contact    TEXT NOT NULL,
            dob        DATE
        );

        create table if not exists salon.visits
        (
            id         bigint generated always as identity primary key,
            date       DATE   NOT NULL,
            start_time TIME   NOT NULL,
            service_id bigint not null,
            client_id  bigint not null,
            master_id  bigint not null
        );

        create table if not exists salon.employees
        (
            id         bigint generated always as identity primary key,
            first_name TEXT NOT NULL,
            last_name  TEXT NOT NULL,
            contact    TEXT NOT NULL,
            dob        DATE NOT NULL,
            function   TEXT NOT NULL
        );

        create table salon.service_to_consumables
        (
            id            bigint generated always as identity,
            consumable_id bigint not null
                constraint service_to_consumables_fk1
                    references consumables (id) on delete cascade,
            service_id    bigint not null
                constraint service_to_consumables_fk2
                    references services (id) on delete cascade
        );

        create table if not exists salon.users
        (
            id         bigint generated always as identity primary key,
            first_name TEXT,
            last_name  TEXT,
            contact    TEXT NOT NULL,
            dob        DATE,
            user_name  TEXT NOT NULL,
            email      TEXT NOT NULL,
            password   TEXT NOT NULL,
            enabled    TEXT DEFAULT 'true'
        );

        create table if not exists salon.tokens
        (
            id         bigint generated always as identity primary key,
            token      TEXT      NOT NULL,
            valid_thru TIMESTAMP NOT NULL
        );


        create table if not exists salon.authorities
        (
            id   bigint generated always as identity primary key,
            name TEXT NOT NULL
        );

        create table if not exists salon.user_to_tokens
        (
            id       bigint generated always as identity,
            token_id bigint not null
                constraint user_to_tokens_fk1
                    references tokens (id),
            user_id  bigint not null
                constraint user_to_tokens_fk2
                    references users (id)
        );

        create table if not exists user_to_authorities
        (
            id           bigint generated always as identity,
            authority_id bigint not null
                constraint user_to_authorities_fk1
                    references salon.authorities (id),
            user_id      bigint not null
                constraint user_to_authorities_fk2
                    references salon.users (id)
        );










