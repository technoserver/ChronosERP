package com.chronos.erp.service.comercial;

import com.chronos.erp.modelo.entidades.VendaConsignadaDetalhe;
import com.chronos.erp.service.AbstractService;

public class ItemVendaConsignadaService extends AbstractService<VendaConsignadaDetalhe> {


    public boolean verificarRestricao(VendaConsignadaDetalhe item) throws Exception {
        this.objeto = item;
        return verificarRestricao();
    }

    @Override
    protected Class<VendaConsignadaDetalhe> getClazz() {
        return VendaConsignadaDetalhe.class;
    }
}
