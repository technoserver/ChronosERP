INSERT INTO funcao(nome, formulario)
VALUES ('Crédito do cliente', 'CREDITO_CLIENTE');

drop view if exists view_movimento_caixa;

CREATE OR REPLACE VIEW view_movimento_caixa AS
SELECT concat(m.id, s.id, 1)::bigint AS id,
       m.id                          AS idmovimento,
       s.id                          AS idorigem,
       s.data_sangria                AS data_hora,
       'Saída - Sangria'::text       AS descricao,
       s.valor,
       '01'::text                    AS codigo_forma_pagamento,
       'Dinheiro'::text              AS forma_pagamento,
       'S'::text                     AS tipo,
       'Sangria'::text               AS origem,
       s.observacao
FROM pdv_movimento m
         JOIN pdv_sangria s ON s.id_pdv_movimento = m.id
UNION
SELECT concat(m.id, s.id, 2)::bigint AS id,
       m.id                          AS idmovimento,
       s.id                          AS idorigem,
       s.data_suprimento             AS data_hora,
       'Entrada - Acrécimo'::text    AS descricao,
       s.valor,
       '01'::text                    AS codigo_forma_pagamento,
       'Dinheiro'::text              AS forma_pagamento,
       'E'::text                     AS tipo,
       'Suprimentos'::text           AS origem,
       s.observacao
FROM pdv_movimento m
         JOIN pdv_suprimento s ON s.id_pdv_movimento = m.id
UNION
SELECT concat(m.id, v.id, t.id, 3)::bigint AS id,
       m.id                                AS idmovimento,
       v.id                                AS idorigem,
       v.data_hora_venda                   AS data_hora,
       'Venda nº '::text || v.id           AS descricao,
       f.valor,
       t.codigo                            AS codigo_forma_pagamento,
       t.descricao                         AS forma_pagamento,
       'E'::text                           AS tipo,
       'PDV'::text                         AS origem,
       ''::text                            AS observacao
FROM pdv_movimento m
         JOIN pdv_venda_cabecalho v ON v.id_pdv_movimento = m.id
         JOIN pdv_forma_pagamento f ON f.id_pdv_venda_cabecalho = v.id
         JOIN tipo_pagamento t ON f.id_tipo_pagamento = t.id
UNION
SELECT concat(m.id, v.id, t.id, 3)::bigint AS id,
       m.id                                AS idmovimento,
       v.id                                AS idorigem,
       v.data_hora_venda                   AS data_hora,
       'Venda nº '::text || v.id           AS descricao,
       f.valor,
       t.codigo                            AS codigo_forma_pagamento,
       t.descricao                         AS forma_pagamento,
       'E'::text                           AS tipo,
       'PDV'::text                         AS origem,
       ''::text                            AS observacao
FROM pdv_movimento m
         JOIN pdv_venda_cabecalho v ON v.id_pdv_movimento = m.id
         JOIN pdv_forma_pagamento f ON f.id_pdv_venda_cabecalho = v.id
         JOIN tipo_pagamento t ON f.id_tipo_pagamento = t.id
UNION
SELECT concat(m.id, o.id, t.id, 3)::bigint AS id,
       m.id                                AS idmovimento,
       o.id                                AS idorigem,
       o.data_inicio                       AS data_hora,
       'OS nº '::text || o.id              AS descricao,
       f.valor,
       t.codigo                            AS codigo_forma_pagamento,
       t.descricao                         AS forma_pagamento,
       'E'::text                           AS tipo,
       'OS'::text                          AS origem,
       ''::text                            AS observacao
FROM pdv_movimento m
         JOIN os_abertura o ON o.id_movimento = m.id
         JOIN os_forma_pagamento f ON f.id_os_abertura = o.id
         JOIN tipo_pagamento t ON f.id_tipo_pagamento = t.id
ORDER BY 4;