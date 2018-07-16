CREATE TABLE certificado_configuracao (
  id         SERIAL NOT NULL,
  id_empresa INTEGER,
  serie      VARCHAR(100),
  senha      VARCHAR(100),
  caminho    TEXT,
  tipo       CHAR(2),
  PRIMARY KEY (id),
  FOREIGN KEY (id_empresa) REFERENCES empresa (id)
);

INSERT INTO certificado_configuracao (id_empresa, serie, senha, caminho, tipo) SELECT
                                                                                 id_empresa,
                                                                                 certificado_digital_serie,
                                                                                 certificado_digital_senha,
                                                                                 certificado_digital_caminho,
                                                                                 'A1'
                                                                               FROM nfe_configuracao;

CREATE TABLE email_configuracao (
  id            SERIAL NOT NULL,
  id_empresa    INTEGER,
  tipo          CHAR(2),
  servidor_smtp VARCHAR(100),
  porta         INTEGER,
  usuario       VARCHAR(100),
  senha         VARCHAR(100),
  assunto       VARCHAR(100),
  autentica_ssl CHAR(1) DEFAULT 'N',
  texto         TEXT,
  PRIMARY KEY (id),
  FOREIGN KEY (id_empresa) REFERENCES empresa (id)
);

ALTER TABLE nfe_configuracao
  DROP COLUMN email_servidor_smtp,
  DROP COLUMN email_porta,
  DROP COLUMN email_usuario,
  DROP COLUMN email_senha,
  DROP COLUMN email_assunto,
  DROP COLUMN email_autentica_ssl,
  DROP COLUMN email_texto,
  DROP COLUMN certificado_digital_serie,
  DROP COLUMN certificado_digital_senha,
  DROP COLUMN certificado_digital_caminho;
ALTER TABLE pdv_configuracao
  DROP COLUMN certificado_digital_serie,
  DROP COLUMN certificado_digital_senha,
  DROP COLUMN certificado_digital_caminho,
  DROP COLUMN email_servidor_smtp,
  DROP COLUMN email_porta,
  DROP COLUMN email_usuario,
  DROP COLUMN email_senha,
  DROP COLUMN email_assunto,
  DROP COLUMN email_autentica_ssl,
  DROP COLUMN email_texto;
ALTER TABLE mdfe_configuracao
  DROP COLUMN certificado_digital_serie,
  DROP COLUMN certificado_digital_senha,
  DROP COLUMN certificado_digital_caminho,
  DROP COLUMN email_servidor_smtp,
  DROP COLUMN email_porta,
  DROP COLUMN email_usuario,
  DROP COLUMN email_senha,
  DROP COLUMN email_assunto,
  DROP COLUMN email_autentica_ssl,
  DROP COLUMN email_texto;
ALTER TABLE cte_configuracao
  DROP COLUMN certificado_digital_serie,
  DROP COLUMN certificado_digital_senha,
  DROP COLUMN certificado_digital_caminho,
  DROP COLUMN email_servidor_smtp,
  DROP COLUMN email_porta,
  DROP COLUMN email_usuario,
  DROP COLUMN email_senha,
  DROP COLUMN email_assunto,
  DROP COLUMN email_autentica_ssl,
  DROP COLUMN email_texto;