
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "NFE_FORMA_PAGAMENTO")
public class NfeFormaPagamento implements Serializable {

    private static final long serialVersionUID = 2L;
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
    @JoinColumn(name = "ID_NFE_CABECALHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfeCabecalho nfeCabecalho;
    @JoinColumn(name = "ID_PDV_TIPO_PAGAMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoPagamento tipoPagamento;

    public NfeFormaPagamento() {
        this.valor = BigDecimal.ZERO;
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

    public NfeCabecalho getNfeCabecalho() {
        return nfeCabecalho;
    }

    public void setNfeCabecalho(NfeCabecalho nfeCabecalho) {
        this.nfeCabecalho = nfeCabecalho;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }


}
