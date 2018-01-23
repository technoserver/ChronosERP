
package com.chronos.modelo.entidades.view;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Embeddable
public class ViewSpedC390 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "CST")
    private String cst;
    @Basic(optional = false)
    @Column(name = "CFOP")
    private Integer cfop;
    @Column(name = "TAXA_ICMS")
    private BigDecimal taxaIcms;
    @Column(name = "DATA_EMISSAO")
    @Temporal(TemporalType.DATE)
    private Date dataEmissao;
    @Column(name = "SOMA_ITEM")
    private BigDecimal somaItem;
    @Column(name = "SOMA_BASE_ICMS")
    private BigDecimal somaBaseIcms;
    @Column(name = "SOMA_ICMS")
    private BigDecimal somaIcms;
    @Column(name = "SOMA_ICMS_OUTRAS")
    private BigDecimal somaIcmsOutras;

    public ViewSpedC390() {
    }

    public String getCst() {
        return cst;
    }

    public void setCst(String cst) {
        this.cst = cst;
    }

    public Integer getCfop() {
        return cfop;
    }

    public void setCfop(Integer cfop) {
        this.cfop = cfop;
    }

    public BigDecimal getTaxaIcms() {
        return taxaIcms;
    }

    public void setTaxaIcms(BigDecimal taxaIcms) {
        this.taxaIcms = taxaIcms;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public BigDecimal getSomaItem() {
        return somaItem;
    }

    public void setSomaItem(BigDecimal somaItem) {
        this.somaItem = somaItem;
    }

    public BigDecimal getSomaBaseIcms() {
        return somaBaseIcms;
    }

    public void setSomaBaseIcms(BigDecimal somaBaseIcms) {
        this.somaBaseIcms = somaBaseIcms;
    }

    public BigDecimal getSomaIcms() {
        return somaIcms;
    }

    public void setSomaIcms(BigDecimal somaIcms) {
        this.somaIcms = somaIcms;
    }

    public BigDecimal getSomaIcmsOutras() {
        return somaIcmsOutras;
    }

    public void setSomaIcmsOutras(BigDecimal somaIcmsOutras) {
        this.somaIcmsOutras = somaIcmsOutras;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewSpedC390)) return false;

        ViewSpedC390 that = (ViewSpedC390) o;

        if (getCst() != null ? !getCst().equals(that.getCst()) : that.getCst() != null) return false;
        return getCfop() != null ? getCfop().equals(that.getCfop()) : that.getCfop() == null;
    }

    @Override
    public int hashCode() {
        int result = getCst() != null ? getCst().hashCode() : 0;
        result = 31 * result + (getCfop() != null ? getCfop().hashCode() : 0);
        return result;
    }
}
