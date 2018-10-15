ALTER TABLE veiculo
  DROP CONSTRAINT veiculo_id_proprietario_veiculo_fkey;
ALTER TABLE veiculo
  RENAME id_proprietario_veiculo TO id_transportadora;
ALTER TABLE veiculo
  ADD CONSTRAINT veiculo_id_transportadora_fkey FOREIGN KEY (id_transportadora) REFERENCES transportadora (id);

DROP TABLE IF EXISTS proprietario_veiculo;

ALTER TABLE transportadora
  add column rntrc varchar(8);
ALTER TABLE transportadora
  add column tipo integer default 0;