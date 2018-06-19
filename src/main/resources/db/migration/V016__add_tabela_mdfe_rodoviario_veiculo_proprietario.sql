ALTER TABLE mdfe_rodoviario_veiculo
  DROP COLUMN proprietario_cpf,
  DROP COLUMN proprietario_cnpj,
  DROP COLUMN proprietario_rntrc,
  DROP COLUMN proprietario_nome,
  DROP COLUMN proprietario_ie,
  DROP COLUMN proprietario_tipo;


CREATE TABLE mdfe_rodoviario_proprietario_veiculo
(
  id                         SERIAL  NOT NULL,
  id_mdfe_rodoviario_veiculo INTEGER NOT NULL,
  cpf_cnpj                   VARCHAR(14),
  rntrc                      VARCHAR(8),
  nome                       VARCHAR(60),
  ie                         VARCHAR(14),
  tipo                       INTEGER,
  uf                         CHAR(2),
  PRIMARY KEY (id),
  FOREIGN KEY (id_mdfe_rodoviario_veiculo) REFERENCES mdfe_rodoviario_veiculo (id)
);