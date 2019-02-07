DROP TABLE IF EXISTS restricao_sistema;

CREATE TABLE restricao_sistema
(
  id                  serial  NOT NULL,
  id_usuario          integer not null,
  desconto_venda      numeric(18, 6),
  desconto_item_venda numeric(18, 16),
  estoque_negativo    boolean default true,
  PRIMARY KEY (id),
  foreign key (id_usuario) REFERENCES usuario (id)
);