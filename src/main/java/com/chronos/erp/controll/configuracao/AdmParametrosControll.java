package com.chronos.erp.controll.configuracao;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.modelo.entidades.AdmParametro;
import com.chronos.erp.modelo.entidades.SituacaoForCli;
import com.chronos.erp.modelo.entidades.TributOperacaoFiscal;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.jsf.FacesUtil;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by john on 04/12/17.
 */
@Named
@ViewScoped
public class AdmParametrosControll extends AbstractControll<AdmParametro> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<TributOperacaoFiscal> operacaoFiscalRepository;

    @Inject
    private Repository<SituacaoForCli> situacaoForCliRepository;

    private TributOperacaoFiscal operacaoFiscal;
    private SituacaoForCli situacaoForCli;
    private List<TributOperacaoFiscal> operacoesFiscais;
    private List<SituacaoForCli> situacoesCliente;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        setTelaGrid(false);

        AdmParametro parametro = dao.get(AdmParametro.class, "empresa.id", empresa.getId());
        parametro = parametro == null ? new AdmParametro() : parametro;
        setObjeto(parametro);
        operacoesFiscais = operacaoFiscalRepository.getEntitys(TributOperacaoFiscal.class, new Object[]{"descricao", "descricaoNaNf", "cfop", "obrigacaoFiscal", "destacaIpi", "destacaPisCofins", "calculoIssqn", "classificacaoContabilConta"});
        situacoesCliente = situacaoForCliRepository.getEntitys(SituacaoForCli.class, "bloquear", "S");


        situacaoForCli = new SituacaoForCli(getObjeto().getSituacaoClienteBloqueado());
        operacaoFiscal = new TributOperacaoFiscal(getObjeto().getTributOperacaoFiscalPadrao());
    }

    @Override
    public ERPLazyDataModel<AdmParametro> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(AdmParametro.class);
            dataModel.setDao(dao);
        }

        dataModel.getFiltros().clear();
        dataModel.getFiltros().add(new Filtro("empresa.id", empresa.getId()));
        return dataModel;
    }

    @Override
    public void salvar() {
        getObjeto().setTributOperacaoFiscalPadrao(operacaoFiscal.getId());
        getObjeto().setOperacaoFiscal(operacaoFiscal);
        FacesUtil.setParamtro(getObjeto());
        super.salvar();
    }

    @Override
    protected Class<AdmParametro> getClazz() {
        return AdmParametro.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PARAMETROS";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public TributOperacaoFiscal getOperacaoFiscal() {
        return operacaoFiscal;
    }

    public void setOperacaoFiscal(TributOperacaoFiscal operacaoFiscal) {
        this.operacaoFiscal = operacaoFiscal;
    }

    public List<TributOperacaoFiscal> getOperacoesFiscais() {
        return operacoesFiscais;
    }

    public void setOperacoesFiscais(List<TributOperacaoFiscal> operacoesFiscais) {
        this.operacoesFiscais = operacoesFiscais;
    }

    public List<SituacaoForCli> getSituacoesCliente() {
        return situacoesCliente;
    }

    public SituacaoForCli getSituacaoForCli() {
        return situacaoForCli;
    }

    public void setSituacaoForCli(SituacaoForCli situacaoForCli) {
        this.situacaoForCli = situacaoForCli;
    }
}
