ALTER TABLE cliente
  ADD COLUMN data_alteracao TIMESTAMP;
ALTER TABLE produto
  ALTER COLUMN data_alteracao TYPE TIMESTAMP;