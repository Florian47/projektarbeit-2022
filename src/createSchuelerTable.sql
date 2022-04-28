create table SCHUELER
(
    ID        INTEGER default AUTOINCREMENT: start 1 increment 1 generated always as identity
        constraint SCHUELER_PK
        primary key,
    FIRSTNAME VARCHAR(255) not null,
    LASTNAME  VARCHAR(255) not null,
    USERNAME  VARCHAR(255) not null
        constraint SCHUELER_USERNAME_UINDEX
            unique,
    PASSWORD  VARCHAR(255) not null
);
