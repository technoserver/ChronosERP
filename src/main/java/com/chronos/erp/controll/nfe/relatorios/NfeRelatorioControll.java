package com.chronos.erp.controll.nfe.relatorios;

import br.com.samuelweb.certificado.exception.CertificadoException;
import com.chronos.erp.controll.AbstractRelatorioControll;
import com.chronos.erp.modelo.entidades.NfeCabecalho;
import com.chronos.erp.modelo.enuns.StatusTransmissao;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.comercial.NfeService;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.jsf.Mensagem;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import org.apache.commons.lang3.time.DateUtils;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

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

            periodo = DateUtils.truncate(periodo == null ? new Date() : periodo, Calendar.DATE);
            dataFinal = DateUtils.addSeconds(DateUtils.addMinutes(DateUtils.addHours(dataFinal == null ? new Date() : dataFinal, 23), 59), 59);
            String nomeArquivo = empresa.getCnpj() + new SimpleDateFormat("_MM_yyyy").format(periodo);
            filtros.add(new Filtro(Filtro.AND, "dataHoraEmissao", Filtro.MAIOR_OU_IGUAL, periodo));
            filtros.add(new Filtro(Filtro.AND, "dataHoraEmissao", Filtro.MENOR_OU_IGUAL,
                    new Date(dataFinal.getTime() + (1000 * 60 * 60 * 24))));
            filtros.add(new Filtro(Filtro.AND, "codigoModelo", Filtro.IGUAL, modelo));
            filtros.add(new Filtro(Filtro.AND, "empresa.id", Filtro.IGUAL, empresa.getId()));
            filtros.add(new Filtro("statusNota", StatusTransmissao.AUTORIZADA.getCodigo(), StatusTransmissao.CANCELADA.getCodigo()));
            nfes = repository.getEntitys(NfeCabecalho.class, filtros, 0, new Object[]{"empresa.id", "empresa.cnpj", "digitoChaveAcesso", "chaveAcesso", "qrcode", "codigoModelo", "serie", "statusNota"});

            if (nfes.size() > 0) {
                service.instanciarConfNfe(empresa, ModeloDocumento.getByCodigo(Integer.valueOf(modelo)), "001");
                service.baixaXml(nfes, nomeArquivo);
            } else {
                Mensagem.addInfoMessage((modelo.equals("55") ? "NFe " : "NFCe") + " não localizada");
            }
        } catch (Exception ex) {
            if (ex instanceof ChronosException || ex instanceof CertificadoException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao baixa os xml", ex);
            }
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
