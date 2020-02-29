package com.chronos.erp.dto;

import java.math.BigDecimal;
import java.util.Date;

public class LancamentoMovimentoDTO {

    private String operador;
    private Date data;
    private BigDecimal valor;
    private String observacao;
    private Integer tipo;

    public LancamentoMovimentoDTO(String operador, Date data, BigDecimal valor, String observacao, Integer tipo) {
        this.operador = operador;
        this.data = data;
        this.valor = valor;
        this.observacao = observacao;
        this.tipo = tipo;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
}
