ALTER TABLE empresa_pessoa
  DROP CONSTRAINT empresa_pessoa_pkey;

ALTER TABLE empresa_pessoa
  DROP CONSTRAINT empresa_pessoa_id_empresa_fkey;

ALTER TABLE empresa_pessoa
  DROP CONSTRAINT empresa_pessoa_id_pessoa_fkey;

ALTER TABLE empresa_pessoa
  ADD CONSTRAINT empresa_pessoa_pkey PRIMARY KEY empresa_pessoa (id_empresa, id_pessoa);

CREATE UNIQUE INDEX id_empresa_pessoa_indx
  ON empresa_pessoa (id);

ALTER TABLE empresa_pessoa
  add column empresa_pessoa char(1) default 'N';

update empresa_pessoa
set empresa_pessoa
where id in(select ep.id
            from colaborador c
                   inner join pessoa p on p.id = c.id_pessoa
                   inner join empresa_pessoa ep on ep.id_pessoa = p.id);

update empresa_pessoa
set empresa_pessoa
where id in(select ep.id
            from cliente c
                   inner join pessoa p on p.id = c.id_pessoa
                   inner join empresa_pessoa ep on ep.id_pessoa = p.id);

update empresa_pessoa
set empresa_pessoa
where id in(select ep.id
            from fornecedor f
                   inner join pessoa p on p.id = f.id_pessoa
                   inner join empresa_pessoa ep on ep.id_pessoa = p.id);