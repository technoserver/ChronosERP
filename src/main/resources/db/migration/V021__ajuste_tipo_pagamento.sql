UPDATE pdv_tipo_pagamento
SET descricao = 'Credito Loja'
WHERE id = 5;
UPDATE pdv_tipo_pagamento
SET descricao = 'Duplicata Mercantil', codigo = '14'
WHERE id = 6;
UPDATE pdv_tipo_pagamento
SET descricao = 'Vale Alimentacao', codigo = '10'
WHERE id = 7;
INSERT INTO pdv_tipo_pagamento (id, codigo, descricao, permite_troco, gera_parcelas)
VALUES (8, '11', 'Vale Refeicao', 'N', 'N');
INSERT INTO pdv_tipo_pagamento (id, codigo, descricao, permite_troco, gera_parcelas)
VALUES (9, '12', 'Vale Presente', 'N', 'N');
INSERT INTO pdv_tipo_pagamento (id, codigo, descricao, permite_troco, gera_parcelas)
VALUES (10, '13', 'Vale Combustivel', 'N', 'N');
INSERT INTO pdv_tipo_pagamento (id, codigo, descricao, permite_troco, gera_parcelas)
VALUES (11, '15', 'Boleto Bancario', 'N', 'N');
INSERT INTO pdv_tipo_pagamento (id, codigo, descricao, permite_troco, gera_parcelas)
VALUES (12, '90', 'Sem pagamento', 'N', 'N');
INSERT INTO pdv_tipo_pagamento (id, codigo, descricao, permite_troco, gera_parcelas)
VALUES (13, '99', 'Outros', 'N', 'N');
