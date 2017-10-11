package com.chronos.controll.sped;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Contador;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 08/10/17.
 */
@Named
@ViewScoped
public class SpedContribuicoesControll extends AbstractControll<Contador> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<Contador> getClazz() {
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
