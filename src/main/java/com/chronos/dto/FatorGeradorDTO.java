package com.chronos.dto;

import java.math.BigDecimal;
import java.util.Date;

public class FatorGeradorDTO {

    private Integer id;
    private Date dataVenda;
    private BigDecimal valor;
    private String vendedor;
    private String numNota;
    private Date dataNota;
    private String doc;


    public FatorGeradorDTO(Integer id, Date dataVenda, BigDecimal valor, String vendedor, String numNota, Date dataNota) {
        this.id = id;
        this.dataVenda = dataVenda;
        this.valor = valor;
        this.vendedor = vendedor;
        this.numNota = numNota;
        this.dataNota = dataNota;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getNumNota() {
        return numNota;
    }

    public void setNumNota(String numNota) {
        this.numNota = numNota;
    }

    public Date getDataNota() {
        return dataNota;
    }

    public void setDataNota(Date dataNota) {
        this.dataNota = dataNota;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }
}
