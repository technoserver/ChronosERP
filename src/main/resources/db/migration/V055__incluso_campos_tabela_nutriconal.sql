ALTER TABLE tabela_nutricional_cabecalho
  ADD COLUMN parte_inteira_medida_caseria integer DEFAULT 1;

ALTER TABLE tabela_nutricional_cabecalho
  ADD COLUMN parte_decimal_medida_caseria integer DEFAULT 0;

ALTER TABLE tabela_nutricional_cabecalho
  ADD COLUMN medida_caseira_utilizada char(2) DEFAULT '00';