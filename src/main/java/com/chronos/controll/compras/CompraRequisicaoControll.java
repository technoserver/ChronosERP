package com.chronos.controll.compras;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class CompraRequisicaoControll extends AbstractControll<CompraRequisicao> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<CompraTipoRequisicao> tipos;
    @Inject
    private Repository<Colaborador> colaboradores;
    @Inject
    private Repository<Produto> produtos;

    private CompraRequisicaoDetalhe detalheSelecionado;
    private CompraRequisicaoDetalhe compraRequisicaoDetalhe;

    private Boolean parametroCompraSugerida;
    @Inject
    private CompraSugeridaControll compraSugeridaController;
    private Set<CompraRequisicaoDetalhe> listaCompraSugerida;

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setDataRequisicao(new Date());
        getObjeto().setListaCompraRequisicaoDetalhe(new HashSet<>());
    }

    @Override
    public void salvar() {
        if (parametroCompraSugerida != null) {
            if (getObjeto().getId() == null) {
                for (CompraRequisicaoDetalhe d : listaCompraSugerida) {
                    d.setCompraRequisicao(getObjeto());
                }
                getObjeto().setListaCompraRequisicaoDetalhe(listaCompraSugerida);
                parametroCompraSugerida = null;
            }
        }
        super.salvar();
    }

    public void incluirItemRequisicao() {
        compraRequisicaoDetalhe = new CompraRequisicaoDetalhe();
        compraRequisicaoDetalhe.setCompraRequisicao(getObjeto());
        compraRequisicaoDetalhe.setItemCotado("N");
        compraRequisicaoDetalhe.setQuantidadeCotada(BigDecimal.ZERO);
    }

    public void alterarItemRequisicao() {
        compraRequisicaoDetalhe = detalheSelecionado;
    }

    public void salvarItemRequisicao() {
        if (compraRequisicaoDetalhe.getId() == null) {
            getObjeto().getListaCompraRequisicaoDetalhe().add(compraRequisicaoDetalhe);
        }
        salvar("Item salvo com sucesso!");
    }

    public void excluirItemRequisicao() {

        getObjeto().getListaCompraRequisicaoDetalhe().remove(detalheSelecionado);
        salvar("Item excluído com sucesso!");

    }

    public void carregaCompraSugerida() {
        try {
            if (parametroCompraSugerida != null) {
                listaCompraSugerida = compraSugeridaController.geraRequisicao();
                doCreate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Erro ao carregar a compra sugerida!", e);

        }
    }

    public List<CompraTipoRequisicao> getListaCompraTipoRequisicao(String nome) {
        List<CompraTipoRequisicao> listaCompraTipoRequisicao = new ArrayList<>();
        try {
            listaCompraTipoRequisicao = tipos.getEntitys(CompraTipoRequisicao.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaCompraTipoRequisicao;
    }

    public List<Colaborador> getListaColaborador(String nome) {
        List<Colaborador> listaColaborador = new ArrayList<>();
        try {
            listaColaborador = colaboradores.getEntitys(Colaborador.class,"pessoa.nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaColaborador;
    }

    public List<Produto> getListaProduto(String nome) {
        List<Produto> listaProduto = new ArrayList<>();
        try {
            listaProduto = produtos.getEntitys(Produto.class,"nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaProduto;
    }

    @Override
    protected Class<CompraRequisicao> getClazz() {
        return null;
    }

    @Override
    protected String getFuncaoBase() {
        return null;
    }

    public CompraRequisicaoDetalhe getDetalheSelecionado() {
        return detalheSelecionado;
    }

    public void setDetalheSelecionado(CompraRequisicaoDetalhe detalheSelecionado) {
        this.detalheSelecionado = detalheSelecionado;
    }

    public CompraRequisicaoDetalhe getCompraRequisicaoDetalhe() {
        return compraRequisicaoDetalhe;
    }

    public void setCompraRequisicaoDetalhe(CompraRequisicaoDetalhe compraRequisicaoDetalhe) {
        this.compraRequisicaoDetalhe = compraRequisicaoDetalhe;
    }

    public Boolean getParametroCompraSugerida() {
        return parametroCompraSugerida;
    }

    public void setParametroCompraSugerida(Boolean parametroCompraSugerida) {
        this.parametroCompraSugerida = parametroCompraSugerida;
    }

}
