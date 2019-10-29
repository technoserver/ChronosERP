package com.chronos.erp.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PdvMovimentoDTO {

    private Integer id;
    private String operador;
    private Integer idmovimento;
    private Integer idorigem;
    private Date dataHora;
    private String descricao;
    private BigDecimal valor;
    private String codigoFormaPagamento;
    private String formaPagamento;
    private String tipo;
    private String origem;
    private String observacao;

    public PdvMovimentoDTO() {
    }

    public PdvMovimentoDTO(Integer id, String operador, Integer idmovimento, Integer idorigem, Date dataHora, String descricao, BigDecimal valor, String codigoFormaPagamento, String formaPagamento, String tipo, String origem, String observacao) {
        this.id = id;
        this.operador = operador;
        this.idmovimento = idmovimento;
        this.idorigem = idorigem;
        this.dataHora = dataHora;
        this.descricao = descricao;
        this.valor = valor;
        this.codigoFormaPagamento = codigoFormaPagamento;
        this.tipo = tipo;
        this.origem = origem;
        this.observacao = observacao;
        this.formaPagamento = formaPagamento;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public Integer getIdmovimento() {
        return idmovimento;
    }

    public void setIdmovimento(Integer idmovimento) {
        this.idmovimento = idmovimento;
    }

    public Integer getIdorigem() {
        return idorigem;
    }

    public void setIdorigem(Integer idorigem) {
        this.idorigem = idorigem;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getCodigoFormaPagamento() {
        return codigoFormaPagamento;
    }

    public void setCodigoFormaPagamento(String codigoFormaPagamento) {
        this.codigoFormaPagamento = codigoFormaPagamento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
}
