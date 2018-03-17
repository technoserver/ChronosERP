CREATE OR REPLACE VIEW view_dados_empresa AS
  SELECT
    empresa.id,
    empresa.razao_social         AS empresa_razao_social,
    empresa.nome_fantasia        AS empresa_nome_fantasia,
    empresa.cnpj                 AS empresa_cnpj,
    empresa.inscricao_estadual   AS empresa_inscricao_estadual,
    empresa.imagem_logotipo      AS empresa_imagem_logotipo,
    empresa_endereco.logradouro  AS empresa_endereco_logradouro,
    empresa_endereco.numero      AS empresa_endereco_numero,
    empresa_endereco.complemento AS empresa_endereco_complemento,
    empresa_endereco.bairro      AS empresa_endereco_bairro,
    empresa_endereco.cidade      AS empresa_endereco_cidade,
    empresa_endereco.cep         AS empresa_endereco_cep,
    empresa_endereco.fone        AS empresa_endereco_fone,
    empresa_endereco.uf          AS empresa_endereco_uf,
    empresa.email                AS empresa_email
  FROM empresa empresa
    JOIN empresa_endereco empresa_endereco ON empresa.id = empresa_endereco.id_empresa
  WHERE empresa_endereco.principal = 'S';

CREATE OR REPLACE VIEW VIEW_PESSOA AS
  SELECT
    p.id,
    p.nome,
    pf.cpf AS cpf_cnpj,
    pf.rg  AS rg_ie,
    p.tipo,
    e.logradouro,
    e.numero,
    e.complemento,
    e.bairro,
    e.cidade,
    e.cep,
    e.municipio_ibge,
    e.uf,
    e.fone,
    p.email,
    p.site
  FROM
    pessoa p
    INNER JOIN pessoa_fisica pf ON (pf.id_pessoa = p.id)
    INNER JOIN pessoa_endereco e ON (e.id_pessoa = p.id)
  WHERE
    e.principal = 'S'

  UNION

  SELECT
    p.id,
    p.nome,
    pj.cnpj               AS cpf_cnpj,
    pj.inscricao_estadual AS rg_ie,
    p.tipo,
    e.logradouro,
    e.numero,
    e.complemento,
    e.bairro,
    e.cidade,
    e.cep,
    e.municipio_ibge,
    e.uf,
    e.fone,
    p.email,
    p.site
  FROM
    pessoa p
    INNER JOIN pessoa_juridica pj ON (pj.id_pessoa = p.id)
    INNER JOIN pessoa_endereco e ON (e.id_pessoa = p.id)
  WHERE
    e.principal = 'S';


CREATE OR REPLACE VIEW view_pessoa_cliente AS
  SELECT
    c.id,
    c.id_operacao_fiscal,
    c.id_pessoa,
    c.id_atividade_for_cli,
    c.id_situacao_for_cli,
    c.desde,
    c.data_cadastro,
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


CREATE OR REPLACE VIEW view_pessoa_transportadora AS
  SELECT
    t.id,
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
    pf.cpf AS cpf_cnpj
  FROM pessoa p
    JOIN pessoa_fisica pf ON pf.id_pessoa = p.id
    JOIN transportadora t ON t.id_pessoa = p.id
    JOIN pessoa_endereco e ON e.id_pessoa = p.id
  WHERE p.transportadora = 'S' AND e.principal = 'S'
  UNION
  SELECT
    t.id,
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
    pj.cnpj AS cpf_cnpj
  FROM pessoa p
    JOIN pessoa_juridica pj ON pj.id_pessoa = p.id
    JOIN transportadora t ON t.id_pessoa = p.id
    JOIN pessoa_endereco e ON e.id_pessoa = p.id
  WHERE p.transportadora = 'S' AND e.principal = 'S';


CREATE OR REPLACE VIEW view_pessoa_fornecedor AS
  SELECT
    f.id,
    f.id_pessoa,
    f.id_atividade_for_cli,
    f.id_situacao_for_cli,
    f.desde,
    f.optante_simples_nacional,
    f.localizacao,
    f.data_cadastro,
    f.sofre_retencao,
    f.cheque_nominal_a,
    f.observacao,
    f.conta_remetente,
    f.prazo_medio_entrega,
    f.gera_faturamento,
    f.num_dias_primeiro_vencimento,
    f.num_dias_intervalo,
    f.quantidade_parcelas,
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
    pf.cpf AS cpf_cnpj
  FROM pessoa p
    JOIN pessoa_fisica pf ON pf.id_pessoa = p.id
    JOIN fornecedor f ON f.id_pessoa = p.id
    JOIN pessoa_endereco e ON e.id_pessoa = p.id
  WHERE p.fornecedor = 'S' AND e.principal = 'S'
  UNION
  SELECT
    f.id,
    f.id_pessoa,
    f.id_atividade_for_cli,
    f.id_situacao_for_cli,
    f.desde,
    f.optante_simples_nacional,
    f.localizacao,
    f.data_cadastro,
    f.sofre_retencao,
    f.cheque_nominal_a,
    f.observacao,
    f.conta_remetente,
    f.prazo_medio_entrega,
    f.gera_faturamento,
    f.num_dias_primeiro_vencimento,
    f.num_dias_intervalo,
    f.quantidade_parcelas,
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
    pj.cnpj AS cpf_cnpj
  FROM pessoa p
    JOIN pessoa_juridica pj ON pj.id_pessoa = p.id
    JOIN fornecedor f ON f.id_pessoa = p.id
    JOIN pessoa_endereco e ON e.id_pessoa = p.id
  WHERE p.fornecedor = 'S' AND e.principal = 'S';


