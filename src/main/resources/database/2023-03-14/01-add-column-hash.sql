--liquibase formatted sql
--changeset rsitek:23
alter table users add hash varchar(120);
--changeset rsitek:24
alter table users add hash_date datetime;