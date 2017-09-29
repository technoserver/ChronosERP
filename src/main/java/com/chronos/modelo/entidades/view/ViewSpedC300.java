
package com.chronos.modelo.entidades.view;

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

}
