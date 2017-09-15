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
    ep.quantidade_estoque AS quantidade,
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
