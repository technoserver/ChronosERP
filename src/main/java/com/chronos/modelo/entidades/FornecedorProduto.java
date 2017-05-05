
package com.chronos.modelo.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



@Entity
@Table(name = "FORNECEDOR_PRODUTO")
public class FornecedorProduto  implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CODIGO_FORNECEDOR_PRODUTO")
    private String codigoFornecedorProduto;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_ULTIMA_COMPRA")
    private Date dataUltimaCompra;
    @Column(name = "PRECO_ULTIMA_COMPRA")
    private BigDecimal precoUltimaCompra;
    @Column(name = "LOTE_MINIMO_COMPRA")
    private BigDecimal loteMinimoCompra;
    @Column(name = "MENOR_EMBALAGEM_COMPRA")
    private BigDecimal menorEmbalagemCompra;
    @Column(name = "CUSTO_ULTIMA_COMPRA")
    private BigDecimal custoUltimaCompra;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Produto produto;
    @JoinColumn(name = "ID_FORNECEDOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Fornecedor fornecedor;

    public FornecedorProduto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoFornecedorProduto() {
        return codigoFornecedorProduto;
    }

    public void setCodigoFornecedorProduto(String codigoFornecedorProduto) {
        this.codigoFornecedorProduto = codigoFornecedorProduto;
    }

    public Date getDataUltimaCompra() {
        return dataUltimaCompra;
    }

    public void setDataUltimaCompra(Date dataUltimaCompra) {
        this.dataUltimaCompra = dataUltimaCompra;
    }

    public BigDecimal getPrecoUltimaCompra() {
        return precoUltimaCompra;
    }

    public void setPrecoUltimaCompra(BigDecimal precoUltimaCompra) {
        this.precoUltimaCompra = precoUltimaCompra;
    }

    public BigDecimal getLoteMinimoCompra() {
        return loteMinimoCompra;
    }

    public void setLoteMinimoCompra(BigDecimal loteMinimoCompra) {
        this.loteMinimoCompra = loteMinimoCompra;
    }

    public BigDecimal getMenorEmbalagemCompra() {
        return menorEmbalagemCompra;
    }

    public void setMenorEmbalagemCompra(BigDecimal menorEmbalagemCompra) {
        this.menorEmbalagemCompra = menorEmbalagemCompra;
    }

    public BigDecimal getCustoUltimaCompra() {
        return custoUltimaCompra;
    }

    public void setCustoUltimaCompra(BigDecimal custoUltimaCompra) {
        this.custoUltimaCompra = custoUltimaCompra;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }


    @Override
    public String toString() {
        return "com.t2tierp.cadastros.java.FornecedorProdutoVO[id=" + id + "]";
    }

}
