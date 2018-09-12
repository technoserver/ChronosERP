DROP VIEW IF EXISTS view_sped_c190;
DROP VIEW IF EXISTS view_sped_nfe_detalhe;

ALTER TABLE nfe_detalhe
  ALTER COLUMN nome_produto TYPE VARCHAR(120);

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