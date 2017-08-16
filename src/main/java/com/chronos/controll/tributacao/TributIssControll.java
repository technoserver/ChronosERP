package com.chronos.controll.tributacao;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.TributIss;
import com.chronos.modelo.entidades.TributOperacaoFiscal;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class TributIssControll extends AbstractControll<TributIss> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Repository<TributOperacaoFiscal> operacoes;


    public List<TributOperacaoFiscal> getListaTributOperacaoFiscal(String nome) {
        List<TributOperacaoFiscal> listaTributOperacaoFiscal = new ArrayList<>();
        try {
            listaTributOperacaoFiscal = operacoes.getEntitys(TributOperacaoFiscal.class,"descricao", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTributOperacaoFiscal;
    }

    @Override
    protected Class<TributIss> getClazz() {
        return TributIss.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "TRIBUT_ISS";
    }
}
