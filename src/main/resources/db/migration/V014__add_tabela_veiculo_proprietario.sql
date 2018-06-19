CREATE TABLE proprietario_veiculo
(
  id       SERIAL NOT NULL,
  nome     VARCHAR(60),
  cpf_cnpj VARCHAR(14),
  uf       CHAR(2),
  ie       VARCHAR(30),
  rntrc    VARCHAR(8),
  tipo     INTEGER,
  PRIMARY KEY (id)
);

CREATE TABLE veiculo
(
  id                      SERIAL NOT NULL,
  id_proprietario_veiculo INTEGER,
  codigo_interno          VARCHAR(10),
  renavan                 VARCHAR(11),
  placa                   VARCHAR(7),
  tara                    INTEGER,
  capacidade_kg           INTEGER,
  capacidade_m3           INTEGER,
  tipo_rodado             CHAR(2),
  tipo_carroceria         CHAR(2),
  uf                      CHAR(2),
  tipo_propriedade        CHAR(1),
  tipo_veiculo            CHAR(1),
  PRIMARY KEY (id),
  FOREIGN KEY (id_proprietario_veiculo) REFERENCES proprietario_veiculo (id)
);