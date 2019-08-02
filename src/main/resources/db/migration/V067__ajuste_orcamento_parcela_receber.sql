update venda_orcamento_cabecalho
set situacao = 'F'
where id in (select id_venda_orcamento_cabecalho from venda_cabecalho)
  and situacao = 'A';

alter table fin_parcela_recebimento
    add column id_pdv_movimento integer;

ALTER TABLE fin_parcela_recebimento
    ADD CONSTRAINT fin_parcela_recebimento_id_pdv_movimento_fkey FOREIGN KEY (id_pdv_movimento) REFERENCES pdv_movimento (id);