CREATE OR REPLACE VIEW view_pessoa_colaborador AS
  SELECT
    c.id,
    c.id_sindicato,
    c.id_tipo_admissao,
    c.id_situacao_colaborador,
    c.id_pessoa,
    c.id_tipo_colaborador,
    c.id_nivel_formacao,
    c.id_cargo,
    c.id_setor,
    c.matricula,
    c.data_cadastro,
    c.data_admissao,
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
    JOIN colaborador c ON c.id_pessoa = p.id
    JOIN pessoa_endereco e ON e.id_pessoa = p.id
  WHERE p.colaborador = 'S' AND e.principal = 'S'
  UNION
  SELECT
    c.id,
    c.id_sindicato,
    c.id_tipo_admissao,
    c.id_situacao_colaborador,
    c.id_pessoa,
    c.id_tipo_colaborador,
    c.id_nivel_formacao,
    c.id_cargo,
    c.id_setor,
    c.matricula,
    c.data_cadastro,
    c.data_admissao,
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
    JOIN colaborador c ON c.id_pessoa = p.id
    JOIN pessoa_endereco e ON e.id_pessoa = p.id
  WHERE p.colaborador = 'S' AND e.principal = 'S';


CREATE OR REPLACE VIEW view_pessoa_cliente_empresa AS
  SELECT
    c.id,
    c.id_operacao_fiscal,
    c.id_pessoa,
    c.id_atividade_for_cli,
    c.id_situacao_for_cli,
    empresa.id                   AS id_empresa,
    c.desde,
    c.data_cadastro,
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
    pf.cpf                       AS cpf_cnpj,
    pf.rg                        AS rg_ie,
    empresa.razao_social         AS empresa_razao_social,
    empresa.nome_fantasia        AS empresa_nome_fantasia,
    empresa.cnpj                 AS empresa_cnpj,
    empresa.inscricao_estadual   AS empresa_inscricao_estadual,
    empresa.imagem_logotipo      AS empresa_imagem_logotipo,
    empresa_endereco.logradouro  AS empresa_endereco_logradouro,
    empresa_endereco.numero      AS empresa_endereco_numero,
    empresa_endereco.complemento AS empresa_endereco_complemento,
    empresa_endereco.bairro      AS empresa_endereco_bairro,
    empresa_endereco.cidade      AS empresa_endereco_cidade,
    empresa_endereco.cep         AS empresa_endereco_cep,
    empresa_endereco.fone        AS empresa_endereco_fone,
    empresa_endereco.uf          AS empresa_endereco_uf,
    empresa.email                AS empresa_email
  FROM pessoa p
    INNER JOIN pessoa_fisica pf ON pf.id_pessoa = p.id
    INNER JOIN cliente c ON c.id_pessoa = p.id
    INNER JOIN pessoa_endereco e ON e.id_pessoa = p.id
    INNER JOIN empresa_pessoa ep ON ep.id_pessoa = p.id
    INNER JOIN empresa empresa ON ep.id_empresa = empresa.id
    INNER JOIN empresa_endereco empresa_endereco ON empresa_endereco.id_empresa = empresa.id
  WHERE empresa_endereco.principal = 'S' AND p.cliente = 'S' AND e.principal = 'S'
  UNION
  SELECT
    c.id,
    c.id_operacao_fiscal,
    c.id_pessoa,
    c.id_atividade_for_cli,
    c.id_situacao_for_cli,
    empresa.id                   AS id_empresa,
    c.desde,
    c.data_cadastro,
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
    pj.cnpj                      AS cpf_cnpj,
    pj.inscricao_estadual,
    empresa.razao_social         AS empresa_razao_social,
    empresa.nome_fantasia        AS empresa_nome_fantasia,
    empresa.cnpj                 AS empresa_cnpj,
    empresa.inscricao_estadual   AS empresa_inscricao_estadual,
    empresa.imagem_logotipo      AS empresa_imagem_logotipo,
    empresa_endereco.logradouro  AS empresa_endereco_logradouro,
    empresa_endereco.numero      AS empresa_endereco_numero,
    empresa_endereco.complemento AS empresa_endereco_complemento,
    empresa_endereco.bairro      AS empresa_endereco_bairro,
    empresa_endereco.cidade      AS empresa_endereco_cidade,
    empresa_endereco.cep         AS empresa_endereco_cep,
    empresa_endereco.fone        AS empresa_endereco_fone,
    empresa_endereco.uf          AS empresa_endereco_uf,
    empresa.email                AS empresa_email
  FROM pessoa p
    INNER JOIN pessoa_juridica pj ON pj.id_pessoa = p.id
    INNER JOIN cliente c ON c.id_pessoa = p.id
    INNER JOIN pessoa_endereco e ON e.id_pessoa = p.id
    INNER JOIN empresa_pessoa ep ON ep.id_pessoa = p.id
    INNER JOIN empresa empresa ON ep.id_empresa = empresa.id
    INNER JOIN empresa_endereco empresa_endereco ON empresa_endereco.id_empresa = empresa.id
  WHERE empresa_endereco.principal = 'S' AND p.cliente = 'S' AND e.principal = 'S';

--View FInanceiro

