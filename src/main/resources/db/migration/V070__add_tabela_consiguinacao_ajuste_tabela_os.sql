DROP TABLE IF EXISTS venda_consiguinada_cabecalho;
DROP TABLE IF EXISTS venda_consiguinada_detalhe;

create table venda_consiguinada_cabecalho
(
    id                    serial         not null,
    id_empresa            integer        not null,
    id_cliente            integer        not null,
    id_condicao_pagamento integer        not null,
    id_venda_cabecalho    integer        not null,
    data_saida            timestamp      not null default now(),
    valor_subtotal        decimal(16, 6) not null default 0,
    taxa_comissao         decimal(16, 6) not null default 0,
    valor_comissao        decimal(16, 6) not null default 0,
    taxa_desconto         decimal(16, 6) not null default 0,
    valor_desconto        decimal(16, 6) not null default 0,
    valor_total           decimal(16, 6) not null default 0,
    local_entrega         varchar(255),
    tipo_frete            char(1),
    status                integer        not null default 0,
    primary key (id),
    foreign key (id_empresa) references empresa (id),
    foreign key (id_cliente) references cliente (id),
    foreign key (id_condicao_pagamento) references venda_condicoes_pagamento (id),
    foreign key (id_venda_cabecalho) references venda_cabecalho (id)
);

create table venda_consiguinada_detalhe
(
    id                              serial         not null,
    id_venda_consiguinada_cabecalho integer        not null,
    id_produto                      integer        not null,
    un                              varchar(30)    not null,
    quantidade                      decimal(18, 6) not null default 0,
    valor_subtotal                  decimal(18, 6) not null default 0,
    taxa_desconto                   decimal(16, 6) not null default 0,
    valor_desconto                  decimal(16, 6) not null default 0,
    valor_total                     decimal(16, 6) not null default 0,
    status                          integer        not null default 0
);

alter table os_abertura
    add column id_vendedor integer;

alter table os_abertura
    add column valor_comissao decimal(16, 6) not null default 0;

ALTER TABLE os_abertura
    ADD CONSTRAINT os_abertura_id_vendedor_fkey FOREIGN KEY (id_vendedor) REFERENCES vendedor (id);