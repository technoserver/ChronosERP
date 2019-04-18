ALTER TABLE venda_comissao
    DROP CONSTRAINT venda_comissao_id_venda_cabecalho_fkey;

ALTER TABLE venda_comissao
    RENAME COLUMN id_venda_cabecalho TO numero_documento;

ALTER TABLE venda_comissao
    ALTER COLUMN numero_documento TYPE varchar(50) USING cast(numero_documento as varchar);

update venda_comissao
set numero_documento = 'M210V' || numero_documento;