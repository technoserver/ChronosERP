CREATE OR REPLACE VIEW view_pessoa_cliente AS
  SELECT c.id,
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
    pf.rg AS rg_ie
  FROM pessoa p
    JOIN pessoa_fisica pf ON pf.id_pessoa = p.id
    JOIN cliente c ON c.id_pessoa = p.id
    JOIN pessoa_endereco e ON e.id_pessoa = p.id
  WHERE p.cliente = 'S' AND e.principal = 'S'
  UNION
  SELECT c.id,
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
    pj.cnpj AS cpf_cnpj,
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
    INNER JOIN fin_status_parcela s ON (pr.id_fin_status_parcela = s.id)
    INNER JOIN conta_caixa cc ON (pr.id_conta_caixa = cc.id)
    INNER JOIN fin_documento_origem doc ON (lr.id_fin_documento_origem = doc.id)
    INNER JOIN cliente c ON (lr.id_cliente = c.id)
    INNER JOIN pessoa p ON (c.id_pessoa = p.id)
    INNER JOIN pessoa_juridica pj ON (pj.id_pessoa = p.id);

-- Views referente a tributação

CREATE OR REPLACE VIEW view_tributacao_cofins AS
  SELECT
    configura.id,
    configura.id_tribut_grupo_tributario,
    configura.id_tribut_operacao_fiscal,
    cofins.cst_cofins,
    cofins.efd_tabela_435,
    cofins.modalidade_base_calculo,
    cofins.porcento_base_calculo,
    cofins.aliquota_porcento,
    cofins.aliquota_unidade,
    cofins.valor_preco_maximo,
    cofins.valor_pauta_fiscal
  FROM tribut_configura_of_gt configura
    JOIN tribut_cofins_cod_apuracao cofins ON cofins.id_tribut_configura_of_gt = configura.id;

CREATE OR REPLACE VIEW view_tributacao_pis AS
  SELECT
    configura.id,
    configura.id_tribut_grupo_tributario,
    configura.id_tribut_operacao_fiscal,
    pis.cst_pis,
    pis.efd_tabela_435,
    pis.modalidade_base_calculo,
    pis.porcento_base_calculo,
    pis.aliquota_porcento,
    pis.aliquota_unidade,
    pis.valor_preco_maximo,
    pis.valor_pauta_fiscal
  FROM tribut_configura_of_gt configura
    JOIN tribut_pis_cod_apuracao pis ON pis.id_tribut_configura_of_gt = configura.id;


CREATE OR REPLACE VIEW view_tributacao_icms AS
  SELECT
    configura.id,
    configura.id_tribut_grupo_tributario,
    configura.id_tribut_operacao_fiscal,
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
  FROM tribut_configura_of_gt configura
    JOIN tribut_icms_uf icms ON icms.id_tribut_configura_of_gt = configura.id
    JOIN tribut_grupo_tributario grupo ON configura.id_tribut_grupo_tributario = grupo.id;

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
    configura.id,
    configura.id_tribut_grupo_tributario,
    configura.id_tribut_operacao_fiscal,
    ipi.cst_ipi,
    ipi.modalidade_base_calculo,
    ipi.porcento_base_calculo,
    ipi.aliquota_porcento,
    ipi.aliquota_unidade,
    ipi.valor_preco_maximo,
    ipi.valor_pauta_fiscal
  FROM tribut_configura_of_gt configura
    JOIN tribut_ipi_dipi ipi ON ipi.id_tribut_configura_of_gt = configura.id;

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
    ep.controle,
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

