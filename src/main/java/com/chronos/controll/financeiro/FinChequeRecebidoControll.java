package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.FinChequeRecebido;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 14/08/17.
 */
@Named
@ViewScoped
public class FinChequeRecebidoControll extends AbstractControll<FinChequeRecebido> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<FinChequeRecebido> getClazz() {
        return FinChequeRecebido.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "CHEQUE_RECEBIDO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
