drop view if exists view_movimento_caixa;

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