CREATE VIEW VIEW_SPED_NFE_DETALHE AS
  SELECT
    NFED.*,
    NFEC.ID_TRIBUT_OPERACAO_FISCAL,
    P.ID_UNIDADE_PRODUTO,
    COFINS.CST_COFINS,
    COFINS.QUANTIDADE_VENDIDA AS QUANTIDADE_VENDIDA_COFINS,
    COFINS.BASE_CALCULO_COFINS,
    COFINS.ALIQUOTA_COFINS_PERCENTUAL,
    COFINS.ALIQUOTA_COFINS_REAIS,
    COFINS.VALOR_COFINS,
    ICMS.ORIGEM_MERCADORIA,
    ICMS.CST_ICMS,
    ICMS.CSOSN,
    ICMS.MODALIDADE_BC_ICMS,
    ICMS.TAXA_REDUCAO_BC_ICMS,
    ICMS.BASE_CALCULO_ICMS,
    ICMS.ALIQUOTA_ICMS,
    ICMS.VALOR_ICMS,
    ICMS.MOTIVO_DESONERACAO_ICMS,
    ICMS.MODALIDADE_BC_ICMS_ST,
    ICMS.PERCENTUAL_MVA_ICMS_ST,
    ICMS.PERCENTUAL_REDUCAO_BC_ICMS_ST,
    ICMS.VALOR_BASE_CALCULO_ICMS_ST,
    ICMS.ALIQUOTA_ICMS_ST,
    ICMS.VALOR_ICMS_ST,
    ICMS.VALOR_BC_ICMS_ST_RETIDO,
    ICMS.VALOR_ICMS_ST_RETIDO,
    ICMS.VALOR_BC_ICMS_ST_DESTINO,
    ICMS.VALOR_ICMS_ST_DESTINO,
    ICMS.ALIQUOTA_CREDITO_ICMS_SN,
    ICMS.VALOR_CREDITO_ICMS_SN,
    ICMS.PERCENTUAL_BC_OPERACAO_PROPRIA,
    ICMS.UF_ST,
    II.VALOR_BC_II,
    II.VALOR_DESPESAS_ADUANEIRAS,
    II.VALOR_IMPOSTO_IMPORTACAO,
    II.VALOR_IOF,
    IPI.ENQUADRAMENTO_IPI,
    IPI.CNPJ_PRODUTOR,
    IPI.CODIGO_SELO_IPI,
    IPI.QUANTIDADE_SELO_IPI,
    IPI.ENQUADRAMENTO_LEGAL_IPI,
    IPI.CST_IPI,
    IPI.VALOR_BASE_CALCULO_IPI,
    IPI.ALIQUOTA_IPI,
    IPI.QUANTIDADE_UNIDADE_TRIBUTAVEL,
    IPI.VALOR_UNIDADE_TRIBUTAVEL,
    IPI.VALOR_IPI,
    ISSQN.BASE_CALCULO_ISSQN,
    ISSQN.ALIQUOTA_ISSQN,
    ISSQN.VALOR_ISSQN,
    ISSQN.MUNICIPIO_ISSQN,
    ISSQN.ITEM_LISTA_SERVICOS,
    PIS.CST_PIS,
    PIS.QUANTIDADE_VENDIDA    AS QUANTIDADE_VENDIDA_PIS,
    PIS.VALOR_BASE_CALCULO_PIS,
    PIS.ALIQUOTA_PIS_PERCENTUAL,
    PIS.ALIQUOTA_PIS_REAIS,
    PIS.VALOR_PIS
  FROM
    NFE_DETALHE NFED
    LEFT JOIN NFE_DETALHE_IMPOSTO_COFINS COFINS ON (COFINS.ID_NFE_DETALHE = NFED.ID)
    LEFT JOIN NFE_DETALHE_IMPOSTO_ICMS ICMS ON (ICMS.ID_NFE_DETALHE = NFED.ID)
    LEFT JOIN NFE_DETALHE_IMPOSTO_II II ON (II.ID_NFE_DETALHE = NFED.ID)
    LEFT JOIN NFE_DETALHE_IMPOSTO_IPI IPI ON (IPI.ID_NFE_DETALHE = NFED.ID)
    LEFT JOIN NFE_DETALHE_IMPOSTO_ISSQN ISSQN ON (ISSQN.ID_NFE_DETALHE = NFED.ID)
    LEFT JOIN NFE_DETALHE_IMPOSTO_PIS PIS ON (PIS.ID_NFE_DETALHE = NFED.ID)
    LEFT JOIN PRODUTO P ON (NFED.ID_PRODUTO = P.ID)
    LEFT JOIN NFE_CABECALHO NFEC ON (NFED.ID_NFE_CABECALHO = NFEC.ID);


CREATE OR REPLACE VIEW view_sped_c190 AS
  SELECT
    nfec.id,
    nfed.cst_icms,
    nfed.cfop,
    nfed.aliquota_icms,
    nfec.data_hora_emissao,
    sum(nfed.valor_total)                AS soma_valor_operacao,
    sum(nfed.base_calculo_icms)          AS soma_base_calculo_icms,
    sum(nfed.valor_icms)                 AS soma_valor_icms,
    sum(nfed.valor_base_calculo_icms_st) AS soma_base_calculo_icms_st,
    sum(nfed.valor_icms_st)              AS soma_valor_icms_st,
    sum(nfed.valor_outras_despesas)      AS soma_vl_red_bc,
    COALESCE(sum(nfed.valor_ipi), 0)     AS soma_valor_ipi
  FROM view_sped_nfe_detalhe nfed
    JOIN nfe_cabecalho nfec ON nfed.id_nfe_cabecalho = nfec.id
  GROUP BY nfec.id, nfed.cst_icms, nfed.cfop, nfed.aliquota_icms, nfec.data_hora_emissao;


CREATE VIEW VIEW_SPED_C300
AS
  SELECT
    SERIE,
    SUBSERIE,
    DATA_EMISSAO,
    SUM(TOTAL_NF) AS SOMA_TOTAL_NF,
    SUM(PIS)      AS SOMA_PIS,
    SUM(COFINS)   AS SOMA_COFINS
  FROM
    ECF_NOTA_FISCAL_CABECALHO
  GROUP BY
    SERIE, SUBSERIE, DATA_EMISSAO;

