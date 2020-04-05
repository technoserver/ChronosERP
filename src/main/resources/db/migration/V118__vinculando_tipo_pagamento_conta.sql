ALTER TABLE fin_tipo_pagamento
    ADD COLUMN id_conta_caixa integer not null default 1;

ALTER TABLE fin_tipo_pagamento
    ADD CONSTRAINT fk_fin_tipo_pagamento_conta_caixa FOREIGN KEY (id_conta_caixa) REFERENCES conta_caixa (id);