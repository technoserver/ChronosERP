CREATE TABLE operadora_cartao_taxa (
  id                  SERIAL         NOT NULL,
  id_operadora_cartao INTEGER        NOT NULL,
  intervalo_inicial   INTEGER        NOT NULL  DEFAULT (1),
  intervalo_final     INTEGER        NOT NULL  DEFAULT (1),
  taxa_adm            DECIMAL(18, 2) NOT NULL  DEFAULT (0),
  credito_em          INTEGER        NOT NULL  DEFAULT (30),
  PRIMARY KEY (id),
  FOREIGN KEY (id_operadora_cartao) REFERENCES operadora_cartao (id)
);

ALTER TABLE operadora_cartao
  DROP COLUMN IF EXISTS taxa_adm;
ALTER TABLE IF EXISTS operadora_cartao
  DROP COLUMN IF EXISTS quantida_maxima_parcela;