CREATE VIEW VIEW_SPED_C321 AS
  SELECT
    NF2D.ID_PRODUTO,
    U.SIGLA               AS DESCRICAO_UNIDADE,
    NF2C.DATA_EMISSAO,
    SUM(NF2D.QUANTIDADE)  AS SOMA_QUANTIDADE,
    SUM(NF2D.VALOR_TOTAL) AS SOMA_ITEM,
    SUM(NF2D.DESCONTO)    AS SOMA_DESCONTO,
    SUM(NF2D.BASE_ICMS)   AS SOMA_BASE_ICMS,
    SUM(NF2D.ICMS)        AS SOMA_ICMS,
    SUM(NF2D.PIS)         AS SOMA_PIS,
    SUM(NF2D.COFINS)      AS SOMA_COFINS
  FROM
    ECF_NOTA_FISCAL_DETALHE NF2D, ECF_NOTA_FISCAL_CABECALHO NF2C, PRODUTO P, UNIDADE_PRODUTO U
  WHERE
    NF2D.ID_NF_CABECALHO = NF2C.ID AND
    NF2D.ID_PRODUTO = P.ID AND
    P.ID_UNIDADE_PRODUTO = U.ID
  GROUP BY ID_PRODUTO, U.SIGLA, NF2C.DATA_EMISSAO;

CREATE VIEW VIEW_SPED_C370 AS
  SELECT
    nd.ID_NF_CABECALHO   AS ID_NF_CABECALHO,
    nc.DATA_EMISSAO      AS DATA_EMISSAO,
    nd.ID_PRODUTO        AS ID_PRODUTO,
    nd.ITEM              AS ITEM,
    p.ID_UNIDADE_PRODUTO AS ID_UNIDADE_PRODUTO,
    nd.QUANTIDADE        AS QUANTIDADE,
    nd.VALOR_TOTAL       AS VALOR_TOTAL,
    nd.CST               AS CST,
    nd.DESCONTO          AS DESCONTO
  FROM ecf_nota_fiscal_detalhe nd
    INNER JOIN ecf_nota_fiscal_cabecalho nc ON (nd.ID_NF_CABECALHO = nc.ID)
    INNER JOIN produto p ON (nd.ID_PRODUTO = p.ID);

CREATE VIEW VIEW_SPED_C390 AS
  SELECT
    NF2D.CST,
    NF2D.CFOP,
    NF2D.TAXA_ICMS,
    NF2C.DATA_EMISSAO,
    SUM(NF2D.VALOR_TOTAL) AS SOMA_ITEM,
    SUM(NF2D.BASE_ICMS)   AS SOMA_BASE_ICMS,
    SUM(NF2D.ICMS)        AS SOMA_ICMS,
    SUM(NF2D.ICMS_OUTRAS) AS SOMA_ICMS_OUTRAS
  FROM
    ECF_NOTA_FISCAL_DETALHE NF2D
    INNER JOIN ECF_NOTA_FISCAL_CABECALHO NF2C ON NF2D.ID_NF_CABECALHO = NF2C.ID
  GROUP BY CST, NF2D.CFOP, TAXA_ICMS, NF2C.DATA_EMISSAO;


CREATE VIEW VIEW_SPED_C425 AS
  SELECT
    VD.ID_ECF_PRODUTO,
    U.SIGLA             AS DESCRICAO_UNIDADE,
    VD.TOTALIZADOR_PARCIAL,
    VC.DATA_VENDA,
    SUM(VD.QUANTIDADE)  AS SOMA_QUANTIDADE,
    SUM(VD.VALOR_TOTAL) AS SOMA_ITEM,
    SUM(VD.PIS)         AS SOMA_PIS,
    SUM(VD.COFINS)      AS SOMA_COFINS
  FROM
    ECF_VENDA_DETALHE VD
    INNER JOIN ECF_VENDA_CABECALHO VC ON VD.ID_ECF_VENDA_CABECALHO = VC.ID
    INNER JOIN PRODUTO P ON VD.ID_ECF_PRODUTO = P.ID
    INNER JOIN UNIDADE_PRODUTO U ON P.ID_UNIDADE_PRODUTO = U.ID
  GROUP BY
    ID_ECF_PRODUTO, U.SIGLA, ID_UNIDADE_PRODUTO, VD.TOTALIZADOR_PARCIAL, DATA_VENDA;

CREATE VIEW VIEW_SPED_C490 AS
  SELECT
    VD.CST,
    VD.CFOP,
    VD.TAXA_ICMS,
    VC.DATA_VENDA,
    SUM(VD.VALOR_TOTAL) AS SOMA_ITEM,
    SUM(VD.BASE_ICMS)   AS SOMA_BASE_ICMS,
    SUM(VD.ICMS)        AS SOMA_ICMS
  FROM
    ECF_VENDA_DETALHE VD
    INNER JOIN ECF_VENDA_CABECALHO VC ON VD.ID_ECF_VENDA_CABECALHO = VC.ID
  WHERE VD.CANCELADO = 'N'
  GROUP BY CST, VD.CFOP, TAXA_ICMS, DATA_VENDA;