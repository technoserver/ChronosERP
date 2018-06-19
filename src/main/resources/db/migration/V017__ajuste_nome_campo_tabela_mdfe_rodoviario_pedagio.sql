ALTER TABLE mdfe_rodoviario_pedagio
  DROP COLUMN cpf_responsavel;
ALTER TABLE mdfe_rodoviario_pedagio
  RENAME cnpj_responsavel TO cnpj_cpf_responsavel;