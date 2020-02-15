DROP VIEW IF EXISTS view_movimento_caixa;
DROP VIEW IF EXISTS view_quantidade_produto_vendido;

ALTER TABLE pdv_sangria
    ALTER COLUMN data_sangria TYPE TIMESTAMP USING data_sangria::TIMESTAMP;

ALTER TABLE pdv_suprimento
    ALTER COLUMN data_suprimento TYPE TIMESTAMP USING data_suprimento::TIMESTAMP;

ALTER TABLE os_abertura
    ALTER COLUMN data_inicio TYPE TIMESTAMP USING data_inicio::TIMESTAMP;

ALTER TABLE os_abertura
    ALTER COLUMN data_fim TYPE TIMESTAMP USING data_fim::TIMESTAMP;

CREATE OR REPLACE VIEW view_movimento_caixa AS

SELECT concat(m.id, s.id, 1) AS id,
       m.id                  AS idmovimento,
       s.id                  AS idorigem,
       s.data_sangria        AS data_hora,
       'Saída - Sangria'     AS descricao,
       s.valor,
       '01'                  AS codigo_forma_pagamento,
       'Dinheiro'            AS forma_pagamento,
       'S'                   AS tipo,
       'Sangria'             AS origem,
       s.observacao
FROM pdv_movimento m
         JOIN pdv_sangria s ON s.id_pdv_movimento = m.id
UNION

SELECT concat(m.id, s.id, 2) AS id,
       m.id                  AS idmovimento,
       s.id                  AS idorigem,
       s.data_suprimento     AS data_hora,
       'Entrada - Acrécimo'  AS descricao,
       s.valor,
       '01'                  AS codigo_forma_pagamento,
       'Dinheiro'            AS forma_pagamento,
       'E'                   AS tipo,
       'Suprimentos'         AS origem,
       s.observacao
FROM pdv_movimento m
         JOIN pdv_suprimento s ON s.id_pdv_movimento = m.id

UNION

select concat(m.id, r.id, 3)                           AS id,
       m.id                                            AS idmovimento,
       r.id                                            AS idorigem,
       r.data_recebimento                              AS data_hora,
       ('Recebimento n°' || r.id) || ' de ' || ps.nome AS descricao,
       r.valor_recebido,
       t.tipo                                          AS codigo_forma_pagamento,
       t.descricao                                     AS forma_pagamento,
       'E'                                             AS tipo,
       'RECEBIMENTO'                                   AS origem,
       r.historico                                     AS observacao

from pdv_movimento m
         inner join pdv_operador o ON o.id = m.id_pdv_operador
         inner join fin_parcela_recebimento r on r.id_pdv_movimento = m.id
         inner join fin_parcela_receber p on p.id = r.id_fin_parcela_receber
         inner join fin_lancamento_receber l on l.id = p.id_fin_lancamento_receber
         inner join fin_tipo_recebimento t on t.id = r.id_fin_tipo_recebimento
         inner join cliente c on c.id = l.id_cliente
         inner join pessoa ps on ps.id = c.id_pessoa

UNION

SELECT concat(m.id, v.id, t.id, 4) AS id,
       m.id                        AS idmovimento,
       v.id                        AS idorigem,
       v.data_hora_venda           AS data_hora,
       'Venda nº ' || v.id         AS descricao,
       f.valor,
       t.codigo                    AS codigo_forma_pagamento,
       t.descricao                 AS forma_pagamento,
       'E'                         AS tipo,
       'PDV'                       AS origem,
       ''                          AS observacao
FROM pdv_movimento m
         JOIN pdv_venda_cabecalho v ON v.id_pdv_movimento = m.id
         JOIN pdv_forma_pagamento f ON f.id_pdv_venda_cabecalho = v.id
         JOIN tipo_pagamento t ON f.id_tipo_pagamento = t.id

UNION

SELECT concat(m.id, o.id, t.id, 6) AS id,
       m.id                        AS idmovimento,
       o.id                        AS idorigem,
       o.data_inicio               AS data_hora,
       'OS nº ' || o.id            AS descricao,
       f.valor,
       t.codigo                    AS codigo_forma_pagamento,
       t.descricao                 AS forma_pagamento,
       'E'                         AS tipo,
       'OS'                        AS origem,
       ''                          AS observacao
FROM pdv_movimento m
         JOIN os_abertura o ON o.id_movimento = m.id
         JOIN os_forma_pagamento f ON f.id_os_abertura = o.id
         JOIN tipo_pagamento t ON f.id_tipo_pagamento = t.id
ORDER BY 4;

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