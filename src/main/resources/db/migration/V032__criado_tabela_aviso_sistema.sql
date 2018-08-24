CREATE TABLE aviso_sistema
(
  id               SERIAL NOT NULL,
  confirmado       char(1) default 'N',
  confirmado_por   varchar(150),
  data_confirmacao TIMESTAMP,
  aviso            TEXT,
  PRIMARY KEY (id)
);