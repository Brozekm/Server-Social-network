create type user_role as enum ('admin', 'user');
create type friendship_status as enum ('new', 'rejected', 'blocked', 'friend');


create table if not exists auth_user
(
    id         SERIAL PRIMARY KEY,
    email      varchar(254) NOT NULL UNIQUE,
    "password" varchar(64)  NOT null,
    first_name varchar(20)  not null,
    surname    varchar(20)  not null,
    "role" user_role not null default 'user'
    );

create table if not exists friendship
(
    id SERIAL primary key,
    sourceId int not null references auth_user(id),
    targetId int not null references auth_user(id),
    status friendship_status not null default 'new',
    created_at timestamp NOT null default now(),
    updated_at timestamp NOT null default now(),
    unique (sourceId, targetId)
    );
