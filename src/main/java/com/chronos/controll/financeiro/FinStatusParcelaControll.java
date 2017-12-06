package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.FinStatusParcela;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 15/08/17.
 */
@Named
@ViewScoped
public class FinStatusParcelaControll extends AbstractControll<FinStatusParcela> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<FinStatusParcela> getClazz() {
        return FinStatusParcela.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "STATUS_PARCELA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
