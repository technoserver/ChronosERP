alter table os_abertura
    add column id_orcamento integer;

ALTER TABLE os_abertura
    ADD CONSTRAINT os_abertura_id_orcamento_fkey FOREIGN KEY (id_orcamento) REFERENCES venda_orcamento_cabecalho (id);

create table tecnico
(
    id                 serial  not null,
    id_colaborador     integer not null,
    id_comissao_perfil integer,
    comissao           decimal(16, 8) default 0,
    meta_vendas        decimal(16, 8) default 0,
    gerente            char(1)        default 'N',
    taxa_gerente       decimal(16, 8) default 0,
    primary key (id),
    foreign key (id_colaborador) references colaborador (id),
    foreign key (id_comissao_perfil) references comissao_perfil (id)
);

insert into tecnico (id_colaborador)
select distinct c.id
from os_abertura o
         inner join colaborador c on o.id_colaborador = c.id;

ALTER TABLE os_abertura
    DROP CONSTRAINT os_abertura_id_colaborador_fkey;


ALTER TABLE os_abertura
    RENAME COLUMN id_colaborador TO id_tecnico;

update os_abertura
set id_tecnico = (select id from tecnico where tecnico.id_colaborador = os_abertura.id_tecnico);

ALTER TABLE os_abertura
    ADD CONSTRAINT os_abertura_id_tecnico_fkey FOREIGN KEY (id_tecnico) REFERENCES tecnico (id);