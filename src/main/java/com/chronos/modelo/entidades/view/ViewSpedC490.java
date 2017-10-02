
package com.chronos.modelo.entidades.view;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Embeddable
public class ViewSpedC490 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "CST")
    private String cst;
    @Basic(optional = false)
    @Column(name = "CFOP")
    private Integer cfop;
    @Column(name = "TAXA_ICMS")
    private BigDecimal taxaIcms;
    @Column(name = "DATA_VENDA")
    @Temporal(TemporalType.DATE)
    private Date dataVenda;
    @Column(name = "SOMA_ITEM")
    private BigDecimal somaItem;
    @Column(name = "SOMA_BASE_ICMS")
    private BigDecimal somaBaseIcms;
    @Column(name = "SOMA_ICMS")
    private BigDecimal somaIcms;

    public ViewSpedC490() {
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

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
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
}
