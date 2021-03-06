package com.chronos.erp.controll.compras;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.jsf.Mensagem;

import javax.annotation.PostConstruct;
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

    private List<CompraTipoRequisicao> tipoRquisicoes;

    @PostConstruct
    @Override
    public void init() {
        super.init();

        tipoRquisicoes = new ArrayList<>();
        tipoRquisicoes.add(new CompraTipoRequisicao(1, "INTERNA"));
        tipoRquisicoes.add(new CompraTipoRequisicao(2, "EXTERNA"));
    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setDataRequisicao(new Date());
        getObjeto().setListaCompraRequisicaoDetalhe(new HashSet<>());
        getObjeto().setEmpresa(empresa);
    }

    @Override
    public void doEdit() {
        super.doEdit();

        CompraRequisicao req = dataModel.getRowData(getObjeto().getId().toString());
        setObjeto(req);

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
        setTelaGrid(false);
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

    }

    public void excluirItemRequisicao() {

        getObjeto().getListaCompraRequisicaoDetalhe().remove(detalheSelecionado);

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
            listaColaborador = colaboradores.getEntitys(Colaborador.class, "pessoa.nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaColaborador;
    }

    public List<Produto> getListaProduto(String nome) {
        List<Produto> listaProduto = new ArrayList<>();
        try {
            listaProduto = produtos.getEntitys(Produto.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaProduto;
    }

    @Override
    protected Class<CompraRequisicao> getClazz() {
        return CompraRequisicao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "COMPRA_REQUISICAO";
    }

    @Override
    protected boolean auditar() {
        return false;
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

    public List<CompraTipoRequisicao> getTipoRquisicoes() {
        return tipoRquisicoes;
    }

    public void setTipoRquisicoes(List<CompraTipoRequisicao> tipoRquisicoes) {
        this.tipoRquisicoes = tipoRquisicoes;
    }
}
