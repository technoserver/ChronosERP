ALTER TABLE pdv_operador
  ADD COLUMN id_colaborador INTEGER;
ALTER TABLE pdv_operador
  ADD CONSTRAINT pdv_operador_id_colaborador_fkey FOREIGN KEY (id_colaborador) REFERENCES colaborador (id);
update pdv_operador
set id_colaborador = 1;
INSERT into aviso_sistema (aviso) values (
  'Foi incluso um vinculo do operador com o colaborador e colocador por default o colaborador 1, para que o sistema possar gerar informações mais precisa por favor alterar o colaborador do operador de caixa')