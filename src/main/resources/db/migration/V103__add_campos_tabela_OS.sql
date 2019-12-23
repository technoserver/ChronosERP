ALTER TABLE os_abertura
    add COLUMN tipo_atendimento varchar(2) not null default ('AI');

ALTER TABLE os_abertura
    add COLUMN id_os_retorno integer;

ALTER TABLE os_abertura
    ADD CONSTRAINT os_abertura_id_os_retorno_fkey FOREIGN KEY (id_os_retorno) REFERENCES os_abertura (id);

update os_abertura
set tipo_atendimento = 'AI';