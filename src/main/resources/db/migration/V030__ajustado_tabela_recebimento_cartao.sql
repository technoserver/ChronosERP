ALTER TABLE fin_parcela_recebimento_cartao
  DROP CONSTRAINT fin_recebimento_cartao_id_operadora_cartao_fkey;
ALTER TABLE fin_parcela_recebimento_cartao
  RENAME id_operadora_cartao TO id_conta_caixa;
ALTER TABLE fin_parcela_recebimento_cartao
  ADD CONSTRAINT fin_parcela_recebimento_cartao_id_conta_caixa_fkey FOREIGN KEY (id_conta_caixa) REFERENCES conta_caixa (id);