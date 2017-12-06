package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.FinTipoPagamento;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 15/08/17.
 */
@Named
@ViewScoped
public class FinTipoPagamentoControll extends AbstractControll<FinTipoPagamento> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<FinTipoPagamento> getClazz() {
        return FinTipoPagamento.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "TIPO_PAGAMENTO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