CREATE OR REPLACE VIEW view_movimento_caixa AS
  SELECT
    concat(m.id, s.id, 1) :: INTEGER AS id,
    m.id                             AS idmovimento,
    s.id                             AS idorigem,
    s.data_sangria                   AS data_hora,
    'Saída - Sangria'                AS descricao,
    s.valor,
    '01'                             AS codigo_forma_pagamento,
    'Dinheiro'                       AS forma_pagamento,
    'S'                              AS tipo,
    'Sangria'                        AS origem,
    s.observacao
  FROM pdv_movimento m
    INNER JOIN pdv_sangria s ON s.id_pdv_movimento = m.id
  UNION
  SELECT
    concat(m.id, s.id, 2) :: INTEGER AS id,
    m.id                             AS idmovimento,
    s.id                             AS idorigem,
    s.data_suprimento                AS data_hora,
    'Entrada - Acrécimo' :: TEXT     AS descricao,
    s.valor,
    '01' :: TEXT                     AS codigo_forma_pagamento,
    'Dinheiro' :: TEXT               AS forma_pagamento,
    'E' :: TEXT                      AS tipo,
    'Suprimentos' :: TEXT            AS origem,
    s.observacao
  FROM pdv_movimento m
    JOIN pdv_suprimento s ON s.id_pdv_movimento = m.id
  UNION
  SELECT
    concat(m.id, v.id, t.id, 3) :: INTEGER AS id,
    m.id                                   AS idmovimento,
    v.id                                   AS idorigem,
    v.data_hora_venda                      AS data_hora,
    'Venda nº ' || v.id                    AS descricao,
    f.valor,
    t.codigo                               AS codigo_forma_pagamento,
    t.descricao                            AS forma_pagamento,
    'E'                                    AS tipo,
    'PDV'                                  AS origem,
    ''                                     AS observacao
  FROM pdv_movimento m
    INNER JOIN pdv_venda_cabecalho v ON v.id_pdv_movimento = m.id
    INNER JOIN pdv_forma_pagamento f ON f.id_pdv_venda_cabecalho = v.id
    INNER JOIN pdv_tipo_pagamento t ON f.id_pdv_tipo_pagamento = t.id
  ORDER BY 4;


CREATE OR REPLACE VIEW view_fin_lancamento_pagar AS
  SELECT
    concat(lp.id, pp.id) :: INTEGER AS id,
    lp.id                           AS id_lancamento_pagar,
    lp.id_empresa,
    pp.id                           AS id_parcela_pagar,
    cc.id                           AS id_conta_caixa,
    s.id                            AS id_status_parcela,
    f.id                            AS id_fornecedor,
    p.id                            AS id_pessoa,
    p.nome                          AS nome_fornecedor,
    pf.cpf                          AS cpf_cnpj,
    lp.quantidade_parcela,
    lp.valor_a_pagar                AS valor_lancamento,
    lp.data_lancamento,
    lp.numero_documento,
    pp.numero_parcela,
    pp.data_vencimento,
    pp.valor                        AS valor_parcela,
    pp.taxa_juro,
    pp.valor_juro,
    pp.taxa_multa,
    pp.valor_multa,
    pp.taxa_desconto,
    pp.valor_desconto,
    doc.sigla_documento,
    s.situacao                      AS situacao_parcela,
    s.descricao                     AS descricao_situacao_parcela,
    cc.nome                         AS nome_conta_caixa,
    f.sofre_retencao                AS fornecedor_sofre_retencao
  FROM fin_lancamento_pagar lp
    INNER JOIN fin_parcela_pagar pp ON pp.id_fin_lancamento_pagar = lp.id
    INNER JOIN fin_status_parcela s ON pp.id_fin_status_parcela = s.id
    INNER JOIN fin_documento_origem doc ON lp.id_fin_documento_origem = doc.id
    INNER JOIN fornecedor f ON lp.id_fornecedor = f.id
    INNER JOIN pessoa p ON f.id_pessoa = p.id
    INNER JOIN pessoa_fisica pf ON pf.id_pessoa = p.id
    INNER JOIN conta_caixa cc ON pp.id_conta_caixa = cc.id
  UNION

  SELECT
    concat(lp.id, pp.id) :: INTEGER AS id,
    lp.id                           AS id_lancamento_pagar,
    lp.id_empresa,
    pp.id                           AS id_parcela_pagar,
    cc.id                           AS id_conta_caixa,
    s.id                            AS id_status_parcela,
    f.id                            AS id_fornecedor,
    p.id                            AS id_pessoa,
    p.nome                          AS nome_fornecedor,
    pj.cnpj                         AS cpf_cnpj,
    lp.quantidade_parcela,
    lp.valor_a_pagar                AS valor_lancamento,
    lp.data_lancamento,
    lp.numero_documento,
    pp.numero_parcela,
    pp.data_vencimento,
    pp.valor                        AS valor_parcela,
    pp.taxa_juro,
    pp.valor_juro,
    pp.taxa_multa,
    pp.valor_multa,
    pp.taxa_desconto,
    pp.valor_desconto,
    doc.sigla_documento,
    s.situacao                      AS situacao_parcela,
    s.descricao                     AS descricao_situacao_parcela,
    cc.nome                         AS nome_conta_caixa,
    f.sofre_retencao                AS fornecedor_sofre_retencao
  FROM fin_lancamento_pagar lp
    INNER JOIN fin_parcela_pagar pp ON pp.id_fin_lancamento_pagar = lp.id
    INNER JOIN fin_status_parcela s ON pp.id_fin_status_parcela = s.id
    INNER JOIN fin_documento_origem doc ON lp.id_fin_documento_origem = doc.id
    INNER JOIN fornecedor f ON lp.id_fornecedor = f.id
    INNER JOIN pessoa p ON f.id_pessoa = p.id
    INNER JOIN pessoa_juridica pj ON pj.id_pessoa = p.id
    INNER JOIN conta_caixa cc ON pp.id_conta_caixa = cc.id;


