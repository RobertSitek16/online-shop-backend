--liquibase formatted sql
--changeset rsitek:26
alter table `order` add order_hash varchar(12);