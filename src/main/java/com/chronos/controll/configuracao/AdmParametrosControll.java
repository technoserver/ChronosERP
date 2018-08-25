package com.chronos.controll.configuracao;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.AdmParametro;
import com.chronos.modelo.entidades.TributOperacaoFiscal;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.FacesUtil;

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

    private TributOperacaoFiscal operacaoFiscal;
    private List<TributOperacaoFiscal> operacoesFiscais;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        setTelaGrid(false);

        AdmParametro parametro = dao.get(AdmParametro.class, "empresa.id", empresa.getId());
        parametro = parametro == null ? new AdmParametro() : parametro;
        setObjeto(parametro);
        operacoesFiscais = operacaoFiscalRepository.getEntitys(TributOperacaoFiscal.class, new Object[]{"descricao", "cfop", "obrigacaoFiscal", "destacaIpi", "destacaPisCofins", "calculoIssqn"});

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
}
