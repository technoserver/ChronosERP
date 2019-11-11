alter table venda_detalhe
    add column id_grade integer;

ALTER TABLE venda_detalhe
    ADD CONSTRAINT venda_detalhe_id_grade_fkey FOREIGN KEY (id_grade) REFERENCES estoque_grade (id);

ALTER TABLE venda_orcamento_detalhe
    RENAME TO orcamento_detalhe;

ALTER TABLE venda_orcamento_cabecalho
    RENAME TO orcamento_cabecalho;

alter table orcamento_detalhe
    add column id_grade integer;

ALTER TABLE orcamento_detalhe
    ADD CONSTRAINT orcamento_detalhe_id_grade_fkey FOREIGN KEY (id_grade) REFERENCES estoque_grade (id);

ALTER SEQUENCE venda_orcamento_detalhe_id_seq RENAME TO orcamento_detalhe_id_seq;

ALTER SEQUENCE venda_orcamento_cabecalho_id_seq RENAME TO orcamento_cabecalho_id_seq;