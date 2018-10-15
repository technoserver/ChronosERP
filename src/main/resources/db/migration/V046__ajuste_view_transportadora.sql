CREATE OR REPLACE VIEW view_pessoa_transportadora AS
  SELECT t.id,
         t.id_pessoa,
         t.data_cadastro,
         t.observacao,
         e.logradouro,
         e.numero,
         e.complemento,
         e.bairro,
         e.cidade,
         e.cep,
         e.municipio_ibge,
         e.uf,
         e.fone,
         p.nome,
         p.tipo,
         p.email,
         p.site,
         pf.cpf AS cpf_cnpj,
         t.rntrc
  FROM pessoa p
         JOIN pessoa_fisica pf ON pf.id_pessoa = p.id
         JOIN transportadora t ON t.id_pessoa = p.id
         JOIN pessoa_endereco e ON e.id_pessoa = p.id
  WHERE p.transportadora = 'S'
    AND e.principal = 'S'
  UNION
  SELECT t.id,
         t.id_pessoa,
         t.data_cadastro,
         t.observacao,
         e.logradouro,
         e.numero,
         e.complemento,
         e.bairro,
         e.cidade,
         e.cep,
         e.municipio_ibge,
         e.uf,
         e.fone,
         p.nome,
         p.tipo,
         p.email,
         p.site,
         pj.cnpj AS cpf_cnpj,
         t.rntrc
  FROM pessoa p
         JOIN pessoa_juridica pj ON pj.id_pessoa = p.id
         JOIN transportadora t ON t.id_pessoa = p.id
         JOIN pessoa_endereco e ON e.id_pessoa = p.id
  WHERE p.transportadora = 'S'
    AND e.principal = 'S';
