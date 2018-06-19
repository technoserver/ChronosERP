ALTER TABLE mdfe_cabecalho  ADD COLUMN status_mdfe INTEGER;
ALTER TABLE mdfe_cabecalho  ADD COLUMN codigo_status_transmissao INTEGER;
ALTER TABLE mdfe_cabecalho  ADD COLUMN data_hora_processamento TIMESTAMP;
ALTER TABLE mdfe_cabecalho  ADD COLUMN justificativa_cancelamento VARCHAR (255);
ALTER TABLE mdfe_cabecalho  ADD COLUMN descricao_motivo_resposta VARCHAR(255);
ALTER TABLE mdfe_cabecalho  ADD COLUMN informacoes_add_contribuinte text;
ALTER TABLE mdfe_cabecalho  ADD COLUMN informacoes_add_fisco text;
