alter table os_abertura
    add column status char(1) default 'A';

update os_abertura
set status = 'A'
where id_os_status = 1;
update os_abertura
set status = 'E'
where id_os_status = 5;
update os_abertura
set status = 'F'
where id_os_status = 6;
update os_abertura
set status = 'C'
where id_os_status = 7;

alter table  os_abertura drop column if exists id_os_status;