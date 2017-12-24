CREATE OR REPLACE FUNCTION movimento_produto(idproduto      INTEGER, idempresa INTEGER, quantidade NUMERIC,
                                             data_movimento TIMESTAMP, documento VARCHAR, tipo CHAR)
  RETURNS TEXT AS
$$
DECLARE   saldo_atual      NUMERIC;
  DECLARE valor_custo      NUMERIC;
  DECLARE saldo_financeiro NUMERIC;
BEGIN
  saldo_atual := (SELECT quantidade_estoque
                  FROM empresa_produto
                  WHERE id_produto = $1 AND id_empresa = $2);
  valor_custo := (SELECT COALESCE(custo_unitario, 0)
                  FROM produto p INNER JOIN empresa_produto ep ON ep.id_produto = p.id
                  WHERE id_produto = $1 AND id_empresa = $2);
  saldo_atual := saldo_atual + $3;
  saldo_financeiro := valor_custo * saldo_atual;

  INSERT INTO estoque_produto_movimentacao (id_produto, documento, quantidade, valor_custo, saldo_fisico, saldo_financeiro, entrada_saida, data_movimento)
  VALUES ($1, $5, $3, valor_custo, saldo_atual, saldo_financeiro, $6, now());

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