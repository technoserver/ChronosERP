
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

}
