update pdv_venda_cabecalho
set status_venda = 'E'
where status_venda = 'F';
update pdv_venda_cabecalho
set status_venda = 'F'
where status_venda = 'N';