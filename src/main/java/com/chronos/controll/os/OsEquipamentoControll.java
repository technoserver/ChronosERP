package com.chronos.controll.os;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.OsEquipamento;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 28/09/17.
 */
@Named
@ViewScoped
public class OsEquipamentoControll extends AbstractControll<OsEquipamento> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<OsEquipamento> getClazz() {
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
