ALTER TABLE pdv_configuracao
  RENAME obervacao_padrao TO observacao_padrao;
ALTER TABLE pdv_venda_cabecalho
  ADD COLUMN data_hora_sincronizacao TIMESTAMP;
ALTER TABLE pdv_venda_cabecalho
  DROP COLUMN data_sincronizacao,
  DROP COLUMN hora_sincronizacao;
