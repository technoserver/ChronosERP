ALTER TABLE os_abertura ALTER COLUMN status TYPE varchar(2);

update os_abertura
set status = '1'
where status = 'A';
update os_abertura
set status = '12'
where status = 'E';
update os_abertura
set status = '13'
where status = 'F';
update os_abertura
set status = '11'
where status = 'C';