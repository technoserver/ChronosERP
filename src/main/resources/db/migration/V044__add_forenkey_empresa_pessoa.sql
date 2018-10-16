ALTER TABLE empresa_pessoa
  ADD CONSTRAINT empresa_pessoa_id_empresa_fkey FOREIGN KEY (id_empresa) REFERENCES empresa (id);

ALTER TABLE empresa_pessoa
  ADD CONSTRAINT empresa_pessoa_id_pessoa_fkey FOREIGN KEY (id_pessoa) REFERENCES pessoa (id);