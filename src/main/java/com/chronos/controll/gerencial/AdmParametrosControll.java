package com.chronos.controll.gerencial;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.AdmParametro;
import com.chronos.modelo.entidades.TributOperacaoFiscal;
import com.chronos.repository.Repository;

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
        operacoesFiscais = operacaoFiscalRepository.getEntitys(TributOperacaoFiscal.class, new Object[]{"descricao"});

        operacaoFiscal = new TributOperacaoFiscal(getObjeto().getTributOperacaoFiscalPadrao());
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
