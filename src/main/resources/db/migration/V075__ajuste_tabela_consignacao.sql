DROP TABLE IF EXISTS venda_consiguinada_cabecalho;
DROP TABLE IF EXISTS venda_consiguinada_detalhe;

DROP TABLE IF EXISTS venda_consignada_detalhe;
DROP TABLE IF EXISTS venda_consignada_cabecalho;

create table venda_consignada_cabecalho
(
    id                    serial         not null,
    id_empresa            integer        not null,
    id_cliente            integer        not null,
    id_condicao_pagamento integer        not null,
    id_vendedor           integer        not null,
    id_venda_cabecalho    integer,
    data_saida            timestamp      not null default now(),
    data_devolucao        timestamp,
    valor_subtotal        decimal(16, 6) not null default 0,
    taxa_comissao         decimal(16, 6)          default 0,
    taxa_desconto         decimal(16, 6)          default 0,
    valor_desconto        decimal(16, 6)          default 0,
    valor_total           decimal(16, 6) not null default 0,
    local_entrega         varchar(255),
    tipo_frete            char(1),
    status                integer        not null default 0,
    primary key (id),
    foreign key (id_empresa) references empresa (id),
    foreign key (id_cliente) references cliente (id),
    foreign key (id_condicao_pagamento) references venda_condicoes_pagamento (id),
    foreign key (id_venda_cabecalho) references venda_cabecalho (id),
    foreign key (id_vendedor) references vendedor (id)
);

create table venda_consignada_detalhe
(
    id                            serial         not null,
    id_venda_consignada_cabecalho integer        not null,
    id_produto                    integer        not null,
    un                            varchar(30)    not null,
    quantidade                    decimal(18, 6) not null default 0,
    quantidade_vendida            decimal(18, 6)          default 0,
    quantidade_devolvida          decimal(18, 6)          default 0,
    valor_unitario                decimal(18, 6) not null default 0,
    valor_subtotal                decimal(18, 6) not null default 0,
    taxa_comissao                 decimal(16, 6)          default 0,
    taxa_desconto                 decimal(16, 6) not null default 0,
    valor_desconto                decimal(16, 6) not null default 0,
    valor_total                   decimal(16, 6) not null default 0,
    status                        integer        not null default 0,
    primary key (id),
    foreign key (id_venda_consignada_cabecalho) references venda_consignada_cabecalho (id),
    foreign key (id_produto) references produto (id)
);

alter table adm_parametro
    add column os_gerar_movimento_caixa char(1) not null default 'N';

alter table os_abertura
    add column id_movimento integer;

ALTER TABLE os_abertura
    ADD CONSTRAINT os_abertura_id_movimento_fkey FOREIGN KEY (id_movimento) REFERENCES pdv_movimento (id);