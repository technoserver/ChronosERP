alter table adm_parametro
    add column sugerir_valor_compra_entrada char(1) default 'N';

alter table pcp_op_cabecalho
    add column status char(1);