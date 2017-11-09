package com.chronos.controll.tributacao;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.TributCofinsCodApuracao;
import com.chronos.modelo.entidades.TributIpiDipi;
import com.chronos.modelo.entidades.TributOperacaoFiscal;
import com.chronos.modelo.entidades.TributPisCodApuracao;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 08/11/17.
 */
@Named
@ViewScoped
public class TributPisCofinsControll extends AbstractControll<TributOperacaoFiscal> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<TributPisCodApuracao> pisRepository;
    @Inject
    private Repository<TributCofinsCodApuracao> cofinsRepository;

    private TributPisCodApuracao pis;
    private TributCofinsCodApuracao cofins;
    private TributIpiDipi ipi;


    @Override
    public void doEdit() {
        super.doEdit();


    }


    private void instanciaImpostos() {

    }

    @Override
    protected Class<TributOperacaoFiscal> getClazz() {
        return TributOperacaoFiscal.class;
    }


    @Override
    protected String getFuncaoBase() {
        return "PIS_COFINS";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public TributCofinsCodApuracao getCofins() {
        return cofins;
    }

    public void setCofins(TributCofinsCodApuracao cofins) {
        this.cofins = cofins;
    }

    public TributPisCodApuracao getPis() {
        return pis;
    }

    public void setPis(TributPisCodApuracao pis) {
        this.pis = pis;
    }
}
