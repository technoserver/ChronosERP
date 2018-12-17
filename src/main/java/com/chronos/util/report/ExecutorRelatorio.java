/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util.report;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.*;
import org.hibernate.jdbc.Work;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author john
 */
public class ExecutorRelatorio implements Work {

    private String diretorioRelatorio;
    private String nomeRelatorio;
    private HttpServletResponse response;
    private Map<String, Object> parametros;
    private String nomeArquivoSaida;

    private boolean relatorioGerado;


    public ExecutorRelatorio(String diretorioRelatorio, String nomeRelatorio,
                             HttpServletResponse response, Map<String, Object> parametros,
                             String nomeArquivoSaida) {
        this.diretorioRelatorio = diretorioRelatorio;
        this.nomeRelatorio = nomeRelatorio;
        this.response = response;
        this.parametros = parametros;
        this.nomeArquivoSaida = nomeArquivoSaida;

        this.parametros.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
    }

    @Override
    public void execute(Connection connection) throws SQLException {
        try {
            InputStream relatorioStream = this.getClass().getResourceAsStream(this.diretorioRelatorio + File.separator + this.nomeRelatorio);
            if(relatorioStream == null){
                throw  new Exception("relatório "+this.nomeRelatorio+" não localizado");
            }
            parametros.put("SUBREPORT_DIR", this.getClass().getResource(this.diretorioRelatorio + File.separator).getPath());

            JasperPrint print = JasperFillManager.fillReport(relatorioStream, this.parametros, connection);
            this.relatorioGerado = print.getPages().size() > 0;

            if (this.relatorioGerado) {
                Exporter<ExporterInput, PdfReportConfiguration, PdfExporterConfiguration, OutputStreamExporterOutput> exportador = new JRPdfExporter();
                exportador.setExporterInput(new SimpleExporterInput(print));
                exportador.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

                // Seta o nome do arquivo e a disposição: "inline" abre no próprio navegador
                // Mude para "attachment" para indicar que deve ser feito um download

                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline; filename=\""
                        + this.nomeArquivoSaida + "\"");

                exportador.exportReport();
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao executar relatório " + this.diretorioRelatorio + "/" + this.nomeRelatorio, e);
        }
    }

    public boolean isRelatorioGerado() {
        return relatorioGerado;
    }
}
