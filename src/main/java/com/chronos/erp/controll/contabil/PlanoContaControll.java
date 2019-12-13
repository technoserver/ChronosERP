package com.chronos.erp.controll.contabil;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.PlanoConta;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class PlanoContaControll extends AbstractControll<PlanoConta> {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<PlanoConta> getClazz() {
        return PlanoConta.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PLANO_CONTA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
