DROP TABLE IF EXISTS estoque_grade;

CREATE TABLE estoque_grade
(
    id                          serial      NOT NULL,
    id_produto                  integer     not null,
    id_empresa                  integer     not null,
    id_estoque_marca            integer,
    id_estoque_sabor            integer,
    id_estoque_tamanho          integer,
    id_estoque_cor              integer,
    id_produto_atributo_detalhe integer,
    codigo                      varchar(50) not null,
    quantidade                  numeric(18, 6) default 0,
    verificado                  numeric(18, 6) default 0,
    primary key (id),
    foreign key (id_produto) references produto (id),
    foreign key (id_empresa) references empresa (id),
    foreign key (id_estoque_marca) references estoque_marca (id),
    foreign key (id_estoque_sabor) references estoque_sabor (id),
    foreign key (id_estoque_tamanho) references estoque_tamanho (id),
    foreign key (id_estoque_cor) references estoque_cor (id),
    foreign key (id_produto_atributo_detalhe) references produto_atributo_detalhe (id)
);