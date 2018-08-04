package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.FinParcelaReceberCartao;
import com.chronos.modelo.entidades.FinParcelaRecebimentoCartao;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class FinRecebimentoCartaoControll extends AbstractControll<FinParcelaReceberCartao> implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<FinParcelaRecebimentoCartao> recebimentoCartaoRepository;


    @Override
    protected Class<FinParcelaReceberCartao> getClazz() {
        return FinParcelaReceberCartao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "RECEBIMENTO_CARTAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
