CREATE TABLE mdfe_xml
(
  id                SERIAL  NOT NULL,
  id_mdfe_cabecalho INTEGER NOT NULL,
  xml               BLOB    NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_mdfe_cabecalho) REFERENCES mdfe_cabecalho (id)
);
CREATE TABLE mdfe_configuracao
(
  id                          SERIAL     NOT NULL,
  id_empresa                  INTEGER    NOT NULL,
  rntrc                       VARCHAR(8) NOT NULL,
  certificado_digital_serie   VARCHAR(100),
  certificado_digital_caminho TEXT,
  certificado_digital_senha   VARCHAR(100),
  caminho_logomarca           TEXT,
  caminho_schemas             TEXT,
  webservice_uf               CHARACTER(2),
  webservice_ambiente         INTEGER,
  email_servidor_smtp         VARCHAR(100),
  email_porta                 INTEGER,
  email_usuario               VARCHAR(100),
  email_senha                 VARCHAR(100),
  email_assunto               VARCHAR(100),
  email_autentica_ssl         CHAR(1),
  email_texto                 TEXT,
  observacao_padrao           TEXT,
  PRIMARY KEY (id),
  FOREIGN KEY (id_empresa) REFERENCES empresa (id)
);