CREATE OR REPLACE VIEW view_fin_resumo_tesouraria AS
  SELECT
    concat(lp.id, pp.id, ppr.id) :: INTEGER AS id,
    cc.id                                   AS id_conta_caixa,
    cc.nome                                 AS nome_conta_caixa,
    p.nome                                  AS nome_pessoa,
    lp.data_lancamento,
    pp.data_pagamento                       AS data_pago_recebido,
    pp.historico,
    pp.valor_pago                           AS valor,
    doc.descricao                           AS descricao_documento_origem,
    'S'                                     AS operacao
  FROM fin_parcela_pagamento pp
    JOIN conta_caixa cc ON pp.id_conta_caixa = cc.id
    JOIN fin_parcela_pagar ppr ON pp.id_fin_parcela_pagar = ppr.id
    JOIN fin_lancamento_pagar lp ON ppr.id_fin_lancamento_pagar = lp.id
    JOIN fornecedor f ON lp.id_fornecedor = f.id
    JOIN pessoa p ON f.id_pessoa = p.id
    JOIN fin_documento_origem doc ON lp.id_fin_documento_origem = doc.id
  UNION
  SELECT
    concat(lr.id, pr.id, ppr.id) :: INTEGER AS id,
    cc.id                                   AS id_conta_caixa,
    cc.nome                                 AS nome_conta_caixa,
    p.nome                                  AS nome_pessoa,
    lr.data_lancamento,
    pr.data_recebimento                     AS data_pago_recebido,
    pr.historico,
    pr.valor_recebido                       AS valor,
    doc.descricao                           AS descricao_documento_origem,
    'E'                                     AS operacao
  FROM fin_parcela_recebimento pr
    JOIN conta_caixa cc ON pr.id_conta_caixa = cc.id
    JOIN fin_parcela_receber ppr ON pr.id_fin_parcela_receber = ppr.id
    JOIN fin_lancamento_receber lr ON ppr.id_fin_lancamento_receber = lr.id
    JOIN cliente c ON lr.id_cliente = c.id
    JOIN pessoa p ON c.id_pessoa = p.id
    JOIN fin_documento_origem doc ON lr.id_fin_documento_origem = doc.id;


CREATE OR REPLACE VIEW view_fin_lancamento_receber AS
  SELECT
    lr.id                                                          AS id,
    c.id                                                           AS id_cliente,
    lr.id_empresa,
    p.nome,
    pf.cpf                                                         AS cpf_cnpj,
    lr.data_lancamento,
    lr.valor_a_receber                                             AS valor_lancamento,
    lr.quantidade_parcela,
    lr.numero_documento,
    pr.numero_parcela,
    pr.data_vencimento,
    pr.valor                                                       AS valor_parcela,
    pr.taxa_juro,
    pr.valor_juro,
    pr.taxa_multa,
    pr.valor_multa,
    pr.taxa_desconto,
    pr.valor_desconto,
    (SELECT COALESCE(sum(fin_parcela_recebimento.valor_recebido), 0 :: NUMERIC)
     FROM fin_parcela_recebimento
     WHERE fin_parcela_recebimento.id_fin_parcela_receber = pr.id) AS valor_recebido,
    (SELECT COALESCE(sum(fin_parcela_recebimento.valor_juro), 0 :: NUMERIC)
     FROM fin_parcela_recebimento
     WHERE fin_parcela_recebimento.id_fin_parcela_receber = pr.id) AS juros_recebido,
    (SELECT COALESCE(sum(fin_parcela_recebimento.valor_multa), 0 :: NUMERIC)
     FROM fin_parcela_recebimento
     WHERE fin_parcela_recebimento.id_fin_parcela_receber = pr.id) AS multa_recebido,
    s.id                                                           AS id_status_parcela,
    s.situacao                                                     AS situacao_parcela,
    s.descricao                                                    AS descricao_situacao_parcela,
    cc.id                                                          AS id_conta_caixa,
    cc.nome                                                        AS nome_conta_caixa,
    cc.limite_cobranca_juro,
    pr.id                                                          AS id_parcela_receber,
    doc.sigla_documento
  FROM fin_lancamento_receber lr
    INNER JOIN fin_parcela_receber pr ON (pr.id_fin_lancamento_receber = lr.id)
    INNER JOIN fin_status_parcela s ON (pr.id_fin_status_parcela = s.id)
    INNER JOIN conta_caixa cc ON (pr.id_conta_caixa = cc.id)
    INNER JOIN fin_documento_origem doc ON (lr.id_fin_documento_origem = doc.id)
    INNER JOIN cliente c ON (lr.id_cliente = c.id)
    INNER JOIN pessoa p ON (c.id_pessoa = p.id)
    INNER JOIN pessoa_fisica pf ON (pf.id_pessoa = p.id)

  UNION
  SELECT
    lr.id                                                          AS id,
    c.id                                                           AS id_cliente,
    lr.id_empresa,
    p.nome,
    pj.cnpj                                                        AS cpf_cnpj,
    lr.data_lancamento,
    lr.valor_a_receber                                             AS valor_lancamento,
    lr.quantidade_parcela,
    lr.numero_documento,
    pr.numero_parcela,
    pr.data_vencimento,
    pr.valor                                                       AS valor_parcela,
    pr.taxa_juro,
    pr.valor_juro,
    pr.taxa_multa,
    pr.valor_multa,
    pr.taxa_desconto,
    pr.valor_desconto,
    (SELECT COALESCE(sum(fin_parcela_recebimento.valor_recebido), 0 :: NUMERIC)
     FROM fin_parcela_recebimento
     WHERE fin_parcela_recebimento.id_fin_parcela_receber = pr.id) AS valor_recebido,
    (SELECT COALESCE(sum(fin_parcela_recebimento.valor_juro), 0 :: NUMERIC)
     FROM fin_parcela_recebimento
     WHERE fin_parcela_recebimento.id_fin_parcela_receber = pr.id) AS juros_recebido,
    (SELECT COALESCE(sum(fin_parcela_recebimento.valor_multa), 0 :: NUMERIC)
     FROM fin_parcela_recebimento
     WHERE fin_parcela_recebimento.id_fin_parcela_receber = pr.id) AS multa_recebido,
    s.id                                                           AS id_status_parcela,
    s.situacao                                                     AS situacao_parcela,
    s.descricao                                                    AS descricao_situacao_parcela,
    cc.id                                                          AS id_conta_caixa,
    cc.nome                                                        AS nome_conta_caixa,
    cc.limite_cobranca_juro,
    pr.id                                                          AS id_parcela_receber,
    doc.sigla_documento

  FROM fin_lancamento_receber lr
    INNER JOIN fin_parcela_receber pr ON (pr.id_fin_lancamento_receber = lr.id)
    INNER JOIN fin_parcela_recebimento prb ON (prb.id_fin_parcela_receber = pr.id)
    INNER JOIN fin_status_parcela s ON (pr.id_fin_status_parcela = s.id)
    INNER JOIN conta_caixa cc ON (pr.id_conta_caixa = cc.id)
    INNER JOIN fin_documento_origem doc ON (lr.id_fin_documento_origem = doc.id)
    INNER JOIN cliente c ON (lr.id_cliente = c.id)
    INNER JOIN pessoa p ON (c.id_pessoa = p.id)
    INNER JOIN pessoa_juridica pj ON (pj.id_pessoa = p.id);


