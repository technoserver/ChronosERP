
package com.chronos.modelo.entidades.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class ViewFinFluxoCaixa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "ID_CONTA_CAIXA")
    private Integer idContaCaixa;
    @Column(name = "NOME_CONTA_CAIXA")
    private String nomeContaCaixa;
    @Column(name = "NOME_PESSOA")
    private String nomePessoa;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_LANCAMENTO")
    private Date dataLancamento;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_VENCIMENTO")
    private Date dataVencimento;
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Column(name = "CODIGO_SITUACAO")
    private String codigoSituacao;
    @Column(name = "DESCRICAO_SITUACAO")
    private String descricaoSituacao;
    @Column(name = "OPERACAO")
    private String operacao;

    public ViewFinFluxoCaixa() {
    }

    public Integer getIdContaCaixa() {
        return idContaCaixa;
    }

    public void setIdContaCaixa(Integer idContaCaixa) {
        this.idContaCaixa = idContaCaixa;
    }

    public String getNomeContaCaixa() {
        return nomeContaCaixa;
    }

    public void setNomeContaCaixa(String nomeContaCaixa) {
        this.nomeContaCaixa = nomeContaCaixa;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getCodigoSituacao() {
        return codigoSituacao;
    }

    public void setCodigoSituacao(String codigoSituacao) {
        this.codigoSituacao = codigoSituacao;
    }

    public String getDescricaoSituacao() {
        return descricaoSituacao;
    }

    public void setDescricaoSituacao(String descricaoSituacao) {
        this.descricaoSituacao = descricaoSituacao;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewFinFluxoCaixa)) return false;

        ViewFinFluxoCaixa that = (ViewFinFluxoCaixa) o;

        return getIdContaCaixa().equals(that.getIdContaCaixa());
    }

    @Override
    public int hashCode() {
        return getIdContaCaixa().hashCode();
    }
}
