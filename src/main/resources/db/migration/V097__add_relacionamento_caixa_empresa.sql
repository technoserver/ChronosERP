alter table pdv_caixa
    add column id_empresa integer;

ALTER TABLE pdv_caixa
    ADD CONSTRAINT pdv_caixa_id_empresa_fkey FOREIGN KEY (id_empresa) REFERENCES empresa (id);

update pdv_caixa
set id_empresa = 1;

ALTER TABLE pdv_caixa
    ALTER COLUMN id_empresa SET NOT NULL;