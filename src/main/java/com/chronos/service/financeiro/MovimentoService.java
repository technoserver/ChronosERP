package com.chronos.service.financeiro;

import com.chronos.modelo.entidades.*;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.ArquivoUtil;
import com.chronos.util.Biblioteca;
import com.chronos.util.FormatValor;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.*;

import static java.nio.file.FileSystems.getDefault;

/**
 * Created by john on 19/01/18.
 */
public class MovimentoService implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<PdvMovimento> repository;
    @Inject
    private Repository<PdvSuprimento> pdvSuprimentoRepository;


    @Inject
    private ExternalContext context;

    @Inject
    private Repository<PdvOperador> pdvOperadorRepository;

    private PdvMovimento movimento;
    private StringBuilder linhasRelatorio;
    private String nomeImpressaoMovimento;


    @PostConstruct
    private void init() {
        movimento = FacesUtil.getMovimento();
    }
    @Transactional
    public void iniciarMovimento(BigDecimal valorSuprimento) throws Exception {

        movimento = new PdvMovimento();
        movimento.setEmpresa(FacesUtil.getEmpresaUsuario());

        movimento.setIdGerenteSupervisor(1);
        movimento.setDataAbertura(new Date());
        movimento.setHoraAbertura(FormatValor.getInstance().formatarHora(new Date()));
        movimento.setTotalSuprimento(valorSuprimento);

        atualizar(movimento);

        addSuprimento(valorSuprimento);
        imprimeAbertura();
    }

    @Transactional
    public void realizarFechamento() throws ParseException {
        movimento.setDataFechamento(new Date());
        movimento.setStatusMovimento("F");
        movimento.setHoraFechamento(FormatValor.getInstance().formatarHora(new Date()));
        repository.atualizar(movimento);
        FacesUtil.setMovimento(null);
    }


    @Transactional
    private void addSuprimento(BigDecimal valor) {
        PdvSuprimento suprimento = new PdvSuprimento();
        suprimento.setPdvMovimento(movimento);
        suprimento.setDataSuprimento(new Date());
        suprimento.setObservacao("Abertura do Caixa");
        suprimento.setValor(valor);

        pdvSuprimentoRepository.salvar(suprimento);
    }

    public void lancaVenda(BigDecimal valor) {
        movimento.setTotalVenda(Biblioteca.soma(movimento.getTotalVenda(), valor));
        atualizar(movimento);
    }
    public void lancaVenda(BigDecimal valorVenda, BigDecimal desconto, BigDecimal troco) {
        movimento.setTotalVenda(Biblioteca.soma(movimento.getTotalVenda(), valorVenda));
        movimento.setTotalDesconto(Biblioteca.soma(movimento.getTotalDesconto(),Optional.ofNullable(desconto).orElse(BigDecimal.ZERO)));
        movimento.setTotalTroco(Biblioteca.soma(movimento.getTotalDesconto(), Optional.ofNullable(troco).orElse(BigDecimal.ZERO)));
        atualizar(movimento);
    }

    public void lancaSangria(BigDecimal valor){
        movimento.setTotalSangria(Biblioteca.soma(movimento.getTotalSangria(), valor));
        atualizar(movimento);
    }

    public void lancaAcrescimo(BigDecimal valor){
        movimento.setTotalAcrescimo(Biblioteca.soma(movimento.getTotalAcrescimo(), valor));
        atualizar(movimento);
    }

    public void lancaRecebimento(BigDecimal valor){
        movimento.setTotalRecebido(Biblioteca.soma(movimento.getTotalRecebido(), valor));
        atualizar(movimento);
    }

    public void lancaTroco(BigDecimal valor){
        movimento.setTotalTroco(Biblioteca.soma(movimento.getTotalTroco(), valor));
        atualizar(movimento);
    }

    @Transactional
    public void atualizar(PdvMovimento movimento){
        this.movimento = movimento;
        this.movimento.calcularTotalFinal();
        this.movimento = repository.atualizar(movimento);
        FacesUtil.setMovimento(movimento);
    }


    private void imprimeAbertura() {
        try {
            linhasRelatorio = new StringBuilder();
            linhasRelatorio.setLength(0);
            append(Biblioteca.repete("=", 48));
            append("               ABERTURA DE CAIXA ");
            append("");
            append("DATA DE ABERTURA: " + FormatValor.getInstance().formatarData(movimento.getDataAbertura()));
            append("HORA DE ABERTURA: " + movimento.getHoraAbertura());
            append("Caixa principal  OPERADOR: padrão");
            append("MOVIMENTO: " + movimento.getId());
            append(Biblioteca.repete("=", 48));
            append("");
            append("SUPRIMENTO...: " + FormatValor.getInstance().formatoDecimal("V", movimento.getTotalSuprimento().doubleValue()));
            append("");
            append("");
            append("");
            append(" ________________________________________ ");
            append("             VISTO DO CAIXA ");
            append("");
            append("");
            append("");
            append(" ________________________________________ ");
            append("           VISTO DO SUPERVISOR ");

            Map map = new HashMap();
            File fileTemp = new File(context.getRealPath("/") + System.getProperty("file.separator") + "temp");
            if (!fileTemp.exists()) {
                fileTemp.mkdir();
            }
            map.put("CONTEUDO", linhasRelatorio.toString());
            String caminhoJasper = "/com/chronos/erplight/relatorios/comercial/nfce/relatorioMovimento.jasper";
            InputStream inputStream = this.getClass().getResourceAsStream(caminhoJasper);
            JasperPrint jp = JasperFillManager.fillReport(inputStream, map, new JREmptyDataSource());
            byte[] pdfFile = JasperExportManager.exportReportToPdf(jp);
            Path localPdf = getDefault().getPath(fileTemp.getPath());
            nomeImpressaoMovimento = "movimento.pdf";
            ArquivoUtil.getInstance().escrever(pdfFile, nomeImpressaoMovimento, localPdf.toString());
            RequestContext.getCurrentInstance().addCallbackParam("movimentoIniciado", true);

        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao imprimir o relatório de abertura de movimento.\n.", ex);

        }
    }

    private void append(String texto) {
        linhasRelatorio.append(texto + "\n");
    }


    public PdvMovimento getMovimento() {
        return movimento;
    }

}
