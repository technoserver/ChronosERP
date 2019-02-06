CREATE OR REPLACE VIEW view_fin_lancamento_receber AS
  SELECT lr.id                                                          AS id,
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
  SELECT lr.id                                                          AS id,
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
         INNER JOIN fin_status_parcela s ON (pr.id_fin_status_parcela = s.id)
         INNER JOIN conta_caixa cc ON (pr.id_conta_caixa = cc.id)
         INNER JOIN fin_documento_origem doc ON (lr.id_fin_documento_origem = doc.id)
         INNER JOIN cliente c ON (lr.id_cliente = c.id)
         INNER JOIN pessoa p ON (c.id_pessoa = p.id)
         INNER JOIN pessoa_juridica pj ON (pj.id_pessoa = p.id);

INSERT INTO funcao (nome, formulario)
values ('NFE', 'NFE');