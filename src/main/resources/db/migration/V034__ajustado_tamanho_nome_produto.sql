DROP VIEW IF EXISTS view_rank_produto;
DROP VIEW IF EXISTS view_quantidade_produto_vendido;
DROP VIEW IF EXISTS view_produto_empresa;

ALTER TABLE produto
  ALTER COLUMN nome TYPE VARCHAR(120);

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

CREATE OR REPLACE VIEW view_quantidade_produto_vendido AS

  SELECT
    p.id,
    v.id_empresa,
    p.nome,
    i.quantidade,
    i.valor_unitario,
    i.valor_totaL,
    v.data_venda,
    COALESCE(p.custo_unitario, 0) AS custo

  FROM produto p
    INNER JOIN venda_detalhe i ON i.id_produto = p.id
    INNER JOIN venda_cabecalho v ON i.id_venda_cabecalho = v.id
  WHERE v.situacao NOT IN ('D', 'P', 'X', 'V')

  UNION

  SELECT
    p.id,
    v.id_empresa,
    p.nome,
    i.quantidade,
    i.valor_unitario,
    i.valor_totaL,
    v.data_hora_venda,
    COALESCE(p.custo_unitario, 0) AS custo

  FROM produto p
    INNER JOIN pdv_venda_detalhe i ON i.id_produto = p.id
    INNER JOIN pdv_venda_cabecalho v ON i.id_pdv_venda_cabecalho = v.id
  WHERE v.status_venda = 'F';


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