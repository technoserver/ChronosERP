INSERT INTO funcao(nome, formulario)
VALUES ('Tamanho do Produto', 'ESTOQUE_TAMANHO');

INSERT INTO funcao(nome, formulario)
VALUES ('Cor do Produto', 'ESTOQUE_COR');

INSERT INTO funcao(nome, formulario)
VALUES ('Ordem de Produção', 'PCP_ORDER_PRODUCAO');

alter table pdv_forma_pagamento
    add column id_condicao_pagamento integer;

ALTER TABLE pdv_forma_pagamento
    ADD CONSTRAINT pdv_forma_pagamento_id_condicao_pagamento_fkey FOREIGN KEY (id_condicao_pagamento) REFERENCES condicoes_pagamento (id);

ALTER TABLE pdv_forma_pagamento
    DROP CONSTRAINT pdv_forma_pagamento_id_pdv_tipo_pagamento_fkey;

ALTER TABLE pdv_forma_pagamento
    RENAME COLUMN id_pdv_tipo_pagamento TO id_tipo_pagamento;

ALTER TABLE pdv_forma_pagamento
    ADD CONSTRAINT comissao_id_pdv_tipo_pagamento_fkey FOREIGN KEY (id_tipo_pagamento) REFERENCES tipo_pagamento (id);