
package com.chronos.modelo.entidades;


import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name = "VENDA_ORCAMENTO_DETALHE")
public class VendaOrcamentoDetalhe implements Serializable {

    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @NotNull
    @Column(name = "QUANTIDADE")  
    @DecimalMin(value = "0.01", message = "A quantidade do produto  deve ser maior que 0,01")    
    private BigDecimal quantidade;
    @NotNull(message = "tste")
    @DecimalMin(value = "0.01", message = "O valor unitario do produto  deve ser maior que R$0,01")
    @DecimalMax(value = "9999999.99", message = "O valor unitario do produto  deve ser menor que R$9.999.999,99")
    @Column(name = "VALOR_UNITARIO")
    private BigDecimal valorUnitario;
    @Column(name = "VALOR_SUBTOTAL")
    private BigDecimal valorSubtotal;
    @Column(name = "TAXA_DESCONTO")
    @DecimalMax(value = "100.0", message = "O deve deve ser igual ou menor que 100")
    private BigDecimal taxaDesconto;
    @Column(name = "VALOR_DESCONTO")
    private BigDecimal valorDesconto;
    @Column(name = "VALOR_TOTAL")
    private BigDecimal valorTotal;
    @JoinColumn(name = "ID_VENDA_ORCAMENTO_CABECALHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private VendaOrcamentoCabecalho vendaOrcamentoCabecalho;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Produto produto;

    public VendaOrcamentoDetalhe() {
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

    public VendaOrcamentoCabecalho getVendaOrcamentoCabecalho() {
        return vendaOrcamentoCabecalho;
    }

    public void setVendaOrcamentoCabecalho(VendaOrcamentoCabecalho vendaOrcamentoCabecalho) {
        this.vendaOrcamentoCabecalho = vendaOrcamentoCabecalho;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.produto);
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
        final VendaOrcamentoDetalhe other = (VendaOrcamentoDetalhe) obj;
        if (!Objects.equals(this.produto, other.produto)) {
            return false;
        }
        return true;
    }

    

  

}
