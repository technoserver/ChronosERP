package com.chronos.service.comercial;

import com.chronos.modelo.entidades.VendaDetalhe;
import com.chronos.service.AbstractService;

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
