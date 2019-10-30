
package com.chronos.erp.modelo.view;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Embeddable
public class ViewSpedC370 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "ID_NF_CABECALHO")
    private Integer idNfCabecalho;
    @Column(name = "DATA_EMISSAO")
    @Temporal(TemporalType.DATE)
    private Date dataEmissao;
    @Column(name = "ID_PRODUTO")
    private Integer idProduto;
    @Column(name = "ITEM")
    private Integer item;
    @Basic(optional = false)
    @Column(name = "ID_UNIDADE_PRODUTO")
    private Integer idUnidadeProduto;
    @Column(name = "QUANTIDADE")
    private BigDecimal quantidade;
    @Column(name = "VALOR_TOTAL")
    private BigDecimal valorTotal;
    @Column(name = "CST")
    private String cst;
    @Column(name = "DESCONTO")
    private BigDecimal desconto;

    public ViewSpedC370() {
    }

    public Integer getIdNfCabecalho() {
        return idNfCabecalho;
    }

    public void setIdNfCabecalho(Integer idNfCabecalho) {
        this.idNfCabecalho = idNfCabecalho;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Integer getIdUnidadeProduto() {
        return idUnidadeProduto;
    }

    public void setIdUnidadeProduto(Integer idUnidadeProduto) {
        this.idUnidadeProduto = idUnidadeProduto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getCst() {
        return cst;
    }

    public void setCst(String cst) {
        this.cst = cst;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewSpedC370)) return false;

        ViewSpedC370 that = (ViewSpedC370) o;

        if (getIdNfCabecalho() != null ? !getIdNfCabecalho().equals(that.getIdNfCabecalho()) : that.getIdNfCabecalho() != null)
            return false;
        if (getDataEmissao() != null ? !getDataEmissao().equals(that.getDataEmissao()) : that.getDataEmissao() != null)
            return false;
        if (getIdProduto() != null ? !getIdProduto().equals(that.getIdProduto()) : that.getIdProduto() != null)
            return false;
        if (getItem() != null ? !getItem().equals(that.getItem()) : that.getItem() != null) return false;
        if (getIdUnidadeProduto() != null ? !getIdUnidadeProduto().equals(that.getIdUnidadeProduto()) : that.getIdUnidadeProduto() != null)
            return false;
        if (getQuantidade() != null ? !getQuantidade().equals(that.getQuantidade()) : that.getQuantidade() != null)
            return false;
        if (getValorTotal() != null ? !getValorTotal().equals(that.getValorTotal()) : that.getValorTotal() != null)
            return false;
        if (getCst() != null ? !getCst().equals(that.getCst()) : that.getCst() != null) return false;
        return getDesconto() != null ? getDesconto().equals(that.getDesconto()) : that.getDesconto() == null;
    }

    @Override
    public int hashCode() {
        int result = getIdNfCabecalho() != null ? getIdNfCabecalho().hashCode() : 0;
        result = 31 * result + (getDataEmissao() != null ? getDataEmissao().hashCode() : 0);
        result = 31 * result + (getIdProduto() != null ? getIdProduto().hashCode() : 0);
        result = 31 * result + (getItem() != null ? getItem().hashCode() : 0);
        result = 31 * result + (getIdUnidadeProduto() != null ? getIdUnidadeProduto().hashCode() : 0);
        result = 31 * result + (getQuantidade() != null ? getQuantidade().hashCode() : 0);
        result = 31 * result + (getValorTotal() != null ? getValorTotal().hashCode() : 0);
        result = 31 * result + (getCst() != null ? getCst().hashCode() : 0);
        result = 31 * result + (getDesconto() != null ? getDesconto().hashCode() : 0);
        return result;
    }
}
