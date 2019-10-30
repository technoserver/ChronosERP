package com.chronos.erp.controll.tributacao;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.TributIss;
import com.chronos.erp.modelo.entidades.TributOperacaoFiscal;
import com.chronos.erp.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class TributIssControll extends AbstractControll<TributIss> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<TributOperacaoFiscal> operacoes;


    public List<TributOperacaoFiscal> getListaTributOperacaoFiscal(String nome) {
        List<TributOperacaoFiscal> listaTributOperacaoFiscal = new ArrayList<>();
        try {

            listaTributOperacaoFiscal = operacoes.getEntitys(TributOperacaoFiscal.class, "descricao", nome, new Object[]{"descricao"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTributOperacaoFiscal;
    }

    @Override
    public void doCreate() {
        super.doCreate();

        getObjeto().setAliquotaPorcento(BigDecimal.ZERO);
        getObjeto().setAliquotaUnidade(BigDecimal.ZERO);
        getObjeto().setItemListaServico(0);
        getObjeto().setModalidadeBaseCalculo('0');
        getObjeto().setPorcentoBaseCalculo(BigDecimal.ZERO);
        getObjeto().setValorPautaFiscal(BigDecimal.ZERO);
        getObjeto().setValorPrecoMaximo(BigDecimal.ZERO);
    }

    @Override
    protected Class<TributIss> getClazz() {
        return TributIss.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "TRIBUT_ISS";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
