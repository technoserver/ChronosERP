package com.chronos.controll.estoque;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Colaborador;
import com.chronos.modelo.entidades.Produto;
import com.chronos.modelo.entidades.RequisicaoInternaCabecalho;
import com.chronos.modelo.entidades.RequisicaoInternaDetalhe;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by john on 02/08/17.
 */
@Named
@ViewScoped
public class RequisicaoInternaCabecalhoControll extends AbstractControll<RequisicaoInternaCabecalho> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<Colaborador> colaboradores;
    @Inject
    private Repository<Produto> produtos;

    private RequisicaoInternaDetalhe requisicaoInternaDetalhe;
    private RequisicaoInternaDetalhe requisicaoInternaDetalheSelecionado;

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setListaRequisicaoInternaDetalhe(new HashSet<>());
    }

    @Override
    public void doEdit() {

        super.doEdit(); //To change body of generated methods, choose Tools | Templates.
        RequisicaoInternaCabecalho r = getDataModel().getRowData(getObjetoSelecionado().getId().toString());
        setObjeto(r);
    }

    public void incluirRequisicaoInternaDetalhe() {
        requisicaoInternaDetalhe = new RequisicaoInternaDetalhe();
        requisicaoInternaDetalhe.setRequisicaoInternaCabecalho(getObjeto());
    }

    public void alterarRequisicaoInternaDetalhe() {
        requisicaoInternaDetalhe = requisicaoInternaDetalheSelecionado;
    }

    public void excluirRequisicaoInternaDetalhe() {

        getObjeto().getListaRequisicaoInternaDetalhe().remove(requisicaoInternaDetalheSelecionado);
        salvar("Registro excluído com sucesso!");

    }

    public void salvarRequisicaoInternaDetalhe() {
        if (requisicaoInternaDetalhe.getId() == null) {
            getObjeto().getListaRequisicaoInternaDetalhe().add(requisicaoInternaDetalhe);
        }
        salvar("Registro salvo com sucesso!");
    }

    public void deferirRequisicao() {
        alteraRequisição("D");
    }

    public void indeferirRequisicao() {
        alteraRequisição("I");
    }

    private void alteraRequisição(String situacao) {
        try {
            if (getObjeto().getListaRequisicaoInternaDetalhe().isEmpty()) {
                Mensagem.addInfoMessage("Não há itens na requisição.");
            } else {
                if (getObjeto().getSituacao() != null && getObjeto().getSituacao().equals("D")) {
                    Mensagem.addInfoMessage("Esta requisição já foi deferida.\nNenhuma alteração realizada.");
                } else if (getObjeto().getSituacao() != null && getObjeto().getSituacao().equals("I")) {
                    Mensagem.addInfoMessage("Esta requisição já foi indeferida.\nNenhuma alteração realizada.");
                } else {
                    getObjeto().setSituacao(situacao);
                    String msg = situacao.equals("D") ? "Requisição deferida!" : "Requisição indeferida!";
                    salvar(msg);
                }
            }
        } catch (Exception e) {
            Mensagem.addErrorMessage("Ocorreu um erro!", e);
        }
    }

    public List<Produto> getListaProduto(String nome) {
        List<Produto> listaProduto = new ArrayList<>();
        try {
            atributos = new Object[]{"nome"};
            listaProduto = produtos.getEntitys(Produto.class, "nome", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaProduto;
    }

    public List<Colaborador> getListaColaborador(String nome) {
        List<Colaborador> listaColaborador = new ArrayList<>();
        try {
            atributos = new Object[]{"pessoa.id","pessoa.nome"};
            listaColaborador = colaboradores.getEntitys(Colaborador.class, "pessoa.nome", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaColaborador;
    }


    @Override
    protected Class<RequisicaoInternaCabecalho> getClazz() {
        return RequisicaoInternaCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "REQUISICAO_INTERNA_CABECALHO";
    }

    public RequisicaoInternaDetalhe getRequisicaoInternaDetalhe() {
        return requisicaoInternaDetalhe;
    }

    public void setRequisicaoInternaDetalhe(RequisicaoInternaDetalhe requisicaoInternaDetalhe) {
        this.requisicaoInternaDetalhe = requisicaoInternaDetalhe;
    }

    public RequisicaoInternaDetalhe getRequisicaoInternaDetalheSelecionado() {
        return requisicaoInternaDetalheSelecionado;
    }

    public void setRequisicaoInternaDetalheSelecionado(RequisicaoInternaDetalhe requisicaoInternaDetalheSelecionado) {
        this.requisicaoInternaDetalheSelecionado = requisicaoInternaDetalheSelecionado;
    }
}
