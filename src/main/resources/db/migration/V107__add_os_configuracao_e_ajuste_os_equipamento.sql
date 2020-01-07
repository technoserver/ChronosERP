CREATE TABLE os_configuracao
(
    id                          serial  not null,
    id_empresa                  integer not null,
    qtd_dias_uteis_para_entrega integer,
    observacao_padrao           text,
    primary key (id)
);

ALTER TABLE os_evolucao
    add column modelo varchar(100) default '';

ALTER TABLE os_evolucao
    add column responsavel varchar(100) default '';