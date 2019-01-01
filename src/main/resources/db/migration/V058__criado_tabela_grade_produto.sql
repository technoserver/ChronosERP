CREATE TABLE produto_grade
(
  id   serial NOT NULL,
  nome varchar(100),
  PRIMARY KEY (id)
);

CREATE TABLE produto_grade_detalhe
(
  id                  serial  NOT NULL,
  id_produto_grade    integer not null,
  id_produto_atributo integer not null,
  PRIMARY KEY (id),
  FOREIGN KEY (id_produto_atributo) REFERENCES produto_atributo (id),
  FOREIGN KEY (id_produto_grade) REFERENCES produto_grade (id)
);

alter table produto
  drop column id_produto_atributo;

alter table produto
  add column id_produto_grade integer;

ALTER TABLE produto
  ADD CONSTRAINT produto_id_produto_grade_fkey FOREIGN KEY (id_produto_grade) REFERENCES produto_grade (id);