--liquibase formatted sql
--changeset rsitek:2
alter table product add image varchar(128) after currency;