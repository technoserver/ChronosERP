package com.chronos.controll.vendas;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.TabelaPreco;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 15/06/18.
 */
@Named
@ViewScoped
public class TabelaPrecosControll extends AbstractControll<TabelaPreco> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<TabelaPreco> getClazz() {
        return TabelaPreco.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "TABELA_PRECO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
