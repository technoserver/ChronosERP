package com.chronos.controll.nfce;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.NfceCaixa;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 04/10/17.
 */
@Named
@ViewScoped
public class NfceMovimentoControll extends AbstractControll<NfceCaixa> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<NfceCaixa> getClazz() {
        return null;
    }

    @Override
    protected String getFuncaoBase() {
        return null;
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