CREATE OR REPLACE VIEW view_fin_fluxo_caixa AS
  SELECT
    concat(lp.id, pp.id) :: INTEGER AS id,
    cc.id                           AS id_conta_caixa,
    cc.nome                         AS nome_conta_caixa,
    p.nome                          AS nome_pessoa,
    lp.data_lancamento,
    pp.data_vencimento,
    pp.valor,
    sp.situacao                     AS codigo_situacao,
    sp.descricao                    AS descricao_situacao,
    n.descricao                     AS descricao_natureza,
    'S'                             AS operacao
  FROM fin_parcela_pagar pp
    JOIN conta_caixa cc ON pp.id_conta_caixa = cc.id
    JOIN fin_lancamento_pagar lp ON pp.id_fin_lancamento_pagar = lp.id
    JOIN fornecedor f ON lp.id_fornecedor = f.id
    JOIN pessoa p ON f.id_pessoa = p.id
    JOIN fin_status_parcela sp ON pp.id_fin_status_parcela = sp.id
    JOIN fin_lcto_pagar_nt_financeira lpn ON lpn.id_fin_lancamento_pagar = lp.id
    JOIN natureza_financeira n ON lpn.id_natureza_financeira = n.id
  WHERE sp.situacao <> '02'
  UNION
  SELECT
    concat(lr.id, pr.id) :: INTEGER                                      AS id,
    cc.id                                                                AS id_conta_caixa,
    cc.nome                                                              AS nome_conta_caixa,
    p.nome                                                               AS nome_pessoa,
    lr.data_lancamento,
    pr.data_vencimento,
    (pr.valor - COALESCE((SELECT SUM(valor_recebido)
                          FROM fin_parcela_recebimento rec
                          WHERE rec.id_fin_parcela_receber = pr.id), 0)) AS VALOR,
    sp.situacao                                                          AS codigo_situacao,
    sp.descricao                                                         AS descricao_situacao,
    n.descricao                                                          AS descricao_natureza,
    'E' :: TEXT                                                          AS operacao
  FROM fin_parcela_receber pr
    JOIN conta_caixa cc ON pr.id_conta_caixa = cc.id
    JOIN fin_lancamento_receber lr ON pr.id_fin_lancamento_receber = lr.id
    JOIN cliente c ON lr.id_cliente = c.id
    JOIN pessoa p ON c.id_pessoa = p.id
    JOIN fin_status_parcela sp ON pr.id_fin_status_parcela = sp.id
    JOIN fin_lcto_receber_nt_financeira ln ON ln.id_fin_lancamento_receber = lr.id
    JOIN natureza_financeira n ON ln.id_natureza_financeira = n.id
  WHERE sp.situacao <> '02';


CREATE OR REPLACE VIEW view_fin_movimento_caixa_banco AS
  SELECT
    concat(cc.id, lp.id, pp.id) :: INTEGER          AS id,
    pp.id                                           AS id_movimento,
    cc.id                                           AS id_conta_caixa,
    cc.nome                                         AS nome_conta_caixa,
    p.nome                                          AS nome_pessoa,
    lp.data_lancamento,
    pp.data_pagamento                               AS data_pago_recebido,
    COALESCE(pp.historico, '' :: CHARACTER VARYING) AS historico,
    pp.valor_pago                                   AS valor,
    tp.descricao                                    AS tipo,
    doc.descricao                                   AS descricao_documento_origem,
    'S'                                             AS operacao
  FROM fin_parcela_pagamento pp
    JOIN fin_tipo_pagamento tp ON tp.id = pp.id_fin_tipo_pagamento
    JOIN conta_caixa cc ON pp.id_conta_caixa = cc.id
    JOIN fin_parcela_pagar ppr ON pp.id_fin_parcela_pagar = ppr.id
    JOIN fin_lancamento_pagar lp ON ppr.id_fin_lancamento_pagar = lp.id
    JOIN fornecedor f ON lp.id_fornecedor = f.id
    JOIN pessoa p ON f.id_pessoa = p.id
    JOIN fin_documento_origem doc ON lp.id_fin_documento_origem = doc.id
  UNION
  SELECT
    concat(cc.id, lr.id, pr.id) :: INTEGER AS id,
    pr.id                                  AS id_movimento,
    cc.id                                  AS id_conta_caixa,
    cc.nome                                AS nome_conta_caixa,
    p.nome                                 AS nome_pessoa,
    lr.data_lancamento,
    pr.data_recebimento                    AS data_pago_recebido,
    COALESCE(pr.historico, '')             AS historico,
    pr.valor_recebido                      AS valor,
    tr.descricao                           AS tipo,
    doc.descricao                          AS descricao_documento_origem,
    'E'                                    AS operacao
  FROM fin_parcela_recebimento pr
    JOIN fin_tipo_recebimento tr ON tr.id = pr.id_fin_tipo_recebimento
    JOIN conta_caixa cc ON pr.id_conta_caixa = cc.id
    JOIN fin_parcela_receber ppr ON pr.id_fin_parcela_receber = ppr.id
    JOIN fin_lancamento_receber lr ON ppr.id_fin_lancamento_receber = lr.id
    JOIN cliente c ON lr.id_cliente = c.id
    JOIN pessoa p ON c.id_pessoa = p.id
    JOIN fin_documento_origem doc ON lr.id_fin_documento_origem = doc.id
  ORDER BY 11, 9;


