package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.view.ViewFinResumoTesourariaID;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 15/08/17.
 */
@Named
@ViewScoped
public class FinResumoTesourariaControll extends AbstractControll<ViewFinResumoTesourariaID> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Repository<ViewFinResumoTesourariaID> resumos;

    private Date periodo;
    private List<ViewFinResumoTesourariaID> listaResumoTesouraria;
    private List<ViewFinResumoTesourariaID> listaResumoTesourariaDetalhe;


    @Override
    public void doEdit() {
        super.doEdit();
        buscaDados();
    }

    public void buscaDados() {
        try {
            if (periodo == null) {
                Mensagem.addInfoMessage("Necessario informar o periodo!");

            } else {
                List<Filtro> filtros;
                filtros = new ArrayList<>();
                filtros.add(new Filtro(Filtro.AND, "viewFinResumoTesouraria.dataLancamento", Filtro.MAIOR_OU_IGUAL, getDataInicial()));
                filtros.add(new Filtro(Filtro.AND, "viewFinResumoTesouraria.dataLancamento", Filtro.MENOR_OU_IGUAL, ultimoDiaMes()));

                if (isTelaGrid()) {
                    listaResumoTesouraria = resumos.getEntitys(ViewFinResumoTesourariaID.class, filtros);
                } else {
                    filtros.add(new Filtro(Filtro.AND, "viewFinResumoTesouraria.idContaCaixa", Filtro.IGUAL, getObjeto().getViewFinResumoTesouraria().getIdContaCaixa()));
                    listaResumoTesourariaDetalhe = resumos.getEntitys(ViewFinResumoTesourariaID.class,filtros);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Erro ao buscar os dados!", e);

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

        for (ViewFinResumoTesourariaID f : listaResumoTesourariaDetalhe) {
            if (f.getViewFinResumoTesouraria().getOperacao().equals("E")) {
                aReceber = aReceber.add(f.getViewFinResumoTesouraria().getValor());
            } else {
                aPagar = aPagar.add(f.getViewFinResumoTesouraria().getValor());
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
    protected Class<ViewFinResumoTesourariaID> getClazz() {
        return ViewFinResumoTesourariaID.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "FIN_RESUMO_TESOURARIA";
    }

    public Date getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Date periodo) {
        this.periodo = periodo;
    }

    public List<ViewFinResumoTesourariaID> getListaResumoTesouraria() {
        return listaResumoTesouraria;
    }

    public void setListaResumoTesouraria(List<ViewFinResumoTesourariaID> listaResumoTesouraria) {
        this.listaResumoTesouraria = listaResumoTesouraria;
    }

    public List<ViewFinResumoTesourariaID> getListaResumoTesourariaDetalhe() {
        return listaResumoTesourariaDetalhe;
    }

    public void setListaResumoTesourariaDetalhe(List<ViewFinResumoTesourariaID> listaResumoTesourariaDetalhe) {
        this.listaResumoTesourariaDetalhe = listaResumoTesourariaDetalhe;
    }
}
