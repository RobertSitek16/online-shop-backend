--liquibase formatted sql
--changeset rsitek:27
alter table product add sale_price decimal(9,2);