CREATE TABLE sync_pendentes
(
  id           SERIAL     NOT NULL,
  id_pdv_caixa INTEGER    NOT NULL,
  id_referente INTEGER    NOT NULL,
  codigo_acao  VARCHAR(3) NOT NULL,
  nome_acao    VARCHAR(30) DEFAULT '',
  PRIMARY KEY (id)
);

ALTER TABLE adm_parametro
  ADD COLUMN frente_caixa BOOLEAN DEFAULT FALSE;