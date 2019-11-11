
package com.chronos.erp.modelo.view;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Embeddable
public class ViewFinChequeNaoCompensado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "ID_CONTA_CAIXA")
    private Integer idContaCaixa;
    @Column(name = "NOME_CONTA_CAIXA")
    private String nomeContaCaixa;
    @Column(name = "TALAO")
    private String talao;
    @Column(name = "NUMERO_TALAO")
    private Integer numeroTalao;
    @Column(name = "NUMERO_CHEQUE")
    private Integer numeroCheque;
    @Column(name = "STATUS_CHEQUE")
    private String statusCheque;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_STATUS")
    private Date dataStatus;
    @Column(name = "VALOR")
    private BigDecimal valor;

    public ViewFinChequeNaoCompensado() {
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

    public String getTalao() {
        return talao;
    }

    public void setTalao(String talao) {
        this.talao = talao;
    }

    public Integer getNumeroTalao() {
        return numeroTalao;
    }

    public void setNumeroTalao(Integer numeroTalao) {
        this.numeroTalao = numeroTalao;
    }

    public Integer getNumeroCheque() {
        return numeroCheque;
    }

    public void setNumeroCheque(Integer numeroCheque) {
        this.numeroCheque = numeroCheque;
    }

    public String getStatusCheque() {
        return statusCheque;
    }

    public void setStatusCheque(String statusCheque) {
        this.statusCheque = statusCheque;
    }

    public Date getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Date dataStatus) {
        this.dataStatus = dataStatus;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewFinChequeNaoCompensado)) return false;

        ViewFinChequeNaoCompensado that = (ViewFinChequeNaoCompensado) o;

        if (getIdContaCaixa() != null ? !getIdContaCaixa().equals(that.getIdContaCaixa()) : that.getIdContaCaixa() != null)
            return false;
        if (getNumeroTalao() != null ? !getNumeroTalao().equals(that.getNumeroTalao()) : that.getNumeroTalao() != null)
            return false;
        return getNumeroCheque() != null ? getNumeroCheque().equals(that.getNumeroCheque()) : that.getNumeroCheque() == null;
    }

    @Override
    public int hashCode() {
        int result = getIdContaCaixa() != null ? getIdContaCaixa().hashCode() : 0;
        result = 31 * result + (getNumeroTalao() != null ? getNumeroTalao().hashCode() : 0);
        result = 31 * result + (getNumeroCheque() != null ? getNumeroCheque().hashCode() : 0);
        return result;
    }
}
