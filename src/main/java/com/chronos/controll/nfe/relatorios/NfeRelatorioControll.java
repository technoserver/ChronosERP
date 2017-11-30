package com.chronos.controll.nfe.relatorios;

import com.chronos.controll.AbstractRelatorioControll;
import com.chronos.modelo.entidades.NfeCabecalho;
import com.chronos.modelo.entidades.enuns.StatusTransmissao;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.comercial.NfeService;
import com.chronos.util.Biblioteca;
import com.chronos.util.jsf.Mensagem;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by john on 30/11/17.
 */
@Named
@RequestScoped
public class NfeRelatorioControll extends AbstractRelatorioControll implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date periodo;
    private Date dataFinal;
    private String modelo;
    @Inject
    private Repository<NfeCabecalho> repository;
    @Inject
    private NfeService service;

    public void relacaoNumero() {

        parametros = new HashMap<>();
        parametros.put("dataInicial", Biblioteca.getDataInicial(periodo));
        parametros.put("dataFinal", Biblioteca.ultimoDiaMes(periodo));
        parametros.put("idempresa", empresa.getId());

        String caminhoRelatorio = "/relatorios/nfe";
        String nomeRelatorio = "relacaoNumeroNfe.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "numerosNfe.pdf");

    }

    public void relacaoNfe() {

        parametros = new HashMap<>();
        parametros.put("dataInicial", periodo);
        parametros.put("dataFinal", dataFinal);
        parametros.put("idempresa", empresa.getId());

        String caminhoRelatorio = "/relatorios/nfe";
        String nomeRelatorio = "relacaoNfe.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "relacaoNfe.pdf");

    }

    public void baixaXml() {
        try {
            List<NfeCabecalho> nfes;
            List<Filtro> filtros = new LinkedList<>();

            filtros.add(new Filtro(Filtro.AND, "dataHoraEmissao", Filtro.MAIOR_OU_IGUAL, periodo));
            filtros.add(new Filtro(Filtro.AND, "dataHoraEmissao", Filtro.MENOR_OU_IGUAL,
                    new Date(dataFinal.getTime() + (1000 * 60 * 60 * 24))));
            filtros.add(new Filtro(Filtro.AND, "codigoModelo", Filtro.IGUAL, modelo));
            filtros.add(new Filtro(Filtro.AND, "statusNota", Filtro.MAIOR_OU_IGUAL, StatusTransmissao.AUTORIZADA.getCodigo()));
            nfes = repository.getEntitys(NfeCabecalho.class, filtros, 0, new Object[]{"chaveAcesso", "digitoChaveAcesso", "statusNota"});

            if (nfes.size() > 0) {
                service.baixaXml(nfes, empresa);
            } else {
                Mensagem.addInfoMessage((modelo.equals("55") ? "NFe " : "NFCe") + " não localizada");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }

    public Date getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Date periodo) {
        this.periodo = periodo;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
}
