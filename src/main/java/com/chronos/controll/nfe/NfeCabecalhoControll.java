package com.chronos.controll.nfe;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.NfeCabecalho;
import com.chronos.modelo.entidades.TributOperacaoFiscal;
import com.chronos.modelo.entidades.view.PessoaCliente;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.comercial.vendas.nfe.NfeService;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 26/09/17.
 */
@Named
@ViewScoped
public class NfeCabecalhoControll extends AbstractControll<NfeCabecalho> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<TributOperacaoFiscal> operacoes;
    @Inject
    private Repository<PessoaCliente> pessoas;


    @Inject
    private NfeService nfeService;

    private PessoaCliente pessoaCliente;
    private String observacao;
    private boolean duplicidade;


    @Override
    public ERPLazyDataModel<NfeCabecalho> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(dao);
            dataModel.setClazz(NfeCabecalho.class);
        }
        dataModel.setAtributos(new Object[]{"cliente", "serie", "numero", "dataHoraEmissao", "chaveAcesso", "digitoChaveAcesso", "valorTotal", "statusNota", "codigoModelo"});
        dataModel.getFiltros().clear();
        dataModel.addFiltro("tipoOperacao", 1, Filtro.IGUAL);
        dataModel.addFiltro("empresa.id", empresa.getId(), Filtro.IGUAL);
        dataModel.addFiltro("codigoModelo", "55", Filtro.IGUAL);
        return dataModel;
    }


    // <editor-fold defaultstate="collapsed" desc="Procedimentos Diversos">


    public void gerarNumeracao() {
        try {
            getObjeto().setNumero(null);
            gerarNumeracao(getObjeto());
        } catch (Exception ex) {
            Mensagem.addErrorMessage("", ex);
            throw new RuntimeException("Erro ao gera um novo número da NFe", ex);

        }
    }

    public void gerarNumeracao(NfeCabecalho nfe) throws Exception {
        nfeService.gerarNumeracao(nfe);
        duplicidade = false;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Pesquisas">

    public List<TributOperacaoFiscal> getListaTributOperacaoFiscal(String descricao) {
        List<TributOperacaoFiscal> listaTributOperacaoFiscal = new ArrayList<>();

        try {
            listaTributOperacaoFiscal = operacoes.getEntitys(TributOperacaoFiscal.class, "descricao", descricao, new Object[]{"descricao"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTributOperacaoFiscal;
    }

    public List<PessoaCliente> getListaPessoaCliente(String nome) {
        List<PessoaCliente> listaPessoaCliente = new ArrayList<>();
        try {
            listaPessoaCliente = pessoas.getEntitys(PessoaCliente.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaPessoaCliente;
    }

    // </editor-fold>

    @Override
    protected Class<NfeCabecalho> getClazz() {
        return NfeCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "NFE_CABECALHO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }


    // <editor-fold defaultstate="collapsed" desc="GETS SETS">


    public PessoaCliente getPessoaCliente() {
        return pessoaCliente;
    }

    public void setPessoaCliente(PessoaCliente pessoaCliente) {
        this.pessoaCliente = pessoaCliente;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public boolean isDuplicidade() {
        return duplicidade;
    }

    public void setDuplicidade(boolean duplicidade) {
        this.duplicidade = duplicidade;
    }

    // </editor-fold>


}
