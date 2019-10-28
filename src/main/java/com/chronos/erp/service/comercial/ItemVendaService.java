package com.chronos.erp.service.comercial;

import com.chronos.erp.modelo.entidades.VendaDetalhe;
import com.chronos.erp.service.AbstractService;

public class ItemVendaService extends AbstractService<VendaDetalhe> {


    public boolean verificarRestricao(VendaDetalhe item) throws Exception {
        this.objeto = item;
        return verificarRestricao();
    }

    @Override
    protected Class<VendaDetalhe> getClazz() {
        return VendaDetalhe.class;
    }
}
