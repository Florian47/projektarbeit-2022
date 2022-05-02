create sequence hibernate_sequence start with 1;

create table user
(
id        int primary key,
first_name VARCHAR(255) not null,
last_name  VARCHAR(255) not null,
username  VARCHAR(255) not null unique,
password  VARCHAR(255) not null
);

