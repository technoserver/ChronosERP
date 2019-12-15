ALTER TABLE venda_devolucao
    DROP CONSTRAINT venda_devolucao_id_venda_cabecalho_fkey;

ALTER TABLE venda_devolucao
    RENAME COLUMN id_venda_cabecalho TO id_venda;

ALTER TABLE venda_devolucao
    add COLUMN codigo_modulo char(3) not null default ('210');

ALTER TABLE venda_devolucao
    add COLUMN gerado_credito char(1) not null default ('S');