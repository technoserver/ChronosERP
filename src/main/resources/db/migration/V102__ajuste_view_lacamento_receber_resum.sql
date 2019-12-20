drop view if exists view_fin_lancamento_receber_resum;

CREATE OR REPLACE VIEW view_fin_lancamento_receber_resum AS
select l.id,
       l.id_empresa,
       l.numero_documento,
       p.nome,
       l.quantidade_parcela,
       l.valor_total,
       (l.valor_total - sum(prb.valor_recebido)) as saldo,
       l.data_lancamento,
       (
           CASE
               WHEN (l.valor_total - sum(prb.valor_recebido)) = 0 THEN
                   'Q'
               WHEN (l.valor_total - sum(prb.valor_recebido)) > 0 THEN
                   'QP'
               ELSE
                   'A'
               END
           )                                     AS status

from fin_lancamento_receber l
         inner join cliente c on c.id = l.id_cliente
         inner join pessoa p on p.id = c.id_pessoa
         inner join fin_parcela_receber pr on pr.id_fin_lancamento_receber = l.id
         left join fin_parcela_recebimento prb on prb.id_fin_parcela_receber = pr.id
group by l.id, p.nome