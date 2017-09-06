package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.CentroResultado;
import com.chronos.modelo.entidades.PlanoCentroResultado;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 14/08/17.
 */
@Named
@ViewScoped
public class CentroResultadoControll extends AbstractControll<CentroResultado> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<PlanoCentroResultado> centrosResultado;

    public List<PlanoCentroResultado> getListaPlanoCentroResultado(String nome) {
        List<PlanoCentroResultado> listaPlanoCentroResultado = new ArrayList<>();
        try {
            listaPlanoCentroResultado = centrosResultado.getEntitys(PlanoCentroResultado.class,"nome", nome,atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaPlanoCentroResultado;
    }

    @Override
    protected Class<CentroResultado> getClazz() {
        return CentroResultado.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "CENTRO_RESULTADO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
