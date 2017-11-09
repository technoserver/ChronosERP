package com.chronos.controll.tributacao;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.TributOperacaoFiscal;
import com.chronos.modelo.entidades.TributPisCodApuracao;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 08/11/17.
 */
@Named
@ViewScoped
public class TributPisControll extends AbstractControll<TributPisCodApuracao> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<TributOperacaoFiscal> operacoes;

    @Override
    public void doCreate() {
        super.doCreate();

        getObjeto().setAliquotaUnidade(BigDecimal.ZERO);
        getObjeto().setEfdTabela435("01");
        getObjeto().setModalidadeBaseCalculo("1");
        getObjeto().setPorcentoBaseCalculo(BigDecimal.ZERO);
        getObjeto().setValorPautaFiscal(BigDecimal.ZERO);
        getObjeto().setValorPrecoMaximo(BigDecimal.ZERO);
        getObjeto().setAliquotaPorcento(BigDecimal.ZERO);
    }

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
    protected Class<TributPisCodApuracao> getClazz() {
        return TributPisCodApuracao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PIS";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
