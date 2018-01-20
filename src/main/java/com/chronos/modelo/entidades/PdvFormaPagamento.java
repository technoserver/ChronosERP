
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "pdv_forma_pagamento")
public class PdvFormaPagamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "FORMA")
    private String forma;
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Column(name = "CARTAO_TIPO_INTEGRACAO")
    private String cartaoTipoIntegracao;
    @Column(name = "CNPJ_OPERADORA_CARTAO")
    private String cnpjOperadoraCartao;
    @Column(name = "BANDEIRA")
    private String bandeira;
    @Column(name = "NUMERO_AUTORIZACAO")
    private String numeroAutorizacao;
    @Column(name = "ESTORNO")
    private String estorno;
    @Column(name = "TROCO")
    private BigDecimal troco;
    @JoinColumn(name = "ID_PDV_VENDA_CABECALHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PdvVendaCabecalho pdvVendaCabecalho;
    @JoinColumn(name = "ID_PDV_TIPO_PAGAMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PdvTipoPagamento pdvTipoPagamento;
    @Transient
    private VendaCondicoesPagamento condicao;

    public PdvFormaPagamento() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getForma() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getCartaoTipoIntegracao() {
        return cartaoTipoIntegracao;
    }

    public void setCartaoTipoIntegracao(String cartaoTipoIntegracao) {
        this.cartaoTipoIntegracao = cartaoTipoIntegracao;
    }

    public String getCnpjOperadoraCartao() {
        return cnpjOperadoraCartao;
    }

    public void setCnpjOperadoraCartao(String cnpjOperadoraCartao) {
        this.cnpjOperadoraCartao = cnpjOperadoraCartao;
    }

    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    public String getNumeroAutorizacao() {
        return numeroAutorizacao;
    }

    public void setNumeroAutorizacao(String numeroAutorizacao) {
        this.numeroAutorizacao = numeroAutorizacao;
    }

    public String getEstorno() {
        return estorno;
    }

    public void setEstorno(String estorno) {
        this.estorno = estorno;
    }

    public BigDecimal getTroco() {
        return troco;
    }

    public void setTroco(BigDecimal troco) {
        this.troco = troco;
    }

    public PdvVendaCabecalho getPdvVendaCabecalho() {
        return pdvVendaCabecalho;
    }

    public void setPdvVendaCabecalho(PdvVendaCabecalho pdvVendaCabecalho) {
        this.pdvVendaCabecalho = pdvVendaCabecalho;
    }

    public PdvTipoPagamento getPdvTipoPagamento() {
        return pdvTipoPagamento;
    }

    public void setPdvTipoPagamento(PdvTipoPagamento pdvTipoPagamento) {
        this.pdvTipoPagamento = pdvTipoPagamento;
    }

    public VendaCondicoesPagamento getCondicao() {
        return condicao;
    }

    public void setCondicao(VendaCondicoesPagamento condicao) {
        this.condicao = condicao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PdvFormaPagamento)) return false;

        PdvFormaPagamento that = (PdvFormaPagamento) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
