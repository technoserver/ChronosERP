package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.NaturezaFinanceira;
import com.chronos.modelo.entidades.PlanoNaturezaFinanceira;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 15/08/17.
 */

@Named
@ViewScoped
public class NaturezaFinanceiraControll extends AbstractControll<NaturezaFinanceira> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<PlanoNaturezaFinanceira> planos;

    public List<PlanoNaturezaFinanceira> getListaPlanoNaturezaFinanceira(String nome) {
        List<PlanoNaturezaFinanceira> listaPlanoNaturezaFinanceira = new ArrayList<>();
        try {
            listaPlanoNaturezaFinanceira = planos.getEntitys(PlanoNaturezaFinanceira.class ,"nome", nome,atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaPlanoNaturezaFinanceira;
    }

    @Override
    protected Class<NaturezaFinanceira> getClazz() {
        return NaturezaFinanceira.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "NATUREZA_FINANCEIRA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
