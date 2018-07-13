UPDATE operadora_cartao
SET bandeira = '01';
ALTER TABLE operadora_cartao
  ALTER COLUMN bandeira TYPE CHAR(2);