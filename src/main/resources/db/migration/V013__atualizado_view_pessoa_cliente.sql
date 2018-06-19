drop view view_pessoa_cliente;
CREATE OR REPLACE VIEW view_pessoa_cliente AS
  SELECT
    c.id,
    c.id_operacao_fiscal,
    c.id_pessoa,
    c.id_atividade_for_cli,
    c.id_situacao_for_cli,
    c.desde,
    c.data_cadastro,
    c.data_alteracao,
    c.observacao,
    c.conta_tomador,
    c.gera_financeiro,
    c.indicador_preco,
    c.porcento_desconto,
    c.forma_desconto,
    c.limite_credito,
    c.tipo_frete,
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
    pf.rg  AS rg_ie
  FROM pessoa p
    JOIN pessoa_fisica pf ON pf.id_pessoa = p.id
    JOIN cliente c ON c.id_pessoa = p.id
    JOIN pessoa_endereco e ON e.id_pessoa = p.id
  WHERE p.cliente = 'S' AND e.principal = 'S'
  UNION
  SELECT
    c.id,
    c.id_operacao_fiscal,
    c.id_pessoa,
    c.id_atividade_for_cli,
    c.id_situacao_for_cli,
    c.desde,
    c.data_cadastro,
    c.data_alteracao,
    c.observacao,
    c.conta_tomador,
    c.gera_financeiro,
    c.indicador_preco,
    c.porcento_desconto,
    c.forma_desconto,
    c.limite_credito,
    c.tipo_frete,
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
    pj.cnpj               AS cpf_cnpj,
    pj.inscricao_estadual AS rg_ie
  FROM pessoa p
    JOIN pessoa_juridica pj ON pj.id_pessoa = p.id
    JOIN cliente c ON c.id_pessoa = p.id
    JOIN pessoa_endereco e ON e.id_pessoa = p.id
  WHERE p.cliente = 'S' AND e.principal = 'S';
