CREATE OR REPLACE VIEW view_quantidade_produto_vendido AS

SELECT p.id,v.id_empresa, p.nome,i.quantidade,i.valor_unitario,i.valor_totaL,v.data_venda,COALESCE(p.custo_unitario ,0) as custo

FROM produto p
     INNER JOIN venda_detalhe i ON i.id_produto = p.id
     INNER JOIN venda_cabecalho v ON i.id_venda_cabecalho = v.id
WHERE v.situacao not in ('D','P','X','V')

union

SELECT p.id,v.id_empresa ,p.nome ,i.quantidade,i.valor_unitario,i.valor_totaL,v.data_hora_venda,COALESCE(p.custo_unitario ,0) as custo

FROM produto p
INNER JOIN pdv_venda_detalhe i ON i.id_produto = p.id
INNER JOIN pdv_venda_cabecalho v ON i.id_pdv_venda_cabecalho = v.id
WHERE v.status_venda = 'F';