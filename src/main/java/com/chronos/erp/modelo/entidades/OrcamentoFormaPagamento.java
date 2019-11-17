
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;


@Entity
@Table(name = "orcamento_forma_pagamento")
public class OrcamentoFormaPagamento implements Serializable {

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
    @JoinColumn(name = "id_orcamento_cabecalho", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private OrcamentoCabecalho orcamentoCabecalho;
    @JoinColumn(name = "ID_TIPO_PAGAMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoPagamento tipoPagamento;
    @JoinColumn(name = "ID_CONDICAO_PAGAMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CondicoesPagamento condicoesPagamento;
    @Transient
    private int qtdParcelas;

    @Transient
    private OperadoraCartao operadoraCartao;

    public OrcamentoFormaPagamento() {
        this.qtdParcelas = 1;
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

    public OrcamentoCabecalho getOrcamentoCabecalho() {
        return orcamentoCabecalho;
    }

    public void setOrcamentoCabecalho(OrcamentoCabecalho orcamentoCabecalho) {
        this.orcamentoCabecalho = orcamentoCabecalho;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public CondicoesPagamento getCondicoesPagamento() {
        return condicoesPagamento;
    }

    public void setCondicoesPagamento(CondicoesPagamento condicoesPagamento) {
        this.condicoesPagamento = condicoesPagamento;
    }

    public int getQtdParcelas() {
        return qtdParcelas;
    }

    public void setQtdParcelas(int qtdParcelas) {
        this.qtdParcelas = qtdParcelas;
    }

    public OperadoraCartao getOperadoraCartao() {
        return operadoraCartao;
    }

    public void setOperadoraCartao(OperadoraCartao operadoraCartao) {
        this.operadoraCartao = operadoraCartao;
    }


    public BigDecimal getValorTotal() {
        return this.valor.subtract(Optional.ofNullable(this.troco).orElse(BigDecimal.ZERO));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrcamentoFormaPagamento)) return false;

        OrcamentoFormaPagamento that = (OrcamentoFormaPagamento) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() != null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
