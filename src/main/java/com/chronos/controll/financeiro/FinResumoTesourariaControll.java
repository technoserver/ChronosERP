package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.view.ViewFinResumoTesourariaID;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.Biblioteca;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 15/08/17.
 */
@Named
@ViewScoped
public class FinResumoTesourariaControll extends AbstractControll<ViewFinResumoTesourariaID> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<ViewFinResumoTesourariaID> repository;

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
                filtros.add(new Filtro(Filtro.AND, "viewFinResumoTesouraria.dataLancamento", Filtro.MAIOR_OU_IGUAL, Biblioteca.getDataInicial(periodo)));
                filtros.add(new Filtro(Filtro.AND, "viewFinResumoTesouraria.dataLancamento", Filtro.MENOR_OU_IGUAL, Biblioteca.ultimoDiaMes(periodo)));

                if (isTelaGrid()) {
                    listaResumoTesouraria = repository.getEntitys(ViewFinResumoTesourariaID.class, filtros);
                } else {
                    filtros.add(new Filtro(Filtro.AND, "viewFinResumoTesouraria.idContaCaixa", Filtro.IGUAL, getObjeto().getViewFinResumoTesouraria().getIdContaCaixa()));
                    listaResumoTesourariaDetalhe = repository.getEntitys(ViewFinResumoTesourariaID.class, filtros);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Erro ao buscar os dados!", e);

        }
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
        return "RESUMO_TESOURARIA";
    }

    @Override
    protected boolean auditar() {
        return false;
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
