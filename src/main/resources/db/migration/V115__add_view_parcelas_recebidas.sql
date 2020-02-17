CREATE OR REPLACE VIEW view_parcelas_recebidas AS
select r.id,
       l.id                          as id_lancamento,
       r.valor_recebido,
       COALESCE(r.valor_desconto, 0) as valor_desconto,
       COALESCE(r.valor_juro, 0)     as valor_juro,
       COALESCE(r.valor_multa, 0)    as valor_multa,
       r.data_recebimento
from fin_parcela_recebimento r
         inner join fin_parcela_receber p on p.id = r.id_fin_parcela_receber
         inner join fin_lancamento_receber l on l.id = p.id_fin_lancamento_receber;


CREATE OR REPLACE VIEW view_saldo_lancamento_receber AS
select l.id,
       l.valor_total - COALESCE((
                                    select sum(pr.valor_recebido + pr.valor_desconto)
                                    from view_parcelas_recebidas pr
                                    where pr.id_lancamento = l.id), 0) as saldo
from fin_lancamento_receber l;

CREATE OR REPLACE VIEW view_fin_lancamento_receber_resum AS
SELECT l.id,
       l.id_empresa,
       l.numero_documento,
       p.nome,
       l.quantidade_parcela,
       l.valor_total,
       (select s.saldo from view_saldo_lancamento_receber s where s.id = l.id) as saldo,
       l.data_lancamento,
       CASE
           WHEN (select s.saldo from view_saldo_lancamento_receber s where id = l.id) = l.valor_total THEN 'A'
           WHEN (select s.saldo from view_saldo_lancamento_receber s where id = l.id) <= 0 THEN 'Q'
           WHEN (select s.saldo from view_saldo_lancamento_receber s where id = l.id) > 0 THEN 'QP'
           ELSE 'A'
           END                                                                 AS status
FROM john.fin_lancamento_receber l
         JOIN john.cliente c ON c.id = l.id_cliente
         JOIN john.pessoa p ON p.id = c.id_pessoa
GROUP BY l.id, p.nome;