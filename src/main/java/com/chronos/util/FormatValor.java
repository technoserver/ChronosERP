package com.chronos.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by john on 02/08/17.
 */
public class FormatValor {

    private static FormatValor instance;

    private SimpleDateFormat formatoDataUA;
    private SimpleDateFormat formatoDataPTB;
    private SimpleDateFormat formatoDataNota;
    private SimpleDateFormat formatoAno;
    private SimpleDateFormat formatoMes;
    private DecimalFormatSymbols simboloDecimal;

    private DecimalFormat formatoQuantidade;
    private DecimalFormat formatoValor;
    private DecimalFormat formatoNumeroDocFiscal;
    private DecimalFormat formatoCodigoNumeroDocFiscal;
    private DecimalFormat formatoSerie;


    private FormatValor() {
        this.formatoDataUA = new SimpleDateFormat("yyyy-MM-dd");
        this.formatoDataPTB = new SimpleDateFormat("dd/MM/yyyy");
        this.formatoDataNota = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        this.simboloDecimal = DecimalFormatSymbols.getInstance();
        this.simboloDecimal.setDecimalSeparator('.');
        this.formatoQuantidade = new DecimalFormat("0.0000", simboloDecimal);
        this.formatoValor = new DecimalFormat("0.00", simboloDecimal);
        this.formatoNumeroDocFiscal = new DecimalFormat("000000000");
        this.formatoCodigoNumeroDocFiscal = new DecimalFormat("00000000");
        this.formatoSerie = new DecimalFormat("000");
        this.formatoAno = new SimpleDateFormat("yy");
        this.formatoMes = new SimpleDateFormat("MM");

    }

    public static FormatValor getInstance() {
        if (instance == null) {
            instance = new FormatValor();
            return instance;
        }
        return instance;
    }


    public Date formatarDataEUA(String data) throws ParseException {
        return formatoDataUA.parse(data);
    }

    public String formatarDataEUA(Date data) throws ParseException {
        return formatoDataUA.format(data);
    }

    public String formatarData(Date data) throws ParseException {
        return formatoDataPTB.format(data);
    }

    public Date formatarDataNota(String data) throws ParseException {
        return formatoDataNota.parse(data);
    }

    public String formatarDataNota(Date data) throws ParseException {
        return formatoDataNota.format(data);
    }

    public String formatarAno(Date date) {
        return formatoAno.format(date);
    }

    public String formatarMes(Date date) {
        return formatoMes.format(date);
    }


    public String formatarSerieToString(Object valor) {

        return formatoSerie.format(valor);
    }

    public Number formatarSerieToNumber(String valor) throws ParseException {

        return formatoSerie.parse(valor);
    }


    public String formatarNumeroDocFiscalToString(Object valor) {

        return formatoNumeroDocFiscal.format(valor);
    }

    public String formatarCodigoNumeroDocFiscalToString(Object valor) {

        return formatoCodigoNumeroDocFiscal.format(valor);
    }

    public Number formatarNumeroDocFiscalToNumber(String valor) throws ParseException {

        return formatoNumeroDocFiscal.parse(valor);
    }

    public String formatarQuantidade(Object valor) {

        return formatoQuantidade.format(valor);
    }

    public Number formatarQuantidadeToNumber(String valor) throws ParseException {

        return formatoQuantidade.parse(valor);
    }

    public BigDecimal formatarQuantidadeToBigDecimal(String valor) throws ParseException {
        BigDecimal vlr = valor==null?null: BigDecimal.valueOf(formatarQuantidadeToNumber(valor).doubleValue());
        return vlr;
    }


    public String formatarValor(Object valor) {

        return formatoValor.format(valor);
    }

    public String formatarValorToNull(Object valor) {
        BigDecimal vlr = (BigDecimal) valor;
        return vlr == null || vlr.signum() == 0 ? null : formatoValor.format(valor);
    }


    public Number formatarValorToNumber(String valor) throws ParseException {
        return formatoQuantidade.parse(valor);
    }

    public BigDecimal formatarValorToBigDecimal(String valor) throws ParseException {
        BigDecimal vlr = valor==null?null: BigDecimal.valueOf(formatarValorToNumber(valor).doubleValue());
        return vlr;
    }


}
