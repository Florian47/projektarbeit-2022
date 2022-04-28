create table SCHUELER
(
    ID        INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)
        constraint SCHUELER_PK
            primary key,
    FIRSTNAME VARCHAR(255) not null,
    LASTNAME  VARCHAR(255) not null,
    USERNAME  VARCHAR(255) not null
        constraint SCHUELER_USERNAME_UINDEX
            unique,
    PASSWORD  VARCHAR(255) not null
);
