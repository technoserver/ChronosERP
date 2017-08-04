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
    private DecimalFormatSymbols simboloDecimal;

    private DecimalFormat formatoQuantidade;
    private DecimalFormat formatoValor;
    private DecimalFormat formatoNumeroDocFiscal;
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
        this.formatoSerie = new DecimalFormat("000");

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

    public Date formatarDataNota(String data) throws ParseException {
        return formatoDataNota.parse(data);
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

    public Number formatarNumeroDocFiscalToNumber(String valor) throws ParseException {

        return formatoNumeroDocFiscal.parse(valor);
    }

    public String formatarQuantidadeToString(Object valor) {

        return formatoQuantidade.format(valor);
    }

    public Number formatarQuantidadeToNumber(String valor) throws ParseException {

        return formatoQuantidade.parse(valor);
    }

    public BigDecimal formatarQuantidadeToBigDecimal(String valor) throws ParseException {
        BigDecimal vlr = valor==null?null: BigDecimal.valueOf(formatarQuantidadeToNumber(valor).doubleValue());
        return vlr;
    }


    public String formatarValorToString(Object valor) {

        return formatoQuantidade.format(valor);
    }

    public Number formatarValorToNumber(String valor) throws ParseException {
        return formatoQuantidade.parse(valor);
    }

    public BigDecimal formatarValorToBigDecimal(String valor) throws ParseException {
        BigDecimal vlr = valor==null?null: BigDecimal.valueOf(formatarValorToNumber(valor).doubleValue());
        return vlr;
    }


}
