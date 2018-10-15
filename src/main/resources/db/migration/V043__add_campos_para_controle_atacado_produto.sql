ALTER TABLE produto
  ADD COLUMN valor_venda_atacado DECIMAL(18, 6) DEFAULT 0;
ALTER TABLE produto
  ADD COLUMN quantidade_venda_atacado DECIMAL(18, 6) DEFAULT 0;