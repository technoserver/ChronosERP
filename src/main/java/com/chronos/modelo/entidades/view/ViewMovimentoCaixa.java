package com.chronos.modelo.entidades.view;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "view_movimento_caixa")
public class ViewMovimentoCaixa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic
    @Column(name = "id")
    private Integer id;
    private Integer idmovimento;
    private Integer idorigem;
    @Column(name = "data_hora")
    private Date dataHora;
    private String descricao;
    private BigDecimal valor;
    @Column(name = "codigo_forma_pagamento")
    private String codigoFormaPagamento;
    @Column(name = "forma_pagamento")
    private String formaPagamento;
    private String tipo;
    private String origem;
    private String observacao;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewMovimentoCaixa that = (ViewMovimentoCaixa) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
