package com.chronos.controll.tributacao;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.TributIcmsUf;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 08/11/17.
 */
@Named
@ViewScoped
public class TributIcmsControll extends AbstractControll<TributIcmsUf> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<TributIcmsUf> getClazz() {
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
