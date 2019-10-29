package com.chronos.erp.util.report;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by john on 02/10/17.
 */
public class JasperReportUtil {

    private final String PONTO = ".";
    private String SEPARATOR = File.separator;
    private HttpServletResponse response;

    public JasperReportUtil() {
    }

    public JasperReportUtil(HttpServletResponse response) {
        this.response = response;
    }

    public byte[] gerarRelatorio(JRXmlDataSource dataSource, HashMap parametrosRelatorio, String caminhoRelatorio, String nomeRelatorioJasper) throws JRException {
        String relatorio = caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "jasper";
        InputStream relatorioStream = this.getClass().getResourceAsStream(relatorio);
        if (relatorioStream == null) {
            throw new RuntimeException("Caminho do relatorio não encontrado : " + relatorio);
        }
        String subDiretorio = this.getClass().getResource(caminhoRelatorio + SEPARATOR).getPath();
        parametrosRelatorio.put("SUBREPORT_DIR", subDiretorio);
        parametrosRelatorio.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));

        JasperPrint jp = JasperFillManager.fillReport(relatorioStream, parametrosRelatorio, dataSource);

        return JasperExportManager.exportReportToPdf(jp);
    }

    public void gerarRelatorioBaixa(HashMap parametrosRelatorio, String caminhoRelatorio, String nomeRelatorioJasper, String nomeArquivoSaida) throws JRException, IOException {

        InputStream relatorioStream = this.getClass().getResourceAsStream(caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "jasper");

        parametrosRelatorio.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));

        JasperPrint jp = JasperFillManager.fillReport(relatorioStream, parametrosRelatorio);


        baixaRelatorio(jp, nomeArquivoSaida);


    }

    public void baixaRelatorio(JasperPrint print, String nomeArquivoSaida) throws IOException, JRException {
        if (print.getPages().size() > 0) {

            Exporter<ExporterInput, PdfReportConfiguration, PdfExporterConfiguration, OutputStreamExporterOutput> exportador = new JRPdfExporter();
            exportador.setExporterInput(new SimpleExporterInput(print));
            exportador.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + nomeArquivoSaida + "\"");

            exportador.exportReport();
        }
    }
}
