package com.chronos.controll.vendas;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Produto;
import com.chronos.modelo.entidades.TabelaPreco;
import com.chronos.modelo.entidades.TabelaPrecoProduto;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 15/06/18.
 */
@Named
@ViewScoped
public class TabelaPrecosControll extends AbstractControll<TabelaPreco> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Produto> produtoRepository;

    private TabelaPrecoProduto item;
    private TabelaPrecoProduto itemSelecionado;


    public void incluirItem() {
        item = new TabelaPrecoProduto();
        item.setTabelaPreco(getObjeto());

    }

    public void alterarItem() {
        item = itemSelecionado;
    }

    public void salvarItem() {
        if (item.getId() == null) {
            getObjeto().getListaProduto().add(item);
        }
    }

    public void excluirItem() {
        getObjeto().getListaProduto().remove(itemSelecionado);
    }






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


    public TabelaPrecoProduto getItem() {
        return item;
    }

    public void setItem(TabelaPrecoProduto item) {
        this.item = item;
    }

    public TabelaPrecoProduto getItemSelecionado() {
        return itemSelecionado;
    }

    public void setItemSelecionado(TabelaPrecoProduto itemSelecionado) {
        this.itemSelecionado = itemSelecionado;
    }
}
