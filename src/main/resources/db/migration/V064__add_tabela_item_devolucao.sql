DROP TABLE IF EXISTS venda_devolucao_item;

create table venda_devolucao_item
(
    id                 serial         not null,
    id_venda_devolucao integer        not null,
    id_produto         integer        not null,
    quantidade         decimal(18, 6) not null,
    valor              decimal(18, 6) not null,
    primary key (id),
    foreign key (id_venda_devolucao) references venda_devolucao (id),
    foreign key (id_produto) references produto (id)
);

drop view view_quantidade_produto_vendido;

ALTER TABLE venda_cabecalho
    ALTER COLUMN situacao TYPE varchar(2);

CREATE OR REPLACE VIEW view_quantidade_produto_vendido AS
    SELECT p.id,
           v.id_empresa,
           p.nome,
           i.quantidade,
           i.valor_unitario,
           i.valor_totaL,
           v.data_venda,
           COALESCE(p.custo_unitario, 0) AS custo

    FROM produto p
             INNER JOIN venda_detalhe i ON i.id_produto = p.id
             INNER JOIN venda_cabecalho v ON i.id_venda_cabecalho = v.id
    WHERE v.situacao in ('F', 'E', 'DV', 'DP')

    UNION

    SELECT p.id,
           v.id_empresa,
           p.nome,
           i.quantidade,
           i.valor_unitario,
           i.valor_totaL,
           v.data_hora_venda,
           COALESCE(p.custo_unitario, 0) AS custo

    FROM produto p
             INNER JOIN pdv_venda_detalhe i ON i.id_produto = p.id
             INNER JOIN pdv_venda_cabecalho v ON i.id_pdv_venda_cabecalho = v.id
    WHERE v.status_venda = 'F'
       or v.status_venda = 'E';