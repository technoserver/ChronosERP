DROP VIEW IF EXISTS view_quantidade_produto_vendido;
CREATE OR REPLACE VIEW view_quantidade_produto_vendido
AS
SELECT p.id,
       v.id_empresa,
       p.nome,
       i.quantidade,
       i.valor_unitario,
       i.valor_total,
       v.data_venda,
       COALESCE(p.custo_unitario, 0::numeric) AS custo
FROM produto p
         JOIN venda_detalhe i ON i.id_produto = p.id
         JOIN venda_cabecalho v ON i.id_venda_cabecalho = v.id
WHERE v.situacao in ('F', 'E')
UNION
SELECT p.id,
       v.id_empresa,
       p.nome,
       i.quantidade,
       i.valor_unitario,
       i.valor_total,
       v.data_hora_venda::date                AS data_venda,
       COALESCE(p.custo_unitario, 0::numeric) AS custo
FROM produto p
         JOIN pdv_venda_detalhe i ON i.id_produto = p.id
         JOIN pdv_venda_cabecalho v ON i.id_pdv_venda_cabecalho = v.id
WHERE v.status_venda in ('F', 'E')
UNION
SELECT p.id,
       o.id_empresa,
       p.nome,
       i.quantidade,
       i.valor_unitario,
       i.valor_total,
       o.data_fim                             AS data_venda,
       COALESCE(p.custo_unitario, 0::numeric) AS custo
FROM produto p
         JOIN os_produto_servico i ON i.id_produto = p.id
         JOIN os_abertura o ON i.id_os_abertura = o.id
WHERE o.status in (12, 13)
  AND p.servico = 'N';

drop function if exists movimento_produto(int, int, decimal, varchar, varchar, char);
drop function if exists movimento_produto(int, int, decimal, varchar, varchar, char, char);

CREATE OR REPLACE FUNCTION movimento_produto(idempresa INTEGER,
                                             idproduto INTEGER,
                                             quant DECIMAL,
                                             codigo_modulo VARCHAR,
                                             doc VARCHAR,
                                             tipo CHAR,
                                             entrada_saida CHAR)
    RETURNS SETOF ESTOQUE_PRODUTO_MOVIMENTACAO AS
$$
DECLARE
    saldo_atual                 DECIMAL;
    DECLARE idproduto_movimento INTEGER;
    DECLARE id_empresa_produto  INTEGER;
BEGIN
    saldo_atual := (SELECT (CASE
                                WHEN $6 = 'F' THEN ep.quantidade_estoque
                                ELSE ep.estoque_verificado
        END)
                    FROM empresa_produto ep
                    WHERE ep.id_produto = $2
                      AND ep.id_empresa = $1);
    id_empresa_produto := (SELECT ep.id
                           FROM empresa_produto ep
                           WHERE ep.id_produto = $2
                             AND ep.id_empresa = $1);


    INSERT INTO estoque_produto_movimentacao ( id_empresa_produto, codigo_modulo, documento, quantidade
                                             , quantidade_anterior, tipo, entrada_saida, data_movimento)
    VALUES (id_empresa_produto, $4, $5, $3, saldo_atual, $6, $7, current_date)
    RETURNING estoque_produto_movimentacao.id
        INTO idproduto_movimento;

    RETURN QUERY (SELECT m.*
                  FROM estoque_produto_movimentacao m
                  WHERE m.id = idproduto_movimento);

END ;
$$ LANGUAGE plpgsql;