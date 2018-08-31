create table estoque_transferencia_cabecalho (
  id                         SERIAL    NOT NULL,
  id_colaborador             integer   not null,
  id_empresa_origem          integer   not null,
  id_empresa_destino         integer   not null,
  id_tribut_operacao_fiscal  integer   not null,
  id_tribut_grupo_tributario integer,
  id_nfe_cabecalho           integer,
  data                       timestamp not null,
  valor_total                decimal(18, 6) default 0,
  status                     char(1)        default 'A',
  observacao                 TEXT,
  PRIMARY KEY (id),
  foreign key (id_colaborador) references colaborador (id),
  foreign key (id_tribut_operacao_fiscal) references tribut_operacao_fiscal (id),
  foreign key (id_tribut_grupo_tributario) references tribut_grupo_tributario (id),
  foreign key (id_empresa_origem) references empresa (id),
  foreign key (id_empresa_destino) references empresa (id),
  foreign key (id_nfe_cabecalho) references nfe_cabecalho (id)
);

create table estoque_transferencia_detalhe (
  id                                 SERIAL  NOT NULL,
  id_estoque_transferencia_cabecalho integer not null,
  id_produto                         integer not null,
  quantidade                         decimal(18, 6) default 0,
  unidade                            varchar(6),
  valor_custo                        decimal(18, 6),
  valor_total                        decimal(18, 6),
  PRIMARY KEY (id),
  foreign key (id_estoque_transferencia_cabecalho) references estoque_transferencia_cabecalho (id)
);