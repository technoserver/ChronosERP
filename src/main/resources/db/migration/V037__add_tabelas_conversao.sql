create table converter_cfop (
  id           serial  NOT NULL,
  tipo         char(1) default 'E',
  cfop_origem  integer NOT NULL,
  cfop_destino integer NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE converter_cst
(
  id          serial  NOT NULL,
  tipo        char(1) default 'E',
  tipo_cst    integer NOT NULL,
  cst_origem  varchar(3),
  cst_destino varchar(3),
  PRIMARY KEY (id)
)