package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.ProdutoAtributo;
import com.chronos.modelo.entidades.ProdutoAtributoDetalhe;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ProdutoAtributoControll extends AbstractControll<ProdutoAtributo> implements Serializable {

    private static final long serialVersionUID = 1L;

    private ProdutoAtributoDetalhe atributoSelecionado;
    private ProdutoAtributoDetalhe atributo;

    @Override
    public void doEdit() {
        super.doEdit();
    }

    @Override
    public void salvar() {
        super.salvar();
    }


    public void incluirAtributo() {

    }


    public void excluirAtributo() {

    }

    public void removerAtributo() {

    }

    @Override
    protected Class<ProdutoAtributo> getClazz() {
        return ProdutoAtributo.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PRODUTO_ATRIBUTO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public ProdutoAtributoDetalhe getAtributoSelecionado() {
        return atributoSelecionado;
    }

    public void setAtributoSelecionado(ProdutoAtributoDetalhe atributoSelecionado) {
        this.atributoSelecionado = atributoSelecionado;
    }

    public ProdutoAtributoDetalhe getAtributo() {
        return atributo;
    }

    public void setAtributo(ProdutoAtributoDetalhe atributo) {
        this.atributo = atributo;
    }
}