CREATE OR REPLACE VIEW view_fin_cheque_nao_compensado AS
  SELECT
    0        AS id,
    cc.id    AS id_conta_caixa,
    cc.nome  AS nome_conta_caixa,
    t.talao,
    t.numero AS numero_talao,
    c.numero AS numero_cheque,
    c.status_cheque,
    c.data_status,
    ce.valor
  FROM cheque c
    JOIN talonario_cheque t ON c.id_talonario_cheque = t.id
    JOIN conta_caixa cc ON t.id_conta_caixa = cc.id
    JOIN fin_cheque_emitido ce ON ce.id_cheque = c.id
  WHERE c.status_cheque <> 'C';


CREATE OR REPLACE VIEW view_resumo_contas_mensal AS
  SELECT
    (SELECT COALESCE(sum(valor_pago), 0 :: NUMERIC)
     FROM fin_parcela_pagamento
     WHERE data_pagamento BETWEEN (date_trunc('month', CURRENT_DATE)) AND (
       date_trunc('month', CURRENT_DATE) + INTERVAL '1 month' - INTERVAL '1 day')) AS valor_quitado,
    COALESCE(sum(valor), 0)                                                        AS total_quita,
    COALESCE(SUM(CASE WHEN data_vencimento > CURRENT_DATE AND data_vencimento <= CURRENT_DATE + '7 day' :: INTERVAL
      THEN valor
                 ELSE 0 END), 0)                                                   AS total_quita_7_dias,
    'P'                                                                            AS tipo
  FROM fin_parcela_pagar pp
    INNER JOIN fin_status_parcela s ON pp.id_fin_status_parcela = s.id
  WHERE s.situacao <> '02' AND data_vencimento BETWEEN (date_trunc('month', CURRENT_DATE)) AND (
    date_trunc('month', CURRENT_DATE) + INTERVAL '1 month' - INTERVAL '1 day')

  UNION

  SELECT

    (SELECT COALESCE(sum(valor_recebido), 0 :: NUMERIC)
     FROM fin_parcela_recebimento
     WHERE data_recebimento BETWEEN (date_trunc('month', CURRENT_DATE)) AND (
       date_trunc('month', CURRENT_DATE) + INTERVAL '1 month' - INTERVAL '1 day')) AS valor_quitado,
    COALESCE(sum(valor), 0)                                                        AS total_quita,
    COALESCE(SUM(CASE WHEN data_vencimento > CURRENT_DATE AND data_vencimento <= CURRENT_DATE + '7 day' :: INTERVAL
      THEN valor
                 ELSE 0 END), 0)                                                   AS total_quita_7_dias,
    'R'                                                                            AS tipo
  FROM fin_parcela_receber pr
    INNER JOIN fin_status_parcela s ON pr.id_fin_status_parcela = s.id
  WHERE s.situacao <> '02' AND data_vencimento BETWEEN (date_trunc('month', CURRENT_DATE)) AND (
    date_trunc('month', CURRENT_DATE) + INTERVAL '1 month' - INTERVAL '1 day');

-- Views referente a tributação

CREATE OR REPLACE VIEW view_tributacao_cofins AS
  SELECT
    cofins.id,
    cofins.id_tribut_operacao_fiscal,
    cofins.cst_cofins,
    cofins.efd_tabela_435,
    cofins.modalidade_base_calculo,
    cofins.porcento_base_calculo,
    cofins.aliquota_porcento,
    cofins.aliquota_unidade,
    cofins.valor_preco_maximo,
    cofins.valor_pauta_fiscal
  FROM tribut_cofins_cod_apuracao cofins;


CREATE OR REPLACE VIEW view_tributacao_pis AS
  SELECT
    pis.id,
    pis.id_tribut_operacao_fiscal,
    pis.cst_pis,
    pis.efd_tabela_435,
    pis.modalidade_base_calculo,
    pis.porcento_base_calculo,
    pis.aliquota_porcento,
    pis.aliquota_unidade,
    pis.valor_preco_maximo,
    pis.valor_pauta_fiscal
  FROM tribut_pis_cod_apuracao pis;


CREATE OR REPLACE VIEW view_tributacao_icms AS
  SELECT
    icms.id,
    icms.id_tribut_grupo_tributario,
    icms.id_tribut_operacao_fiscal,
    grupo.origem_mercadoria,
    icms.uf_destino,
    icms.cfop,
    icms.csosn_b,
    icms.cst_b,
    icms.modalidade_bc,
    icms.aliquota,
    icms.valor_pauta,
    icms.valor_preco_maximo,
    icms.mva,
    icms.porcento_bc,
    icms.modalidade_bc_st,
    icms.aliquota_interna_st,
    icms.aliquota_interestadual_st,
    icms.porcento_bc_st,
    icms.aliquota_icms_st,
    icms.valor_pauta_st,
    icms.valor_preco_maximo_st
  FROM tribut_icms_uf icms
    INNER JOIN tribut_grupo_tributario grupo ON grupo.id = icms.id_tribut_grupo_tributario;


