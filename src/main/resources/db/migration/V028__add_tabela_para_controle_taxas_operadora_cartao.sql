CREATE TABLE operadora_cartao_taxa (
  id                  SERIAL         NOT NULL,
  id_operadora_cartao INTEGER        NOT NULL,
  intervalor_inicial  INTEGER        NOT NULL  DEFAULT (1),
  intervalor_final    INTEGER        NOT NULL  DEFAULT (1),
  taxa_adm            DECIMAL(18, 2) NOT NULL  DEFAULT (0),
  credito_em          INTEGER        NOT NULL  DEFAULT (30),
  PRIMARY KEY (id)
);