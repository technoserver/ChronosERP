update pdv_forma_pagamento
set cartao_tipo_integracao = '2'
where forma in ('03', '04')
  and cartao_tipo_integracao is null;

update venda_forma_pagamento
set cartao_tipo_integracao = '2'
where forma in ('03', '04')
  and cartao_tipo_integracao is null;

update orcamento_forma_pagamento
set cartao_tipo_integracao = '2'
where forma in ('03', '04')
  and cartao_tipo_integracao is null;

update nfe_forma_pagamento
set cartao_tipo_integracao = '2'
where forma in ('03', '04')
  and cartao_tipo_integracao is null;