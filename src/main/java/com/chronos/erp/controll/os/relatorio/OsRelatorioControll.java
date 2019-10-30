package com.chronos.erp.controll.os.relatorio;

import com.chronos.erp.controll.AbstractRelatorioControll;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by john on 14/12/17.
 */
@Named
@RequestScoped
public class OsRelatorioControll extends AbstractRelatorioControll implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date dataInicial;
    private Date dataFinal;

    public void imprimirOS(int id) {
        parametros = new HashMap<>();
        parametros.put("id_os", id);
        String caminhoRelatorio = "/relatorios/os";
        String nomeRelatorio = "os.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "os.pdf");
    }

    public void imprimirRelacao() {
        parametros = new HashMap<>();
        parametros.put("idempresa", empresa.getId());
        parametros.put("periodoInicial", dataInicial);
        parametros.put("periodoFinal", dataFinal);
        String caminhoRelatorio = "/relatorios/os";
        String nomeRelatorio = "relacaoOS.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "relacaoOs.pdf");
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
