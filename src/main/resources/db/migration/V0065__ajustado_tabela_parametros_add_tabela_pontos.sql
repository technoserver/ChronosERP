alter table restricao_sistema
    add column altera_preco_na_venda char(1) default 'S';
alter table restricao_sistema
    add column altera_preco_venda char(1) default 'S';

alter table adm_parametro
    add column controla_ponto_sobre_venda char(1) default 'N';
alter table adm_parametro
    add column dias_validade_ponto_sobre_venda char(1) default 'A';
alter table adm_parametro
    add column valo_ponto_sobre_venda decimal(18, 6);


create table ponto_sobre_venda_cliente
(
    id         serial              not null,
    id_cliente integer             not null,
    pontos     integer             not null,
    validade   timestamp           not null,
    resgatado  char(1) default 'N' not null,
    primary key (id),
    foreign key (id_cliente) references cliente (id)
);