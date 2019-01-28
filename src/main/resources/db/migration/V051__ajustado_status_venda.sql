update venda_cabecalho
set situacao = 'E'
where situacao = 'F';
update venda_cabecalho
set situacao = 'F'
where situacao = 'N';