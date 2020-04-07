ALTER TABLE adm_parametro
    add column bloquear_cliente_inadiplente char(1) default 'N';

update cliente
set indicador_preco = 'P'
where id_tabela_preco is null
   or indicador_preco = 'T';