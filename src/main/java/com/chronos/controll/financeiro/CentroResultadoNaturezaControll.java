package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.CtResultadoNtFinanceira;
import com.chronos.modelo.entidades.NaturezaFinanceira;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 25/12/17.
 */
@Named
@ViewScoped
public class CentroResultadoNaturezaControll extends AbstractControll<CtResultadoNtFinanceira> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<NaturezaFinanceira> financeiraRepository;

    public List<NaturezaFinanceira> getListnaturezaFinanceira(String nome) {

        List<NaturezaFinanceira> naturezas = new ArrayList<>();


        return naturezas;

    }

    @Override
    protected Class<CtResultadoNtFinanceira> getClazz() {
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
