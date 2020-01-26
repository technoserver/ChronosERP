package com.chronos.erp.controll.vendas;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.EmpresaProduto;
import com.chronos.erp.modelo.entidades.Produto;
import com.chronos.erp.modelo.entidades.TabelaPreco;
import com.chronos.erp.modelo.entidades.TabelaPrecoProduto;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.comercial.TabelaPrecoService;
import com.chronos.erp.util.jsf.Mensagem;

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
import java.util.stream.Collectors;

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
    @Inject
    private Repository<EmpresaProduto> empresaProdutoRepository;

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
        filtros.add(new Filtro("produto.inativo", "N"));
        filtros.add(new Filtro("produto.excluido", "N"));
        filtros.add(new Filtro("produto.tipo", "V"));
        filtros.add(new Filtro("empresa.id", empresa.getId()));
        filtros.add(new Filtro("produto.valorVenda", Filtro.MAIOR, BigDecimal.ZERO));
        List<EmpresaProduto> produtos = empresaProdutoRepository.getEntitys(EmpresaProduto.class, filtros, new Object[]{"produto.id,produto.nome", "produto.valorVenda"});

        for (EmpresaProduto p : produtos) {
            TabelaPrecoProduto precoProduto = new TabelaPrecoProduto();
            precoProduto.setTabelaPreco(getObjeto());
            precoProduto.setPreco(p.getProduto().getValorVenda());
            precoProduto.setProduto(p.getProduto());
            getObjeto().getListaProduto().add(precoProduto);
        }
    }

    public List<Produto> getListaProduto(String nome) {
        List<Produto> produtos = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("produto.inativo", "N"));
            filtros.add(new Filtro("produto.excluido", "N"));
            filtros.add(new Filtro("produto.tipo", "V"));
            filtros.add(new Filtro("produto.nome", Filtro.LIKE, nome));
            filtros.add(new Filtro("empresa.id", empresa.getId()));
            filtros.add(new Filtro("produto.valorVenda", Filtro.MAIOR, BigDecimal.ZERO));
            List<EmpresaProduto> empresaProdutos = empresaProdutoRepository.getEntitys(EmpresaProduto.class, filtros, new Object[]{"produto.id,produto.nome", "produto.valorVenda"});

            produtos = empresaProdutos
                    .stream()
                    .map(ep -> new Produto(ep.getId(), ep.getProduto().getNome(), ep.getProduto().getValorVenda())).collect(Collectors.toList());

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
