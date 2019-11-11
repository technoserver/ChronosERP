alter table estoque_grade
    add column valor_venda decimal(18, 6) default 0;

alter table pdv_venda_detalhe
    add column id_grade integer;

ALTER TABLE pdv_venda_detalhe
    ADD CONSTRAINT pdv_venda_detalhe_id_grade_fkey FOREIGN KEY (id_grade) REFERENCES estoque_grade (id);