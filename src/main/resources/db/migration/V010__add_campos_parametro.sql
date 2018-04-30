ALTER TABLE adm_parametro
  ADD COLUMN tribut_operacao_fiscal_padrao INTEGER;
ALTER TABLE adm_parametro
  ADD COLUMN faturar_venda BOOLEAN DEFAULT FALSE;
ALTER TABLE adm_parametro
  ADD COLUMN permite_estoque_negativo BOOLEAN DEFAULT TRUE;
