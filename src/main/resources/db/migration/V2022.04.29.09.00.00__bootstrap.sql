create sequence hibernate_sequence start with 0;

create table person
(
  id bigint not null primary key,
  create_ts timestamp not null,
  first_name varchar(80) not null,
  last_name varchar(80) not null
);
create index person_firstname on person (last_name);
