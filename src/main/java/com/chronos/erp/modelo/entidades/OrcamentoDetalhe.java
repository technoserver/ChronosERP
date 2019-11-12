
package com.chronos.erp.modelo.entidades;


import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;


@Entity
@Table(name = "ORCAMENTO_DETALHE")
public class OrcamentoDetalhe implements Serializable {

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
    @NotNull
    private BigDecimal valorSubtotal;
    @Column(name = "TAXA_DESCONTO")
    @DecimalMax(value = "100.0", message = "O deve deve ser igual ou menor que 100")
    private BigDecimal taxaDesconto;
    @Column(name = "VALOR_DESCONTO")
    private BigDecimal valorDesconto;
    @Column(name = "VALOR_TOTAL")
    @NotNull
    private BigDecimal valorTotal;
    @JoinColumn(name = "ID_VENDA_ORCAMENTO_CABECALHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private OrcamentoCabecalho orcamentoCabecalho;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Produto produto;

    public OrcamentoDetalhe() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getQuantidade() {
        return Optional.ofNullable(quantidade).orElse(BigDecimal.ZERO);
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return Optional.ofNullable(valorUnitario).orElse(BigDecimal.ZERO);
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
        return Optional.ofNullable(taxaDesconto).orElse(BigDecimal.ZERO);
    }

    public void setTaxaDesconto(BigDecimal taxaDesconto) {
        this.taxaDesconto = taxaDesconto;
    }

    public BigDecimal getValorDesconto() {
        return Optional.ofNullable(valorDesconto).orElse(BigDecimal.ZERO);
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

    public OrcamentoCabecalho getOrcamentoCabecalho() {
        return orcamentoCabecalho;
    }

    public void setOrcamentoCabecalho(OrcamentoCabecalho orcamentoCabecalho) {
        this.orcamentoCabecalho = orcamentoCabecalho;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void calcularSubTotal() {
        valorSubtotal = getQuantidade().multiply(getValorUnitario());
    }

    public void calcularValorTotal() {
        valorTotal = getValorSubtotal().subtract(getValorDesconto());
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
        final OrcamentoDetalhe other = (OrcamentoDetalhe) obj;
        return Objects.equals(this.produto, other.produto);
    }


}
