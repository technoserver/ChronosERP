CREATE OR REPLACE FUNCTION movimento_produto(
  idproduto     INTEGER,
  idempresa     INTEGER,
  quant         DECIMAL,
  codigo_modulo VARCHAR,
  doc           VARCHAR,
  tipo          CHAR)
  RETURNS SETOF ESTOQUE_PRODUTO_MOVIMENTACAO AS
$$
DECLARE   saldo_atual         DECIMAL;
  DECLARE valor_custo         DECIMAL;
  DECLARE saldo_financeiro    DECIMAL;
  DECLARE idproduto_movimento INTEGER;
  DECLARE id_empresa_produto  INTEGER;
BEGIN
  saldo_atual := (SELECT ep.quantidade_estoque
                  FROM empresa_produto ep
                  WHERE ep.id_produto = $1 AND ep.id_empresa = $2);
  id_empresa_produto := (SELECT ep.id
                         FROM empresa_produto ep
                         WHERE ep.id_produto = $1 AND ep.id_empresa = $2);
  valor_custo := (SELECT COALESCE(custo_unitario, 0)
                  FROM produto p INNER JOIN empresa_produto ep ON ep.id_produto = p.id
                  WHERE id_produto = $1 AND id_empresa = $2);
  saldo_atual := (saldo_atual + $3);
  saldo_financeiro := valor_custo * saldo_atual;

  INSERT INTO estoque_produto_movimentacao (id_produto, id_empresa_produto, codigo_modulo, documento, quantidade, valor_custo, saldo_fisico, saldo_financeiro, entrada_saida, data_movimento)
  VALUES ($1, id_empresa_produto, $4, $5, $3, valor_custo, saldo_atual, saldo_financeiro, $6, current_date)
  RETURNING estoque_produto_movimentacao.id
    INTO idproduto_movimento;

  RETURN QUERY (SELECT m.*
                FROM estoque_produto_movimentacao m
                WHERE m.id = idproduto_movimento);

END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION criar_tabela(tabela TEXT, comando TEXT)
  RETURNS TEXT AS $$
BEGIN
  IF EXISTS(
      SELECT *
      FROM pg_catalog.pg_tables
      WHERE tablename = tabela
  )
  THEN
    RETURN 'TABELA ' || '''' || tabela || '''' || ' JÁ EXISTE';
  ELSE
    EXECUTE comando;
    RETURN 'TABELA CRIADA';
  END IF;
END;
$$ LANGUAGE plpgsql;

--prox
CREATE OR REPLACE FUNCTION alterar_tabela(tabela TEXT, comando TEXT)
  RETURNS TEXT AS $$
BEGIN
  IF EXISTS(
      SELECT *
      FROM pg_catalog.pg_tables
      WHERE tablename = tabela
  )
  THEN
    EXECUTE comando;
    RETURN 'TABELA ALTERADA';

  ELSE
    RETURN 'TABELA ' || '''' || tabela || '''' || ' NÂO EXISTE';
  END IF;
END;
$$ LANGUAGE plpgsql;
--prox
CREATE OR REPLACE FUNCTION criar_coluna(tabela TEXT, coluna TEXT, comando TEXT)
  RETURNS TEXT AS $$
BEGIN
  IF EXISTS(
      SELECT column_name
      FROM information_schema.columns
      WHERE table_name = tabela AND column_name = coluna
  )
  THEN
    RETURN 'COLUNA ' || '''' || coluna || '''' || ' JÁ EXISTE';
  ELSE
    EXECUTE comando;
    RETURN 'COLUNNA CRIADA';
  END IF;
END;
$$ LANGUAGE plpgsql;
--prox
CREATE OR REPLACE FUNCTION alterar_coluna(tabela TEXT, coluna TEXT, comando TEXT)
  RETURNS TEXT AS $$
BEGIN
  IF EXISTS(
      SELECT column_name
      FROM information_schema.columns
      WHERE table_name = tabela AND column_name = coluna
  )
  THEN
    EXECUTE comando;
    RETURN 'COLUNA ' || '''' || coluna || '''' || ' ALTERADA';
  ELSE

    RETURN 'COLUNNA INEXISTENTE';
  END IF;
END;
$$ LANGUAGE plpgsql;