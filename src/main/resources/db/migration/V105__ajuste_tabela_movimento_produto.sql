drop function if exists movimento_produto(int, int, decimal, varchar, varchar, char, char);

ALTER TABLE estoque_produto_movimentacao
    DROP CONSTRAINT estoque_produto_movimentacao_id_produto_fkey;

alter table estoque_produto_movimentacao
    drop column id_produto;

alter table estoque_produto_movimentacao
    drop column saldo_financeiro;

alter table estoque_produto_movimentacao
    drop column valor_custo;

ALTER TABLE estoque_produto_movimentacao
    RENAME COLUMN saldo_fisico TO quantidade_anterior;

delete
from estoque_produto_movimentacao;

ALTER TABLE estoque_produto_movimentacao
    ADD COLUMN tipo char(1) not null default ('V');

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