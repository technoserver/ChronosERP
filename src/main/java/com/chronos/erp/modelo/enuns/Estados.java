/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.modelo.enuns;

/**
 * @author usuario
 */
public enum Estados {

    AC("AC", "ACRE", "12"),
    AL("AL", "ALAGOAS", "27"),
    AP("AP", "AMAPA", "16"),
    AM("AM", "AMAZONAS", "13"),
    BA("BA", "BAHIA", "29"),
    CE("CE", "CEARA", "23"),
    DF("DF", "DISTRITO FEDERAL", "53"),
    ES("ES", "ESPIRITO SANTOS", "32"),
    GO("GO", "GOIAIS", "52"),
    MA("MA", "MARANHAO", "21"),
    MT("MT", "MATO GROSSO", "51"),
    MS("MS", "MATO GROSSO DO SUL", "50"),
    MG("MG", "MINAS GERAIS", "31"),
    PA("PA", "PARA", "15"),
    PB("PB", "PARAIBA", "25"),
    PR("PR", "PARANA", "41"),
    PE("PE", "PERNANBUCO", "26"),
    PI("PI", "PIAUI", "22"),
    RJ("RJ", "RIO DE JANEIRO", "33"),
    RN("RB", "RIO GRANDE DO NORTE", "24"),
    RS("RS", "RIO GRANDE DO SUL", "43"),
    RO("RO", "RONDONIA", "11"),
    RR("RR", "RORAIMA", "14"),
    SC("SC", "SANTA CATARINA", "42"),
    SP("SP", "SAO PAULO", "35"),
    SE("SE", "SERGIPE", "28"),
    TO("TO", "TOCANTINS", "17");
    private String codigo;
    private String nomeExibicao;
    private String codigoIbge;

    Estados(String codigo, String nomeExibicao, String codigoIbge) {
        this.codigo = codigo;
        this.nomeExibicao = nomeExibicao;
        this.codigoIbge = codigoIbge;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNomeExibicao() {
        return nomeExibicao;
    }

    public void setNomeExibicao(String nomeExibicao) {
        this.nomeExibicao = nomeExibicao;
    }


    public String getCodigoIbge() {
        return codigoIbge;
    }

    public void setCodigoIbge(String codigoIbge) {
        this.codigoIbge = codigoIbge;
    }

    public Estados[] getEstados() {
        return Estados.values();
    }


}
