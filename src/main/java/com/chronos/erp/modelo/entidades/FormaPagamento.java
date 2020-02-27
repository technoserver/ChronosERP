
package com.chronos.erp.modelo.entidades;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;


@Embeddable
public class FormaPagamento implements Serializable {

    private static final long serialVersionUID = 2L;

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
    @JoinColumn(name = "ID_TIPO_PAGAMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoPagamento tipoPagamento;
    @Column(name = "QTD_PARCELA")
    private Integer qtdParcelas;
    @JoinColumn(name = "ID_CONDICAO_PAGAMENTO", referencedColumnName = "ID")
    @ManyToOne
    private CondicoesPagamento condicoesPagamento;

    public FormaPagamento() {
        this.qtdParcelas = 1;
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

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public int getQtdParcelas() {
        return qtdParcelas;
    }

    public void setQtdParcelas(int qtdParcelas) {
        this.qtdParcelas = qtdParcelas;
    }

    public CondicoesPagamento getCondicoesPagamento() {
        return condicoesPagamento;
    }

    public void setCondicoesPagamento(CondicoesPagamento condicoesPagamento) {
        this.condicoesPagamento = condicoesPagamento;
    }

    public BigDecimal getValorTotal() {
        return this.valor.subtract(Optional.ofNullable(this.troco).orElse(BigDecimal.ZERO));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormaPagamento that = (FormaPagamento) o;
        return qtdParcelas == that.qtdParcelas &&
                Objects.equals(forma, that.forma) &&
                Objects.equals(valor, that.valor) &&
                Objects.equals(cartaoTipoIntegracao, that.cartaoTipoIntegracao) &&
                Objects.equals(cnpjOperadoraCartao, that.cnpjOperadoraCartao) &&
                Objects.equals(bandeira, that.bandeira) &&
                Objects.equals(numeroAutorizacao, that.numeroAutorizacao) &&
                Objects.equals(estorno, that.estorno) &&
                Objects.equals(troco, that.troco) &&
                Objects.equals(tipoPagamento, that.tipoPagamento) &&
                Objects.equals(condicoesPagamento, that.condicoesPagamento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(forma, valor, cartaoTipoIntegracao, cnpjOperadoraCartao, bandeira, numeroAutorizacao, estorno, troco, tipoPagamento, qtdParcelas, condicoesPagamento);
    }
}
