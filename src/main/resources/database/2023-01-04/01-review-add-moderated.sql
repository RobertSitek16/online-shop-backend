--liquibase formatted sql
--changeset rsitek:9
alter table review add moderated boolean default false;