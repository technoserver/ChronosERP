package com.chronos.controll.nfe;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.NfeCabecalho;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 26/09/17.
 */
@Named
@ViewScoped
public class NfeCabecalhoControll extends AbstractControll<NfeCabecalho> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<NfeCabecalho> getClazz() {
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
