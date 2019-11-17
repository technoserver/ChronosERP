alter table venda_forma_pagamento
    add column id_condicao_pagamento integer;

ALTER TABLE venda_forma_pagamento
    ADD CONSTRAINT venda_forma_pagamento_id_condicao_pagamento_fkey FOREIGN KEY (id_condicao_pagamento) REFERENCES condicoes_pagamento (id);

alter table orcamento_forma_pagamento
    add column id_condicao_pagamento integer;

ALTER TABLE orcamento_forma_pagamento
    ADD CONSTRAINT orcamento_forma_pagamento_id_condicao_pagamento_fkey FOREIGN KEY (id_condicao_pagamento) REFERENCES condicoes_pagamento (id);


alter table os_forma_pagamento
    add column id_condicao_pagamento integer;

ALTER TABLE os_forma_pagamento
    ADD CONSTRAINT os_forma_pagamento_id_condicao_pagamento_fkey FOREIGN KEY (id_condicao_pagamento) REFERENCES condicoes_pagamento (id);