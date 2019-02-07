package com.chronos.controll.vendas;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Produto;
import com.chronos.modelo.entidades.TabelaPreco;
import com.chronos.modelo.entidades.TabelaPrecoProduto;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.service.comercial.TabelaPrecoService;
import com.chronos.util.jsf.Mensagem;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by john on 15/06/18.
 */
@Named
@ViewScoped
public class TabelaPrecosControll extends AbstractControll<TabelaPreco> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, String> aplicado;

    @Inject
    private TabelaPrecoService service;

    @Inject
    private Repository<Produto> produtoRepository;

    private TabelaPrecoProduto item;
    private TabelaPrecoProduto itemSelecionado;


    @PostConstruct
    @Override
    public void init() {
        super.init();
        aplicado = new HashMap<>();
        aplicado.put("Cliente/Vendedor", "C");
        aplicado.put("Vendedor/Região", "R");
    }

    @Override
    public void doEdit() {
        super.doEdit();
        TabelaPreco tabelaPreco = dataModel.getRowData(getObjetoSelecionado().getId().toString());
        setObjeto(tabelaPreco);
        setTelaGrid(false);
    }

    @Override
    public void salvar() {
        try {
            setObjeto(service.salvar(getObjeto()));
            Mensagem.addInfoMessage("Registro salvo com sucesso");
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException(ex);
            }
        }
    }

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


    public void incluirTodosProduto() {

        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro("inativo", "N"));
        filtros.add(new Filtro("excluido", "N"));
        filtros.add(new Filtro("tipo", "V"));
        filtros.add(new Filtro("valorVenda", Filtro.MAIOR, BigDecimal.ZERO));
        List<Produto> produtos = produtoRepository.getEntitys(Produto.class, filtros, new Object[]{"nome", "valorVenda"});

        for (Produto p : produtos) {
            TabelaPrecoProduto precoProduto = new TabelaPrecoProduto();
            precoProduto.setTabelaPreco(getObjeto());
            precoProduto.setPreco(p.getValorVenda());
            precoProduto.setProduto(p);
            precoProduto.setPreco(p.getValorVenda());
            getObjeto().getListaProduto().add(precoProduto);
        }
    }

    public List<Produto> getListaProduto(String nome) {
        List<Produto> produtos = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("inativo", "N"));
            filtros.add(new Filtro("excluido", "N"));
            filtros.add(new Filtro("tipo", "V"));
            filtros.add(new Filtro("valorVenda", Filtro.MAIOR, BigDecimal.ZERO));
            produtos = produtoRepository.getEntitys(Produto.class, filtros, new Object[]{"nome", "valorVenda"});
        } catch (Exception ex) {

        }

        return produtos;

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

    public Map<String, String> getAplicado() {
        return aplicado;
    }

    public void setAplicado(Map<String, String> aplicado) {
        this.aplicado = aplicado;
    }
}
