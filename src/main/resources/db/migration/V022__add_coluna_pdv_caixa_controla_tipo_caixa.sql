ALTER TABLE pdv_caixa
  ADD COLUMN web CHAR(1) DEFAULT 'N';
UPDATE pdv_caixa
SET web = 'N';
UPDATE pdv_caixa
SET web = 'S'
WHERE id = 1;