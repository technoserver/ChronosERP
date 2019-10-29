package com.chronos.erp.controll.tributacao;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.TributCofinsCodApuracao;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 08/11/17.
 */
@Named
@ViewScoped
public class TributCofinsControll extends AbstractControll<TributCofinsCodApuracao> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<TributCofinsCodApuracao> getClazz() {
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
