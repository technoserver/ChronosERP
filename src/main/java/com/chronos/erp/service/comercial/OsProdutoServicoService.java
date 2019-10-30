package com.chronos.erp.service.comercial;

import com.chronos.erp.modelo.entidades.OsProdutoServico;
import com.chronos.erp.service.AbstractService;

public class OsProdutoServicoService extends AbstractService<OsProdutoServico> {

    public boolean verificarRestricao(OsProdutoServico item) throws Exception {
        this.objeto = item;
        return verificarRestricao();
    }

    @Override
    protected Class<OsProdutoServico> getClazz() {
        return OsProdutoServico.class;
    }
}
