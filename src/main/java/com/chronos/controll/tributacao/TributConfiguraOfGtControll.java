package com.chronos.controll.tributacao;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class TributConfiguraOfGtControll extends AbstractControll<TributConfiguraOfGt> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<TributGrupoTributario> grupos;
    @Inject
    private Repository<TributOperacaoFiscal> operacoes;
    @Inject
    private Repository<TipoReceitaDipi> ipis;

    private TributIcmsUf tributIcmsUf;
    private TributIcmsUf tributIcmsUfSelecionado;

    @Override
    public void doCreate() {
        super.doCreate();

        TributPisCodApuracao pis = new TributPisCodApuracao();
        pis.setTributConfiguraOfGt(getObjeto());
        getObjeto().setTributPisCodApuracao(pis);

        TributCofinsCodApuracao cofins = new TributCofinsCodApuracao();
        cofins.setTributConfiguraOfGt(getObjeto());
        getObjeto().setTributCofinsCodApuracao(cofins);

        TributIpiDipi ipi = new TributIpiDipi();
        ipi.setTributConfiguraOfGt(getObjeto());
        getObjeto().setTributIpiDipi(ipi);

        getObjeto().setListaTributIcmsUf(new HashSet<>());
    }

    public void incluirTributIcmsUf() {
        tributIcmsUf = new TributIcmsUf();
        tributIcmsUf.setTributConfiguraOfGt(getObjeto());
    }

    public void alterarTributIcmsUf() {
        tributIcmsUf = tributIcmsUfSelecionado;
    }

    public void salvarTributIcmsUf() {
        if (tributIcmsUf.getId() == null) {
            getObjeto().getListaTributIcmsUf().add(tributIcmsUf);
        }
        salvar("Registro salvo com sucesso!");
    }

    public void excluirTributIcmsUf() {

        getObjeto().getListaTributIcmsUf().remove(tributIcmsUfSelecionado);
        salvar("Registro excluído com sucesso!");

    }

    public List<TributGrupoTributario> getListaTributGrupoTributario(String nome) {
        List<TributGrupoTributario> listaTributGrupoTributario = new ArrayList<>();
        try {
            listaTributGrupoTributario = grupos.getEntitys(TributGrupoTributario.class, "descricao", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTributGrupoTributario;
    }

    public List<TributOperacaoFiscal> getListaTributOperacaoFiscal(String nome) {
        List<TributOperacaoFiscal> listaTributOperacaoFiscal = new ArrayList<>();
        try {
            listaTributOperacaoFiscal = operacoes.getEntitys(TributOperacaoFiscal.class, "descricao", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTributOperacaoFiscal;
    }

    public List<TipoReceitaDipi> getListaTipoReceitaDipi(String nome) {
        List<TipoReceitaDipi> listaTipoReceitaDipi = new ArrayList<>();
        try {
            listaTipoReceitaDipi = ipis.getEntitys(TipoReceitaDipi.class, "descricao", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTipoReceitaDipi;
    }


    @Override
    protected Class<TributConfiguraOfGt> getClazz() {
        return TributConfiguraOfGt.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "TRIBUT_CONFIGURA_OF_GT";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public TributIcmsUf getTributIcmsUf() {
        return tributIcmsUf;
    }

    public void setTributIcmsUf(TributIcmsUf tributIcmsUf) {
        this.tributIcmsUf = tributIcmsUf;
    }

    public TributIcmsUf getTributIcmsUfSelecionado() {
        return tributIcmsUfSelecionado;
    }

    public void setTributIcmsUfSelecionado(TributIcmsUf tributIcmsUfSelecionado) {
        this.tributIcmsUfSelecionado = tributIcmsUfSelecionado;
    }

}
