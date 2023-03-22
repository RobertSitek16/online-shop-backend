--liquibase formatted sql
--changeset rsitek:25
update payment set default_payment=false;
insert into payment(name, type, default_payment, note)
values ('Online payment Przelewy 24', 'P24_ONLINE', true, '');