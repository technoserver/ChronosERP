DROP VIEW IF EXISTS view_rank_produto;
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