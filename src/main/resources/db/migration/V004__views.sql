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
  WHERE p.cliente = 'S' :: BPCHAR AND e.principal = 'S' :: BPCHAR;
