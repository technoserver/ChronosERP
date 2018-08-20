ALTER TABLE nfe_cabecalho
  ADD COLUMN valor_total_tributos_federais DECIMAL DEFAULT 0;
ALTER TABLE nfe_cabecalho
  ADD COLUMN valor_total_tributos_estaduais DECIMAL DEFAULT 0;
ALTER TABLE nfe_cabecalho
  ADD COLUMN valor_total_tributos_municipais DECIMAL DEFAULT 0;
ALTER TABLE nfe_cabecalho
  ADD COLUMN valor_total_tributos DECIMAL DEFAULT 0;
update nfe_cabecalho
set valor_total_tributos = (SELECT sum(valor_total_tributos)
                            from nfe_detalhe
                            where id_nfe_cabecalho = nfe_cabecalho.id);
ALTER TABLE nfe_detalhe
  ADD COLUMN valor_total_tributos_federais DECIMAL DEFAULT 0;
ALTER TABLE nfe_detalhe
  ADD COLUMN valor_total_tributos_estaduais DECIMAL DEFAULT 0;
ALTER TABLE nfe_detalhe
  ADD COLUMN valor_total_tributos_municipais DECIMAL DEFAULT 0;