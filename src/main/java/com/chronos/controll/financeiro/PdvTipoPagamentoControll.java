package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.PdvTipoPagamento;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * Created by john on 01/02/18.
 */
@Named
@ViewScoped
public class PdvTipoPagamentoControll extends AbstractControll<PdvTipoPagamento> {


    @Override
    protected Class<PdvTipoPagamento> getClazz() {
        return PdvTipoPagamento.class;
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
