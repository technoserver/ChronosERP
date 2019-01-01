CREATE TABLE produto_atributo
(
  id    serial NOT NULL,
  sigla varchar(10),
  nome  varchar(100),
  PRIMARY KEY (id)
);

CREATE TABLE produto_atributo_detalhe
(
  id                  serial NOT NULL,
  id_produto_atributo integer,
  nome                varchar(100),
  PRIMARY KEY (id),
  FOREIGN KEY (id_produto_atributo) REFERENCES produto_atributo (id)
);

DROP TABLE IF EXISTS estoque_grade;

CREATE TABLE estoque_grade
(
  id                          serial NOT NULL,
  id_empresa_produto          integer,
  id_estoque_marca            integer,
  id_estoque_sabor            integer,
  id_estoque_tamanho          integer,
  id_estoque_cor              integer,
  id_produto_atributo_detalhe integer,
  codigo                      character varying(50),
  quantidade                  numeric(18, 6),
  verificado                  numeric(18, 6),
  PRIMARY KEY (id),
  FOREIGN KEY (id_empresa_produto) REFERENCES empresa_produto (id),
  FOREIGN KEY (id_estoque_cor) REFERENCES estoque_cor (id),
  FOREIGN KEY (id_estoque_marca) REFERENCES estoque_marca (id),
  FOREIGN KEY (id_estoque_sabor) REFERENCES estoque_sabor (id),
  FOREIGN KEY (id_estoque_tamanho) REFERENCES estoque_tamanho (id),
  FOREIGN KEY (id_produto_atributo_detalhe) REFERENCES produto_atributo_detalhe (id)
);

alter table produto
  add column possui_grade boolean default false;

alter table produto
  add column id_produto_atributo integer;

ALTER TABLE produto
  ADD CONSTRAINT produto_id_produto_atributo_fkey FOREIGN KEY (id_produto_atributo) REFERENCES produto_atributo (id);