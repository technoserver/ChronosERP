package com.chronos.erp.dto;

/**
 * Created by john on 18/06/18.
 */
public class DocFiscalDto {

    private String chave;
    private Integer indicadorReentrega;
    private Integer modelo;

    public DocFiscalDto(String chave, Integer indicadorReentrega, Integer modelo) {
        this.chave = chave;
        this.indicadorReentrega = indicadorReentrega;
        this.modelo = modelo;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public Integer getIndicadorReentrega() {
        return indicadorReentrega;
    }

    public void setIndicadorReentrega(Integer indicadorReentrega) {
        this.indicadorReentrega = indicadorReentrega;
    }

    public Integer getModelo() {
        return modelo;
    }

    public void setModelo(Integer modelo) {
        this.modelo = modelo;
    }
}
