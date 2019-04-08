ALTER TABLE situacao_for_cli
  ADD COLUMN bloquear char(1) DEFAULT 'S';

update situacao_for_cli
set bloquear = 'N'
where id = 1;

update situacao_for_cli
set bloquear = 'S'
where id = 2;