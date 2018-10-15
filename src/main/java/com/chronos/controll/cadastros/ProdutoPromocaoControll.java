package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.Produto;
import com.chronos.modelo.entidades.ProdutoPromocao;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.comercial.ProdutoPromocaoService;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class ProdutoPromocaoControll extends AbstractControll<ProdutoPromocao> implements Serializable {

    private static final long serialVersionUID = 1L;


    @Inject
    private ProdutoPromocaoService service;
    @Inject
    private Repository<Produto> produtoRepository;


    @Override
    public ERPLazyDataModel<ProdutoPromocao> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(ProdutoPromocao.class);
            dataModel.setDao(dao);
        }
        dataModel.setAtributos(new Object[]{"quantidadeEmPromocao", "valor", "produto.nome"});
        return dataModel;
    }

    @Override
    public void salvar() {
        try {
            setObjeto(service.salvar(getObjeto()));
            Mensagem.addInfoMessage("Registro salvo com sucesso");
        } catch (Exception ex) {
            throw new RuntimeException("erro a salvar prduto promocao", ex);
        }
    }

    @Override
    public void doEdit() {
        super.doEdit();
        ProdutoPromocao produtoPromocao = dataModel.getRowData(getObjetoSelecionado().getId().toString());
        setObjeto(produtoPromocao);
        setTelaGrid(false);
    }

    public List<Produto> getListaProduto(String nome) {
        List<Produto> produtos = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("inativo", "N"));
            filtros.add(new Filtro("excluido", "N"));
            filtros.add(new Filtro("tipo", "V"));
            filtros.add(new Filtro("nome", Filtro.LIKE, nome));
            filtros.add(new Filtro("valorVenda", Filtro.MAIOR, BigDecimal.ZERO));
            produtos = produtoRepository.getEntitys(Produto.class, filtros, new Object[]{"nome",});
        } catch (Exception ex) {

        }

        return produtos;
    }

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
