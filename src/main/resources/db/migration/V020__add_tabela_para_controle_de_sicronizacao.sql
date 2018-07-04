CREATE TABLE sync_pendentes
(
  id          SERIAL     NOT NULL,
  id_caixa    INTEGER    NOT NULL,
  codigo_acao VARCHAR(3) NOT NULL,
  nome_acao   VARCHAR(30) DEFAULT '',
  PRIMARY KEY (id)
);