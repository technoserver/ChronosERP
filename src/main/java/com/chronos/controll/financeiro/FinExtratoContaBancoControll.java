package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 14/08/17.
 */
@Named
@ViewScoped
public class FinExtratoContaBancoControll extends AbstractControll<ContaCaixa> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<FinExtratoContaBanco> extratos;
    @Inject
    private Repository<FinParcelaPagamento> pagamentos;
    @Inject
    private Repository<FinParcelaRecebimento> recebimentos;
    @Inject
    private Repository<FinChequeEmitido> cheques;

    private List<FinExtratoContaBanco> extratoContaBanco;
    private Date periodo;

    @Override
    public void voltar() {
        super.voltar();
        extratoContaBanco = new ArrayList<>();
        periodo = null;
    }

    public void buscaDados() {
        try {
            if (mesAno() == null) {
                Mensagem.addInfoMessage("Necessário informar um período válido!");

            } else {
                List<Filtro> filtros = new ArrayList<>();
                filtros.add(new Filtro(Filtro.AND, "contaCaixa", Filtro.IGUAL, getObjeto()));
                filtros.add(new Filtro(Filtro.AND, "mesAno", Filtro.IGUAL, mesAno()));

                extratoContaBanco = extratos.getEntitys(FinExtratoContaBanco.class, filtros);
                if (extratoContaBanco == null || extratoContaBanco.isEmpty()) {
                    Mensagem.addInfoMessage("Nenhum registro encontrado para o período informado.");

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Erro ao buscar os dados!",e);

        }
    }

    public String getTotais() {
        if (extratoContaBanco != null) {
            BigDecimal creditos = BigDecimal.ZERO;
            BigDecimal debitos = BigDecimal.ZERO;
            BigDecimal saldo = BigDecimal.ZERO;
            for (FinExtratoContaBanco e : extratoContaBanco) {
                if (e.getValor().compareTo(BigDecimal.ZERO) == -1) {
                    debitos = debitos.add(e.getValor());
                } else {
                    creditos = creditos.add(e.getValor());
                }
            }
            saldo = creditos.add(debitos);

            DecimalFormat decimalFormat = new DecimalFormat("#,###,##0.00");
            String texto = "|   Créditos: R$ " + decimalFormat.format(creditos);
            texto += "|   Débitos: R$ " + decimalFormat.format(debitos);
            texto += "|   Saldo: R$ " + decimalFormat.format(saldo) + "   |";
            return texto;
        }
        return "";
    }

    private String mesAno() {
        try {
            if (periodo == null) {
                return null;
            }
            Calendar dataValida = Calendar.getInstance();
            dataValida.setLenient(false);
            dataValida.setTime(periodo);

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
            return dateFormat.format(dataValida.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected Class<ContaCaixa> getClazz() {
        return ContaCaixa.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "EXTRATO_CONTA_BANCO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public List<FinExtratoContaBanco> getExtratoContaBanco() {
        return extratoContaBanco;
    }

    public void setExtratoContaBanco(List<FinExtratoContaBanco> extratoContaBanco) {
        this.extratoContaBanco = extratoContaBanco;
    }

    public Date getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Date periodo) {
        this.periodo = periodo;
    }
}
