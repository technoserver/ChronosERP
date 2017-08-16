package com.chronos.controll.compras;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.SelectEvent;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class CompraCotacaoControll extends AbstractControll<CompraCotacao> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Repository<Fornecedor> fornecedores;
    private Repository<CompraRequisicaoDetalhe> requisicoes;
    private Repository<CompraCotacaoDetalhe> cotacoes;


    private CompraFornecedorCotacao compraFornecedorCotacao;
    private CompraCotacaoDetalhe compraCotacaoDetalhe;
    private Set<CompraCotacaoDetalhe> listaCompraCotacaoDetalhe;
    private CompraRequisicaoDetalhe compraRequisicaoDetalhe;


    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setListaCompraFornecedorCotacao(new HashSet<>());
        getObjeto().setListaCompraReqCotacaoDetalhe(new HashSet<>());
        getObjeto().setSituacao("A");
        listaCompraCotacaoDetalhe = new HashSet<>();
    }

    @Override
    public void doEdit() {
        super.doEdit();

        try {
            CompraFornecedorCotacao fornecedorCotacao = null;
            for (CompraFornecedorCotacao f : getObjeto().getListaCompraFornecedorCotacao()) {
                fornecedorCotacao = f;
            }

            listaCompraCotacaoDetalhe = new HashSet<>(cotacoes.getEntitys(CompraCotacaoDetalhe.class ,"compraFornecedorCotacao", fornecedorCotacao));
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Erro ao buscar os itens da cotação", e);

        }
    }

    @Override
    public void salvar() {
        try {
            if (getObjeto().getId() != null) {
                throw new Exception("Não é possível alterar uma cotação!");
            }

            if (getObjeto().getListaCompraFornecedorCotacao().isEmpty()) {
                throw new Exception("É necessário incluir um fornecedor para cotação!");
            }

            if (listaCompraCotacaoDetalhe.isEmpty()) {
                throw new Exception("É necessário incluir itens para cotação!");
            }

            for (CompraCotacaoDetalhe d : listaCompraCotacaoDetalhe) {
                CompraReqCotacaoDetalhe compraReqCotacaoDetalhe = new CompraReqCotacaoDetalhe();
                compraReqCotacaoDetalhe.setCompraCotacao(getObjeto());
                compraReqCotacaoDetalhe.setCompraRequisicaoDetalhe(d.getCompraRequisicaoDetalhe());
                compraReqCotacaoDetalhe.setQuantidadeCotada(d.getQuantidade());

                getObjeto().getListaCompraReqCotacaoDetalhe().add(compraReqCotacaoDetalhe);
            }

            for (CompraFornecedorCotacao f : getObjeto().getListaCompraFornecedorCotacao()) {
                HashSet<CompraCotacaoDetalhe> listaDetalhe = new HashSet<>();

                for (CompraCotacaoDetalhe d : listaCompraCotacaoDetalhe) {
                    CompraCotacaoDetalhe ccd = new CompraCotacaoDetalhe();
                    ccd.setCompraRequisicaoDetalhe(d.getCompraRequisicaoDetalhe());
                    ccd.setCompraFornecedorCotacao(f);
                    ccd.setProduto(d.getProduto());
                    ccd.setQuantidade(d.getQuantidade());

                    listaDetalhe.add(ccd);
                }

                f.setListaCompraCotacaoDetalhe(listaDetalhe);
            }

            super.salvar();
        } catch (Exception e) {
            Mensagem.addErrorMessage("",e);

        }
    }

    public void incluirFornecedor() {
        compraFornecedorCotacao = new CompraFornecedorCotacao();
        compraFornecedorCotacao.setCompraCotacao(getObjeto());
    }

    public void salvarFornecedor() {
        getObjeto().getListaCompraFornecedorCotacao().add(compraFornecedorCotacao);
        Mensagem.addInfoMessage("Fornecedor incluído com sucesso!");

    }

    public void incluirItem() {
        compraCotacaoDetalhe = new CompraCotacaoDetalhe();
        compraRequisicaoDetalhe = new CompraRequisicaoDetalhe();
    }

    public void salvarItem() {
        try {
            if ((compraRequisicaoDetalhe.getQuantidade().subtract(compraRequisicaoDetalhe.getQuantidadeCotada())).compareTo(compraCotacaoDetalhe.getQuantidade()) == -1) {
                Mensagem.addInfoMessage( "A quantidade informada excede a quantidade disponível para cotação!");

            } else {
                compraCotacaoDetalhe.setProduto(compraRequisicaoDetalhe.getProduto());
                listaCompraCotacaoDetalhe.add(compraCotacaoDetalhe);

                compraRequisicaoDetalhe.setQuantidadeCotada(compraRequisicaoDetalhe.getQuantidadeCotada().add(compraCotacaoDetalhe.getQuantidade()));
                if (compraRequisicaoDetalhe.getQuantidade().compareTo(compraRequisicaoDetalhe.getQuantidadeCotada()) == 0) {
                    compraRequisicaoDetalhe.setItemCotado("S");
                }

                requisicoes.atualizar(compraRequisicaoDetalhe);

                compraCotacaoDetalhe.setCompraRequisicaoDetalhe(compraRequisicaoDetalhe);
                Mensagem.addInfoMessage( "Item incluído com sucesso!");

            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro na inclusão do item",e);

        }
    }

    public void onSelecionaProduto(SelectEvent event) {
        CompraRequisicaoDetalhe requisicao = (CompraRequisicaoDetalhe) event.getObject();
        compraCotacaoDetalhe.setQuantidade(requisicao.getQuantidade());
    }

    public List<Fornecedor> getListaFornecedor(String nome) {
        List<Fornecedor> listaFornecedor = new ArrayList<>();
        try {
            listaFornecedor = fornecedores.getEntitys(Fornecedor.class, "pessoa.nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaFornecedor;
    }

    public List<CompraRequisicaoDetalhe> getListaItensRequisicao(String nome) {
        List<CompraRequisicaoDetalhe> listaCompraRequisicaoDetalhe = new ArrayList<>();
        try {
            listaCompraRequisicaoDetalhe = requisicoes.getEntitys(CompraRequisicaoDetalhe.class,"itemCotado", "N");
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaCompraRequisicaoDetalhe;
    }


    @Override
    protected Class<CompraCotacao> getClazz() {
        return CompraCotacao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "COMPRA_COTACAO";
    }

    public CompraFornecedorCotacao getCompraFornecedorCotacao() {
        return compraFornecedorCotacao;
    }

    public void setCompraFornecedorCotacao(CompraFornecedorCotacao compraFornecedorCotacao) {
        this.compraFornecedorCotacao = compraFornecedorCotacao;
    }

    public Set<CompraCotacaoDetalhe> getListaCompraCotacaoDetalhe() {
        return listaCompraCotacaoDetalhe;
    }

    public void setListaCompraCotacaoDetalhe(Set<CompraCotacaoDetalhe> listaCompraCotacaoDetalhe) {
        this.listaCompraCotacaoDetalhe = listaCompraCotacaoDetalhe;
    }

    public CompraCotacaoDetalhe getCompraCotacaoDetalhe() {
        return compraCotacaoDetalhe;
    }

    public void setCompraCotacaoDetalhe(CompraCotacaoDetalhe compraCotacaoDetalhe) {
        this.compraCotacaoDetalhe = compraCotacaoDetalhe;
    }

    public CompraRequisicaoDetalhe getCompraRequisicaoDetalhe() {
        return compraRequisicaoDetalhe;
    }

    public void setCompraRequisicaoDetalhe(CompraRequisicaoDetalhe compraRequisicaoDetalhe) {
        this.compraRequisicaoDetalhe = compraRequisicaoDetalhe;
    }
}
