package com.chronos.util.report;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by john on 02/10/17.
 */
public class JasperReportUtil {

    private String SEPARATOR = File.separator;
    private final String PONTO = ".";

    public byte[] gerarRelatorio(JRXmlDataSource dataSource, HashMap parametrosRelatorio, String caminhoRelatorio, String nomeRelatorioJasper) throws JRException {

        InputStream relatorioStream = this.getClass().getResourceAsStream(caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "jasper");

        String subDiretorio = this.getClass().getResource(caminhoRelatorio + SEPARATOR).getPath();
        parametrosRelatorio.put("SUBREPORT_DIR", subDiretorio);
        parametrosRelatorio.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));

        JasperPrint jp = JasperFillManager.fillReport(relatorioStream, parametrosRelatorio, dataSource);

        return JasperExportManager.exportReportToPdf(jp);
    }
}
