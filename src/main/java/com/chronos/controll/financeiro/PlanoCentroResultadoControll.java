package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.PlanoCentroResultado;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class PlanoCentroResultadoControll extends AbstractControll<PlanoCentroResultado> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
    }

    @Override
    protected Class<PlanoCentroResultado> getClazz() {
        return PlanoCentroResultado.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PLANO_CENTRO_RESULTADO";
    }
}
