create table operadora_cartao_taxa(
  id          SERIAL NOT NULL,
  id_operadora_cartao INTEGER NOT NULL,
  intervalor_inicial INTEGER NOT NULL  DEFAULT (1) ,
  intervalor_final INTEGER NOT NULL  DEFAULT (1),
  taxa_adm DECIMAL (18,2) NOT NULL  default(0),
  credito_em integer NOT NULL  DEFAULT (30),
  primary KEY (id)
);