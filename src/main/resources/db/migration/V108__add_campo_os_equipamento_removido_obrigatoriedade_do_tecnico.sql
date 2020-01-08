ALTER TABLE os_evolucao
    drop column modelo;

ALTER TABLE os_abertura_equipamento
    add column modelo varchar(100) default '';

alter table os_abertura
    alter column id_tecnico drop not null;