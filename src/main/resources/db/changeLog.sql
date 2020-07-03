--liquibase formatted sql

--changeset beweging:1
create table beweging
(
    id              serial primary key,
    vldatum         date not null ,
    bewegingcode    varchar(20) not null
);