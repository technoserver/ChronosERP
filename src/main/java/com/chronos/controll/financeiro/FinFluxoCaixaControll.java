package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.view.ViewFinFluxoCaixaID;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 14/08/17.
 */
@Named
@ViewScoped
public class FinFluxoCaixaControll extends AbstractControll<ViewFinFluxoCaixaID> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<ViewFinFluxoCaixaID> fluxos;

    private Date periodo;
    private List<ViewFinFluxoCaixaID> listaFluxoCaixa;
    private List<ViewFinFluxoCaixaID> listaFluxoCaixaDetalhe;



    @Override
    public void doEdit() {
        super.doEdit();
        buscaDados();
    }

    public void buscaDados() {
        try {
            if (periodo == null) {
                Mensagem.addInfoMessage("Necessário informar o período!");

            } else {
                List<Filtro> filtros = new ArrayList<>();
                filtros.add(new Filtro(Filtro.AND, "viewFinFluxoCaixa.dataVencimento", Filtro.MAIOR_OU_IGUAL, getDataInicial()));
                filtros.add(new Filtro(Filtro.AND, "viewFinFluxoCaixa.dataVencimento", Filtro.MENOR_OU_IGUAL, ultimoDiaMes()));

                if (isTelaGrid()) {
                    listaFluxoCaixa = fluxos.getEntitys(ViewFinFluxoCaixaID.class, filtros);
                } else {
                    filtros.add(new Filtro(Filtro.AND, "viewFinFluxoCaixa.idContaCaixa", Filtro.IGUAL, getObjeto().getViewFinFluxoCaixa().getIdContaCaixa()));
                    listaFluxoCaixaDetalhe = fluxos.getEntitys(ViewFinFluxoCaixaID.class,filtros);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage( "Erro ao buscar os dados!",e);

        }
    }

    private Date getDataInicial() {
        try {
            if (periodo == null) {
                return null;
            }
            Calendar dataValida = Calendar.getInstance();
            dataValida.setTime(periodo);
            dataValida.setLenient(false);

            dataValida.set(Calendar.DAY_OF_MONTH, 1);

            dataValida.getTime();

            return dataValida.getTime();
        } catch (Exception e) {
            return null;
        }
    }

    private Date ultimoDiaMes() {
        Calendar dataF = Calendar.getInstance();
        dataF.setTime(periodo);
        dataF.setLenient(false);
        dataF.set(Calendar.DAY_OF_MONTH, dataF.getActualMaximum(Calendar.DAY_OF_MONTH));

        return dataF.getTime();
    }

    public String getTotais() {
        BigDecimal aPagar = BigDecimal.ZERO;
        BigDecimal aReceber = BigDecimal.ZERO;
        BigDecimal saldo = BigDecimal.ZERO;

        for (ViewFinFluxoCaixaID f : listaFluxoCaixaDetalhe) {
            if (f.getViewFinFluxoCaixa().getOperacao().equals("E")) {
                aReceber = aReceber.add(f.getViewFinFluxoCaixa().getValor());
            } else {
                aPagar = aPagar.add(f.getViewFinFluxoCaixa().getValor());
            }
        }
        saldo = aReceber.subtract(aPagar);

        DecimalFormat decimalFormat = new DecimalFormat("#,###,##0.00");
        String texto = "|   A Receber: R$ " + decimalFormat.format(aReceber);
        texto += "|   A Pagar: R$ " + decimalFormat.format(aPagar);
        texto += "|   Saldo: R$ " + decimalFormat.format(saldo) + "   |";
        return texto;
    }

    @Override
    protected Class<ViewFinFluxoCaixaID> getClazz() {
        return ViewFinFluxoCaixaID.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "FIN_FLUXO_CAIXA";
    }

    public Date getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Date periodo) {
        this.periodo = periodo;
    }

    public List<ViewFinFluxoCaixaID> getListaFluxoCaixa() {
        return listaFluxoCaixa;
    }

    public void setListaFluxoCaixa(List<ViewFinFluxoCaixaID> listaFluxoCaixa) {
        this.listaFluxoCaixa = listaFluxoCaixa;
    }

    public List<ViewFinFluxoCaixaID> getListaFluxoCaixaDetalhe() {
        return listaFluxoCaixaDetalhe;
    }
}
