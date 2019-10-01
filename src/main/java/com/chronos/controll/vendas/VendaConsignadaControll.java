package com.chronos.controll.vendas;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.VendaConsignadaCabecalho;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class VendaConsignadaControll extends AbstractControll<VendaConsignadaCabecalho> implements Serializable {


    @Override
    protected Class<VendaConsignadaCabecalho> getClazz() {
        return VendaConsignadaCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "VENDA_CONSIGNADA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
