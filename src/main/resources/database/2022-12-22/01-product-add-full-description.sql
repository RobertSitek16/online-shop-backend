--liquibase formatted sql
--changeset rsitek:3
alter table product add full_description text default null after description;