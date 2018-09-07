update produto
set servico = 'N'
where servico is null;
update produto
set valor_venda = 0
where valor_venda is null and tipo = 'V';