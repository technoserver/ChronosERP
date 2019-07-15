alter table os_abertura
    drop column if exists status;

alter table os_abertura
    add column status integer default 1;

update os_abertura
set status = 1
where id_os_status = 1;

update os_abertura
set status = 12
where id_os_status = 5;

update os_abertura
set status = 13
where id_os_status = 6;

update os_abertura
set status = 11
where id_os_status = 7;

alter table os_abertura
    drop column if exists id_os_status;