CREATE OR REPLACE VIEW view_tributacao_icms_custom AS
  SELECT
    cab.id,
    cab.descricao,
    cab.origem_mercadoria,
    det.uf_destino,
    det.cfop,
    det.csosn_b,
    det.cst_b,
    det.modalidade_bc,
    det.aliquota,
    det.valor_pauta,
    det.valor_preco_maximo,
    det.mva,
    det.porcento_bc,
    det.modalidade_bc_st,
    det.aliquota_interna_st,
    det.aliquota_interestadual_st,
    det.porcento_bc_st,
    det.aliquota_icms_st,
    det.valor_pauta_st,
    det.valor_preco_maximo_st
  FROM tribut_icms_custom_cab cab
    JOIN tribut_icms_custom_det det ON det.id_tribut_icms_custom_cab = cab.id;

CREATE OR REPLACE VIEW view_tributacao_ipi AS
  SELECT
    ipi.id,
    ipi.id_tribut_operacao_fiscal,
    ipi.cst_ipi,
    ipi.modalidade_base_calculo,
    ipi.porcento_base_calculo,
    ipi.aliquota_porcento,
    ipi.aliquota_unidade,
    ipi.valor_preco_maximo,
    ipi.valor_pauta_fiscal
  FROM tribut_ipi_dipi ipi;

-- View NFCe

CREATE OR REPLACE VIEW view_nfce_cliente AS
  SELECT
    c.id,
    c.id_operacao_fiscal,
    p.nome,
    p.email,
    pf.cpf,
    pf.rg,
    pf.orgao_rg,
    pf.data_emissao_rg,
    pf.sexo,
    c.data_cadastro,
    c.id_pessoa,
    c.id_atividade_for_cli,
    c.id_situacao_for_cli,
    c.desde,
    e.logradouro,
    e.numero,
    e.complemento,
    e.bairro,
    e.cidade,
    e.cep,
    e.municipio_ibge,
    e.uf,
    e.fone
  FROM pessoa p
    JOIN pessoa_fisica pf ON pf.id_pessoa = p.id
    JOIN cliente c ON c.id_pessoa = p.id
    JOIN pessoa_endereco e ON e.id_pessoa = p.id
  WHERE p.cliente = 'S' AND e.principal = 'S';

-- View Vendas

CREATE OR REPLACE VIEW view_rank_produto AS
  SELECT
    p.nome,
    sum(p.quantidade)            AS quantidade,
    round(sum(p.porcentagem), 2) AS porcetagem,
    view_dados_empresa.empresa_razao_social,
    view_dados_empresa.empresa_nome_fantasia,
    view_dados_empresa.empresa_cnpj,
    view_dados_empresa.empresa_inscricao_estadual,
    view_dados_empresa.empresa_imagem_logotipo,
    view_dados_empresa.empresa_endereco_logradouro,
    view_dados_empresa.empresa_endereco_numero,
    view_dados_empresa.empresa_endereco_complemento,
    view_dados_empresa.empresa_endereco_bairro,
    view_dados_empresa.empresa_endereco_cidade,
    view_dados_empresa.empresa_endereco_cep,
    view_dados_empresa.empresa_endereco_fone,
    view_dados_empresa.empresa_endereco_uf,
    view_dados_empresa.empresa_email
  FROM (SELECT
          CASE
          WHEN (sum(i.quantidade) * 100.0 / sum(sum(i.quantidade))
          OVER ()) <= 1 :: NUMERIC
            THEN 'OUTROS' :: CHARACTER VARYING
          ELSE p_1.nome
          END               AS nome,
          sum(i.quantidade) AS quantidade,
          sum(i.quantidade) * 100.0 / sum(sum(i.quantidade))
          OVER ()           AS porcentagem
        FROM produto p_1
          JOIN venda_detalhe i ON i.id_produto = p_1.id
        GROUP BY p_1.nome) p,
    view_dados_empresa
  GROUP BY p.nome, view_dados_empresa.empresa_razao_social, view_dados_empresa.empresa_nome_fantasia,
    view_dados_empresa.empresa_cnpj, view_dados_empresa.empresa_inscricao_estadual,
    view_dados_empresa.empresa_imagem_logotipo, view_dados_empresa.empresa_endereco_logradouro,
    view_dados_empresa.empresa_endereco_numero, view_dados_empresa.empresa_endereco_complemento,
    view_dados_empresa.empresa_endereco_bairro, view_dados_empresa.empresa_endereco_cidade,
    view_dados_empresa.empresa_endereco_cep, view_dados_empresa.empresa_endereco_fone,
    view_dados_empresa.empresa_endereco_uf, view_dados_empresa.empresa_email
  ORDER BY 2 DESC;

-- View Estoque

CREATE OR REPLACE VIEW view_produto_empresa AS
  SELECT
    p.id,
    e.id                  AS idempresa,
    sg.id                 AS idsubgrupo,
    g.id                  AS idgrupo,
    m.id                  AS idmarca,
    un.id                 AS idunidade,
    p.gtin,
    p.nome,
    p.ncm,
    p.cest,
    p.custo_unitario,
    p.valor_venda,
    p.servico,
    p.tipo,
    p.imagem,
    ep.quantidade_estoque AS quantidade,
    ep.estoque_verificado,
    p.inativo,
    p.excluido,
    sg.nome               AS subgrupo,
    g.nome                AS grupo,
    m.nome                AS marca,
    un.sigla
  FROM produto p
    JOIN empresa_produto ep ON ep.id_produto = p.id
    JOIN empresa e ON ep.id_empresa = e.id
    JOIN produto_subgrupo sg ON sg.id = p.id_subgrupo
    JOIN produto_grupo g ON g.id = sg.id_grupo
    JOIN unidade_produto un ON un.id = p.id_unidade_produto
    LEFT JOIN produto_marca m ON m.id = p.id_marca_produto
  ORDER BY p.nome;

--View para o SPED

