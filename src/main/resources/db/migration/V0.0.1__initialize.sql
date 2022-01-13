-- create type user_role as enum ('admin', 'user');
-- create type friendship_status as enum ('new', 'rejected', 'blocked', 'friend');


create table if not exists auth_user
(
    id         SERIAL PRIMARY KEY,
    email      varchar(254) NOT NULL UNIQUE,
    "password" varchar(64)  NOT null,
    user_name  varchar(20)  not null,
    "role"     varchar(10) not null default 'USER'
    );

create table if not exists friendship
(
    id SERIAL primary key,
    sourceId int not null references auth_user(id),
    targetId int not null references auth_user(id),
    status varchar(10) not null default 'NEW',
    created_at timestamp NOT null default now(),
    updated_at timestamp NOT null default now(),
    unique (sourceId, targetId)
    );
