package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.ProdutoPromocao;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ProdutoPromocaoControll extends AbstractControll<ProdutoPromocao> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<ProdutoPromocao> getClazz() {
        return ProdutoPromocao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PRODUTO_PROMOCAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
