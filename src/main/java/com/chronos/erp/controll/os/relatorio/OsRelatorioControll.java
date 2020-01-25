package com.chronos.erp.controll.os.relatorio;

import com.chronos.erp.controll.AbstractRelatorioControll;
import com.chronos.erp.modelo.entidades.OsAbertura;
import com.chronos.erp.repository.Repository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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

    @Inject
    private Repository<OsAbertura> repository;

    private Date dataInicial;
    private Date dataFinal;
    private int id;
    private OsAbertura os;


    public void buscarOs() {
        os = repository.get(id, OsAbertura.class);
    }

    public void imprimirOS(int id, String tipo) {
        parametros = new HashMap<>();
        parametros.put("id_os", id);
        String caminhoRelatorio = "/relatorios/os";
        String nomeRelatorio = tipo.equals("OC") ? "os_orcamento.jasper" : "os.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "os.pdf");
    }

    public void imprimirComprovanteOS(int id) {
        parametros = new HashMap<>();
        parametros.put("id_os", id);
        parametros.put("comprovanteEntrega", true);
        String caminhoRelatorio = "/relatorios/os";
        String nomeRelatorio = "os.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "os.pdf");
    }

    public void impirmirFichaAtendimento(int id) {
        parametros = new HashMap<>();
        parametros.put("id_os", id);
        String caminhoRelatorio = "/relatorios/os";
        String nomeRelatorio = "os_ficha_atendimento.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "ficha_atendimento.pdf");
    }

    public void imprimiOs2Via(int id) {
        parametros = new HashMap<>();
        parametros.put("id_os", id);
        String caminhoRelatorio = "/relatorios/os";
        String nomeRelatorio = "osAtendimento.jasper";

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OsAbertura getOs() {
        return os;
    }
}
