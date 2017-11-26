package com.chronos.controll.estoque.relatorios;

import com.chronos.controll.AbstractRelatorioControll;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by john on 18/09/17.
 */
@Named
@RequestScoped
public class EstoqueRelatorioControll extends AbstractRelatorioControll implements Serializable {

    private static final long serialVersionUID = 1L;


    private Date dataInicial;
    private Date dataFinal;


    public void imprimirRelacaoEntradas() {
        parametros = new HashMap<>();
        parametros.put("peridoInicial", dataInicial);
        parametros.put("peridoFinal", dataFinal);
        parametros.put("idempresa", empresa.getId());
        String caminhoRelatorio = "/relatorios/estoque";
        String nomeRelatorio = "relacaoNotaEntrada.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "relacaoEntradas.pdf");
    }


    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }
}
