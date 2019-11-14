CREATE TABLE orcamento_forma_pagamento
(
    id                     serial  NOT NULL,
    id_tipo_pagamento      integer NOT NULL,
    id_orcamento_cabecalho integer NOT NULL,
    forma                  char(2),
    valor                  decimal(18, 6),
    cartao_tipo_integracao char(1),
    cnpj_operadora_cartao  varchar(14),
    bandeira               char(2),
    numero_autorizacao     varchar(20),
    estorno                char(1),
    troco                  decimal(18, 6),
    PRIMARY KEY (id),
    FOREIGN KEY (id_tipo_pagamento) REFERENCES tipo_pagamento (id),
    FOREIGN KEY (id_orcamento_cabecalho) REFERENCES orcamento_cabecalho (id)
);

insert into orcamento_forma_pagamento (id_orcamento_cabecalho, id_tipo_pagamento, forma, valor, estorno, troco)
select o.id, 6, '14', o.valor_total, 'N', 0
from orcamento_cabecalho o;

alter table orcamento_cabecalho
    drop column id_venda_condicoes_pagamento;

CREATE TABLE venda_forma_pagamento
(
    id                     serial  NOT NULL,
    id_tipo_pagamento      integer NOT NULL,
    id_venda_cabecalho     integer NOT NULL,
    forma                  char(2),
    valor                  decimal(18, 6),
    cartao_tipo_integracao char(1),
    cnpj_operadora_cartao  varchar(14),
    bandeira               char(2),
    numero_autorizacao     varchar(20),
    estorno                char(1),
    troco                  decimal(18, 6),
    PRIMARY KEY (id),
    FOREIGN KEY (id_tipo_pagamento) REFERENCES tipo_pagamento (id),
    FOREIGN KEY (id_venda_cabecalho) REFERENCES venda_cabecalho (id)
);

insert into venda_forma_pagamento (id_venda_cabecalho, id_tipo_pagamento, forma, valor, estorno, troco)
select o.id, 6, '14', o.valor_total, 'N', 0
from venda_cabecalho o;

alter table venda_cabecalho
    drop column id_venda_condicoes_pagamento;

ALTER TABLE venda_condicoes_pagamento
    RENAME TO condicoes_pagamento;

ALTER SEQUENCE venda_condicoes_pagamento_id_seq RENAME TO condicoes_pagamento_id_seq;

ALTER TABLE venda_condicoes_parcelas
    RENAME TO condicoes_parcelas;

ALTER SEQUENCE venda_condicoes_parcelas_id_seq RENAME TO condicoes_parcelas_id_seq;