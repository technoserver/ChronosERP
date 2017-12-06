package com.chronos.controll.os;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.OsAberturaEquipamento;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 28/09/17.
 */
@Named
@ViewScoped
public class OsHistoricoEquipamentoControll extends AbstractControll<OsAberturaEquipamento> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<OsAberturaEquipamento> getClazz() {
        return null;
    }

    @Override
    protected String getFuncaoBase() {
        return "OS_HISTORICO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
