
package com.chronos.modelo.entidades.view;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Embeddable
public class ViewSpedC321 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "ID_PRODUTO")
    private Integer idProduto;
    @Column(name = "DESCRICAO_UNIDADE")
    private String descricaoUnidade;
    @Column(name = "DATA_EMISSAO")
    @Temporal(TemporalType.DATE)
    private Date dataEmissao;
    @Column(name = "SOMA_QUANTIDADE")
    private BigDecimal somaQuantidade;
    @Column(name = "SOMA_ITEM")
    private BigDecimal somaItem;
    @Column(name = "SOMA_DESCONTO")
    private BigDecimal somaDesconto;
    @Column(name = "SOMA_BASE_ICMS")
    private BigDecimal somaBaseIcms;
    @Column(name = "SOMA_ICMS")
    private BigDecimal somaIcms;
    @Column(name = "SOMA_PIS")
    private BigDecimal somaPis;
    @Column(name = "SOMA_COFINS")
    private BigDecimal somaCofins;

    public ViewSpedC321() {
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
    }

    public String getDescricaoUnidade() {
        return descricaoUnidade;
    }

    public void setDescricaoUnidade(String descricaoUnidade) {
        this.descricaoUnidade = descricaoUnidade;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public BigDecimal getSomaQuantidade() {
        return somaQuantidade;
    }

    public void setSomaQuantidade(BigDecimal somaQuantidade) {
        this.somaQuantidade = somaQuantidade;
    }

    public BigDecimal getSomaItem() {
        return somaItem;
    }

    public void setSomaItem(BigDecimal somaItem) {
        this.somaItem = somaItem;
    }

    public BigDecimal getSomaDesconto() {
        return somaDesconto;
    }

    public void setSomaDesconto(BigDecimal somaDesconto) {
        this.somaDesconto = somaDesconto;
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
        if (!(o instanceof ViewSpedC321)) return false;

        ViewSpedC321 that = (ViewSpedC321) o;

        if (getIdProduto() != null ? !getIdProduto().equals(that.getIdProduto()) : that.getIdProduto() != null)
            return false;
        if (getDescricaoUnidade() != null ? !getDescricaoUnidade().equals(that.getDescricaoUnidade()) : that.getDescricaoUnidade() != null)
            return false;
        if (getDataEmissao() != null ? !getDataEmissao().equals(that.getDataEmissao()) : that.getDataEmissao() != null)
            return false;
        if (getSomaQuantidade() != null ? !getSomaQuantidade().equals(that.getSomaQuantidade()) : that.getSomaQuantidade() != null)
            return false;
        if (getSomaItem() != null ? !getSomaItem().equals(that.getSomaItem()) : that.getSomaItem() != null)
            return false;
        if (getSomaDesconto() != null ? !getSomaDesconto().equals(that.getSomaDesconto()) : that.getSomaDesconto() != null)
            return false;
        if (getSomaBaseIcms() != null ? !getSomaBaseIcms().equals(that.getSomaBaseIcms()) : that.getSomaBaseIcms() != null)
            return false;
        if (getSomaIcms() != null ? !getSomaIcms().equals(that.getSomaIcms()) : that.getSomaIcms() != null)
            return false;
        if (getSomaPis() != null ? !getSomaPis().equals(that.getSomaPis()) : that.getSomaPis() != null) return false;
        return getSomaCofins() != null ? getSomaCofins().equals(that.getSomaCofins()) : that.getSomaCofins() == null;
    }

    @Override
    public int hashCode() {
        int result = getIdProduto() != null ? getIdProduto().hashCode() : 0;
        result = 31 * result + (getDescricaoUnidade() != null ? getDescricaoUnidade().hashCode() : 0);
        result = 31 * result + (getDataEmissao() != null ? getDataEmissao().hashCode() : 0);
        result = 31 * result + (getSomaQuantidade() != null ? getSomaQuantidade().hashCode() : 0);
        result = 31 * result + (getSomaItem() != null ? getSomaItem().hashCode() : 0);
        result = 31 * result + (getSomaDesconto() != null ? getSomaDesconto().hashCode() : 0);
        result = 31 * result + (getSomaBaseIcms() != null ? getSomaBaseIcms().hashCode() : 0);
        result = 31 * result + (getSomaIcms() != null ? getSomaIcms().hashCode() : 0);
        result = 31 * result + (getSomaPis() != null ? getSomaPis().hashCode() : 0);
        result = 31 * result + (getSomaCofins() != null ? getSomaCofins().hashCode() : 0);
        return result;
    }
}