CREATE OR REPLACE VIEW view_sped_nfe_detalhe AS
  SELECT
    nfed.id,
    nfed.id_produto,
    nfed.id_nfe_cabecalho,
    nfed.numero_item,
    nfed.codigo_produto,
    nfed.gtin,
    nfed.nome_produto,
    p.servico,
    nfed.ncm,
    nfed.nve,
    nfed.ex_tipi,
    nfed.cfop,
    nfed.unidade_comercial,
    nfed.quantidade_comercial,
    nfed.valor_unitario_comercial,
    nfed.valor_bruto_produto,
    nfed.gtin_unidade_tributavel,
    nfed.unidade_tributavel,
    nfed.quantidade_tributavel,
    nfed.valor_unitario_tributavel,
    nfed.valor_frete,
    nfed.valor_seguro,
    nfed.valor_desconto,
    nfed.valor_outras_despesas,
    nfed.entra_total,
    nfed.valor_subtotal,
    nfed.valor_total,
    nfed.numero_pedido_compra,
    nfed.item_pedido_compra,
    nfed.informacoes_adicionais,
    nfed.numero_fci,
    nfed.numero_recopi,
    nfed.valor_total_tributos,
    nfed.percentual_devolvido,
    nfed.valor_ipi_devolvido,
    nfed.cest,
    nfed.indicador_escala_relevante,
    nfed.cnpj_fabricante,
    nfed.codigo_beneficio_fiscal,
    nfec.id_tribut_operacao_fiscal,
    p.id_unidade_produto,
    cofins.cst_cofins,
    cofins.quantidade_vendida AS quantidade_vendida_cofins,
    cofins.base_calculo_cofins,
    cofins.aliquota_cofins_percentual,
    cofins.aliquota_cofins_reais,
    cofins.valor_cofins,
    icms.origem_mercadoria,
    icms.cst_icms,
    icms.csosn,
    icms.modalidade_bc_icms,
    icms.taxa_reducao_bc_icms,
    icms.base_calculo_icms,
    icms.aliquota_icms,
    icms.valor_icms,
    icms.motivo_desoneracao_icms,
    icms.modalidade_bc_icms_st,
    icms.percentual_mva_icms_st,
    icms.percentual_reducao_bc_icms_st,
    icms.valor_base_calculo_icms_st,
    icms.aliquota_icms_st,
    icms.valor_icms_st,
    icms.valor_bc_icms_st_retido,
    icms.valor_icms_st_retido,
    icms.valor_bc_icms_st_destino,
    icms.valor_icms_st_destino,
    icms.aliquota_credito_icms_sn,
    icms.valor_credito_icms_sn,
    icms.percentual_bc_operacao_propria,
    icms.uf_st,
    ii.valor_bc_ii,
    ii.valor_despesas_aduaneiras,
    ii.valor_imposto_importacao,
    ii.valor_iof,
    ipi.enquadramento_ipi,
    ipi.cnpj_produtor,
    ipi.codigo_selo_ipi,
    ipi.quantidade_selo_ipi,
    ipi.enquadramento_legal_ipi,
    ipi.cst_ipi,
    ipi.valor_base_calculo_ipi,
    ipi.aliquota_ipi,
    ipi.quantidade_unidade_tributavel,
    ipi.valor_unidade_tributavel,
    ipi.valor_ipi,
    issqn.base_calculo_issqn,
    issqn.aliquota_issqn,
    issqn.valor_issqn,
    issqn.municipio_issqn,
    issqn.item_lista_servicos,
    pis.cst_pis,
    pis.quantidade_vendida    AS quantidade_vendida_pis,
    pis.valor_base_calculo_pis,
    pis.aliquota_pis_percentual,
    pis.aliquota_pis_reais,
    pis.valor_pis
  FROM nfe_detalhe nfed
    LEFT JOIN nfe_detalhe_imposto_cofins cofins ON cofins.id_nfe_detalhe = nfed.id
    LEFT JOIN nfe_detalhe_imposto_icms icms ON icms.id_nfe_detalhe = nfed.id
    LEFT JOIN nfe_detalhe_imposto_ii ii ON ii.id_nfe_detalhe = nfed.id
    LEFT JOIN nfe_detalhe_imposto_ipi ipi ON ipi.id_nfe_detalhe = nfed.id
    LEFT JOIN nfe_detalhe_imposto_issqn issqn ON issqn.id_nfe_detalhe = nfed.id
    LEFT JOIN nfe_detalhe_imposto_pis pis ON pis.id_nfe_detalhe = nfed.id
    LEFT JOIN produto p ON nfed.id_produto = p.id
    LEFT JOIN nfe_cabecalho nfec ON nfed.id_nfe_cabecalho = nfec.id;


CREATE OR REPLACE VIEW view_sped_c190 AS
  SELECT
    nfec.id,
    COALESCE(nfed.cst_icms, nfed.csosn)               AS cst_icms,
    nfed.cfop,
    COALESCE(nfed.aliquota_icms, 0)                   AS aliquota_icms,
    nfec.data_hora_emissao                            AS data_emissao,
    COALESCE(sum(nfed.valor_total), 0)                AS soma_valor_operacao,
    COALESCE(sum(nfed.base_calculo_icms), 0)          AS soma_base_calculo_icms,
    COALESCE(sum(nfed.valor_icms), 0)                 AS soma_valor_icms,
    COALESCE(sum(nfed.valor_base_calculo_icms_st), 0) AS soma_base_calculo_icms_st,
    COALESCE(sum(nfed.valor_icms_st), 0)              AS soma_valor_icms_st,
    COALESCE(sum(nfed.valor_outras_despesas), 0)      AS soma_vl_red_bc,
    COALESCE(sum(nfed.valor_ipi), 0)                  AS soma_valor_ipi
  FROM view_sped_nfe_detalhe nfed
    JOIN nfe_cabecalho nfec ON nfed.id_nfe_cabecalho = nfec.id
  WHERE nfed.servico <> 'S'
  GROUP BY nfec.id, nfed.cst_icms, nfed.csosn, nfed.cfop, nfed.aliquota_icms, nfec.data_hora_emissao;