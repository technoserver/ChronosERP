
package com.chronos.erp.modelo.view;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Embeddable
public class ViewSpedC300 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "SERIE")
    private String serie;
    @Column(name = "SUBSERIE")
    private String subserie;
    @Column(name = "DATA_EMISSAO")
    @Temporal(TemporalType.DATE)
    private Date dataEmissao;
    @Column(name = "SOMA_TOTAL_NF")
    private BigDecimal somaTotalNf;
    @Column(name = "SOMA_PIS")
    private BigDecimal somaPis;
    @Column(name = "SOMA_COFINS")
    private BigDecimal somaCofins;

    public ViewSpedC300() {
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getSubserie() {
        return subserie;
    }

    public void setSubserie(String subserie) {
        this.subserie = subserie;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public BigDecimal getSomaTotalNf() {
        return somaTotalNf;
    }

    public void setSomaTotalNf(BigDecimal somaTotalNf) {
        this.somaTotalNf = somaTotalNf;
    }

    public BigDecimal getSomaPis() {
        return somaPis;
    }

    public void setSomaPis(BigDecimal somaPis) {
        this.somaPis = somaPis;
    }

    public BigDecimal getSomaCofins() {
        return somaCofins;
    }

    public void setSomaCofins(BigDecimal somaCofins) {
        this.somaCofins = somaCofins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewSpedC300)) return false;

        ViewSpedC300 that = (ViewSpedC300) o;

        if (getSerie() != null ? !getSerie().equals(that.getSerie()) : that.getSerie() != null) return false;
        if (getSubserie() != null ? !getSubserie().equals(that.getSubserie()) : that.getSubserie() != null)
            return false;
        if (getDataEmissao() != null ? !getDataEmissao().equals(that.getDataEmissao()) : that.getDataEmissao() != null)
            return false;
        if (getSomaTotalNf() != null ? !getSomaTotalNf().equals(that.getSomaTotalNf()) : that.getSomaTotalNf() != null)
            return false;
        if (getSomaPis() != null ? !getSomaPis().equals(that.getSomaPis()) : that.getSomaPis() != null) return false;
        return getSomaCofins() != null ? getSomaCofins().equals(that.getSomaCofins()) : that.getSomaCofins() == null;
    }

    @Override
    public int hashCode() {
        int result = getSerie() != null ? getSerie().hashCode() : 0;
        result = 31 * result + (getSubserie() != null ? getSubserie().hashCode() : 0);
        result = 31 * result + (getDataEmissao() != null ? getDataEmissao().hashCode() : 0);
        result = 31 * result + (getSomaTotalNf() != null ? getSomaTotalNf().hashCode() : 0);
        result = 31 * result + (getSomaPis() != null ? getSomaPis().hashCode() : 0);
        result = 31 * result + (getSomaCofins() != null ? getSomaCofins().hashCode() : 0);
        return result;
    }
}
