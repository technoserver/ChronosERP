ALTER TABLE venda_comissao
    RENAME TO comissao;

ALTER TABLE comissao
    RENAME COLUMN id_vendedor TO id_colaborador;

ALTER TABLE comissao
    DROP CONSTRAINT venda_comissao_id_vendedor_fkey;

ALTER TABLE comissao
    ADD CONSTRAINT comissao_id_colaborador_fkey FOREIGN KEY (id_colaborador) REFERENCES colaborador (id);

ALTER TABLE comissao
    ADD COLUMN codigo_modulo char(3);

UPDATE comissao
set codigo_modulo = '210'
where id in (SELECT id FROM comissao WHERE numero_documento LIKE '%M210%');

UPDATE comissao
set codigo_modulo = '200'
where id in (SELECT id FROM comissao WHERE numero_documento LIKE '%M200%');

update comissao
set numero_documento = (SELECT SUBSTRING((select c.numero_documento from comissao c where c.id = comissao.id), 6));