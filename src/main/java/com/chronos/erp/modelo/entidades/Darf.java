
package com.chronos.erp.modelo.entidades;

import java.math.BigDecimal;
import java.util.Date;

public class Darf {

    private String razaoSocial;
    private String telefone;
    private String periodoApuracao;
    private String cpfCnpj;
    private String codigoReceita;
    private String numeroReferencia;
    private Date dataVencimento;
    private BigDecimal valorPrincipal;
    private BigDecimal valorMulta;
    private BigDecimal valorJuros;
    private BigDecimal valorTotal;

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getPeriodoApuracao() {
        return periodoApuracao;
    }

    public void setPeriodoApuracao(String periodoApuracao) {
        this.periodoApuracao = periodoApuracao;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getCodigoReceita() {
        return codigoReceita;
    }

    public void setCodigoReceita(String codigoReceita) {
        this.codigoReceita = codigoReceita;
    }

    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public BigDecimal getValorPrincipal() {
        return valorPrincipal;
    }

    public void setValorPrincipal(BigDecimal valorPrincipal) {
        this.valorPrincipal = valorPrincipal;
    }

    public BigDecimal getValorMulta() {
        return valorMulta;
    }

    public void setValorMulta(BigDecimal valorMulta) {
        this.valorMulta = valorMulta;
    }

    public BigDecimal getValorJuros() {
        return valorJuros;
    }

    public void setValorJuros(BigDecimal valorJuros) {
        this.valorJuros = valorJuros;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

}
