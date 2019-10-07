package com.chronos.controll.pdv;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.TipoPagamento;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * Created by john on 01/02/18.
 */
@Named
@ViewScoped
public class PdvTipoPagamentoControll extends AbstractControll<TipoPagamento> {


    @Override
    protected Class<TipoPagamento> getClazz() {
        return TipoPagamento.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PDV_TIPO_PAGAMENTO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
