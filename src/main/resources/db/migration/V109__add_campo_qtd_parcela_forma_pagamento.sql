ALTER TABLE os_forma_pagamento
    add column qtd_parcela integer default 1;

ALTER TABLE pdv_forma_pagamento
    add column qtd_parcela integer default 1;

ALTER TABLE venda_forma_pagamento
    add column qtd_parcela integer default 1;