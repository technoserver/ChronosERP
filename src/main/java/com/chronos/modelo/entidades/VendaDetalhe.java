package com.chronos.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "VENDA_DETALHE")
public class VendaDetalhe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "QUANTIDADE")
    @NotNull
    @DecimalMin(value = "0.01", message = "a quantidade deve ser maior que 0,01")
    private BigDecimal quantidade;
    @NotNull
    @Column(name = "VALOR_UNITARIO")
    @DecimalMin(value = "0.01", message = "O valor  deve ser maior que R$0,01")
    private BigDecimal valorUnitario;
    @Column(name = "VALOR_SUBTOTAL")
    private BigDecimal valorSubtotal;
    @Column(name = "TAXA_DESCONTO")
    private BigDecimal taxaDesconto;
    @Column(name = "VALOR_DESCONTO")
    private BigDecimal valorDesconto;
    @Column(name = "VALOR_TOTAL")
    private BigDecimal valorTotal;
    @Column(name = "TAXA_COMISSAO")
    @DecimalMax(value = "100.0", message = "O deve deve ser igual ou menor que 100")
    private BigDecimal taxaComissao;
    @Column(name = "VALOR_COMISSAO")
    private BigDecimal valorComissao;
    @JoinColumn(name = "ID_VENDA_CABECALHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private VendaCabecalho vendaCabecalho;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Produto produto;

    public VendaDetalhe() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getValorSubtotal() {
        return valorSubtotal;
    }

    public void setValorSubtotal(BigDecimal valorSubtotal) {
        this.valorSubtotal = valorSubtotal;
    }

    public BigDecimal getTaxaDesconto() {
        return taxaDesconto;
    }

    public void setTaxaDesconto(BigDecimal taxaDesconto) {
        this.taxaDesconto = taxaDesconto;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getTaxaComissao() {
        return taxaComissao;
    }

    public void setTaxaComissao(BigDecimal taxaComissao) {
        this.taxaComissao = taxaComissao;
    }

    public BigDecimal getValorComissao() {
        return valorComissao;
    }

    public void setValorComissao(BigDecimal valorComissao) {
        this.valorComissao = valorComissao;
    }

    public VendaCabecalho getVendaCabecalho() {
        return vendaCabecalho;
    }

    public void setVendaCabecalho(VendaCabecalho vendaCabecalho) {
        this.vendaCabecalho = vendaCabecalho;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.produto);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VendaDetalhe other = (VendaDetalhe) obj;
        if (!Objects.equals(this.produto, other.produto)) {
            return false;
        }
        return true;
    }

    
}
