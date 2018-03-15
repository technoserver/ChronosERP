package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.FinLancamentoReceberCartao;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 15/03/18.
 */
@Named
@ViewScoped
public class FinLancamentoReceberCartaoControll extends AbstractControll<FinLancamentoReceberCartao> implements Serializable {

    private static final long serialVersionUID = 1L;


    @Override
    public ERPLazyDataModel<FinLancamentoReceberCartao> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(FinLancamentoReceberCartao.class);
            dataModel.setDao(dao);
        }

        return dataModel;
    }

    @Override
    protected Class<FinLancamentoReceberCartao> getClazz() {
        return FinLancamentoReceberCartao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "LANCAMENTO_RECEBER_CARTAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
