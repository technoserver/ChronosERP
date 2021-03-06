DROP VIEW IF EXISTS view_quantidade_produto_vendido;
CREATE OR REPLACE VIEW view_quantidade_produto_vendido AS
    SELECT p.id,
           v.id_empresa,
           g.id                          as id_grupo,
           s.id                          as id_subgrupo,
           p.nome,
           g.nome                        as grupo,
           s.nome                        as subgrupo,
           i.quantidade,
           i.valor_unitario,
           i.valor_totaL,
           v.data_venda,
           COALESCE(p.custo_unitario, 0) AS custo

    FROM produto p
             INNER JOIN produto_subgrupo s ON s.id = p.id_subgrupo
             INNER JOIN produto_grupo g ON g.id = s.id_grupo
             INNER JOIN venda_detalhe i ON i.id_produto = p.id
             INNER JOIN venda_cabecalho v ON i.id_venda_cabecalho = v.id
    WHERE v.situacao NOT IN ('D', 'P', 'X', 'V')

    UNION

    SELECT p.id,
           v.id_empresa,
           g.id                          as id_grupo,
           s.id                          as id_subgrupo,
           p.nome,
           g.nome                        as grupo,
           s.nome                        as subgrupo,
           i.quantidade,
           i.valor_unitario,
           i.valor_totaL,
           v.data_hora_venda,
           COALESCE(p.custo_unitario, 0) AS custo

    FROM produto p
             INNER JOIN produto_subgrupo s ON s.id = p.id_subgrupo
             INNER JOIN produto_grupo g ON g.id = s.id_grupo
             INNER JOIN pdv_venda_detalhe i ON i.id_produto = p.id
             INNER JOIN pdv_venda_cabecalho v ON i.id_pdv_venda_cabecalho = v.id
    WHERE v.status_venda IN ('F', 'E')
    UNION

    SELECT p.id,
           os.id_empresa,
           g.id                          as id_grupo,
           s.id                          as id_subgrupo,
           p.nome,
           g.nome                        as grupo,
           s.nome                        as subgrupo,
           ps.quantidade,
           ps.valor_unitario,
           ps.valor_totaL,
           os.data_fim,
           COALESCE(p.custo_unitario, 0) AS custo

    FROM produto p
             INNER JOIN produto_subgrupo s ON s.id = p.id_subgrupo
             INNER JOIN produto_grupo g ON g.id = s.id_grupo
             INNER JOIN os_produto_servico ps ON ps.id_produto = p.id
             INNER JOIN os_abertura os ON ps.id_os_abertura = os.id
    WHERE p.servico = 'N'
      and os.status in (12, 13);