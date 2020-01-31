ALTER TABLE os_produto_servico
    add column nome_produto_servico varchar(120) default '';

ALTER TABLE pdv_venda_detalhe
    add column nome_produto varchar(120) default '';

ALTER TABLE venda_detalhe
    add column nome_produto varchar(120) default '';

ALTER TABLE orcamento_detalhe
    add column nome_produto varchar(120) default '';

UPDATE pdv_venda_detalhe
set nome_produto = (select nome from produto where id = pdv_venda_detalhe.id_produto);

UPDATE os_produto_servico
set nome_produto_servico = (select nome from produto where id = os_produto_servico.id_produto);

UPDATE venda_detalhe
set nome_produto = (select nome from produto where id = venda_detalhe.id_produto);

UPDATE orcamento_detalhe
set nome_produto = (select nome from produto where id = orcamento_detalhe.id_produto);

alter table adm_parametro
    add column alterar_nome_produto_venda char(1) default 'N';