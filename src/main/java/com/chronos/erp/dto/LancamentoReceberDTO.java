package com.chronos.erp.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class LancamentoReceberDTO implements Serializable {

    private Integer id;
    private String numDoc;
    private String cliente;
    private Integer qtdParcelas;
    private BigDecimal valorAReceber;
    private BigDecimal saldo;
    private Date dataLancamento;
    private String status;

    public LancamentoReceberDTO() {
    }

    public LancamentoReceberDTO(Integer id, String numDoc, String cliente, Integer qtdParcelas, BigDecimal valorAReceber, BigDecimal saldo, Date dataLancamento, String status) {
        this.id = id;
        this.numDoc = numDoc;
        this.cliente = cliente;
        this.qtdParcelas = qtdParcelas;
        this.valorAReceber = valorAReceber;
        this.saldo = saldo;
        this.dataLancamento = dataLancamento;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public Integer getQtdParcelas() {
        return qtdParcelas;
    }

    public void setQtdParcelas(Integer qtdParcelas) {
        this.qtdParcelas = qtdParcelas;
    }

    public BigDecimal getValorAReceber() {
        return valorAReceber;
    }

    public void setValorAReceber(BigDecimal valorAReceber) {
        this.valorAReceber = valorAReceber;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
