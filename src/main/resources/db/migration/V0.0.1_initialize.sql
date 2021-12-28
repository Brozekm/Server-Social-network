create table auth_user (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           email varchar(254) NOT NULL UNIQUE,
                           "password" varchar(64) NOT null,
                           first_name varchar(20) not null,
                           surname varchar(20) not null
);

create table auth_role(
                          id UUID primary key default gen_random_uuid(),
                          rolename varchar(20) not null unique
);

create table auth_user_role(
                               user_id UUID not null references auth_user(id),
                               role_id UUID not null references auth_role(id),
                               unique (user_id, role_id)
);