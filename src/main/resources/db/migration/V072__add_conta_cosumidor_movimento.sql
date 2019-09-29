CREATE TABLE conta_consumidor
(
    id                serial    not null,
    data_movimentacao timestamp not null,
    tipo_credito      char(1)   not null,
    valor_credito     decimal(18, 6),
    credito_utilizado char(1)   not null,
    codigo            varchar(20),
    cpf_cnpj          varchar(14),
    primary key (id)
);

CREATE TABLE conta_consumidor_item
(
    id                  serial         not null,
    id_conta_consumidor integer        not null,
    id_produto          integer        not null,
    quantidade          decimal(18, 6) not null,
    valor               decimal(18, 6) not null,
    primary key (id),
    foreign key (id_conta_consumidor) references conta_consumidor (id),
    foreign key (id_produto) references produto (id)
);

CREATE TABLE movimento_conta_consumidor
(
    id                  serial    not null,
    id_conta_consumidor integer   not null,
    data_movimento      timestamp not null,
    tipo_movimento      char(1)   not null,
    numero_documento    varchar(50),
    codigo_modulo       char(3),
    valor               decimal(18, 6),
    primary key (id),
    foreign key (id_conta_consumidor) references conta_consumidor (id)
);