ALTER TABLE situacao_for_cli
    add column dias_bloqueio integer;

ALTER TABLE adm_parametro
    add column situacao_cliente_bloqueado integer;