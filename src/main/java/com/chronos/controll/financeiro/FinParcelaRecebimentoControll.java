package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by john on 15/08/17.
 */
@Named
@ViewScoped
public class FinParcelaRecebimentoControll extends AbstractControll<FinParcelaReceber> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Repository<FinParcelaReceber> finParcelaReceberDao;
    private Repository<FinTipoRecebimento> finTipoRecebimentoDao;
    private Repository<FinChequeRecebido> finChequeRecebidoDao;
    private Repository<ContaCaixa> contaCaixaDao;
    private Repository<Cheque> chequeDao;
    private Repository<FinStatusParcela> finStatusParcelaDao;
    private Repository<Cliente> clienteDao;
    private Repository<AdmParametro> admParametroDao;

    private FinParcelaRecebimento finParcelaRecebimento;
    private FinParcelaRecebimento finParcelaRecebimentoSelecionado;

    private FinChequeRecebido finChequeRecebido;

    private String strTipoBaixa;
    private List<FinParcelaReceber> parcelasSelecionadas;
    private boolean recebimentoCheque;
    private String historico;
    private Cliente cliente;

    @Override
    protected Class<FinParcelaReceber> getClazz() {
        return FinParcelaReceber.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "FIN_PARCELA_RECEBIMENTO";
    }
}
