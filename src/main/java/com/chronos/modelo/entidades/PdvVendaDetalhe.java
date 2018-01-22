package com.chronos.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

/**
 * Created by john on 18/01/18.
 */
@Entity
@Table(name = "pdv_venda_detalhe")
public class PdvVendaDetalhe {

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
    @DecimalMax(value = "99.99", message = "O valor  deve ser menor que R$99,99")
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
    @JoinColumn(name = "ID_PDV_VENDA_CABECALHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private PdvVendaCabecalho pdvVendaCabecalho;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Produto produto;

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
        valorSubtotal = getQuantidade().multiply(getValorUnitario());
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
        valorDesconto = getTaxaDesconto().multiply(getValorSubtotal()).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getValorTotal() {
        valorTotal = getValorSubtotal().subtract(getValorDesconto());
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

    public PdvVendaCabecalho getPdvVendaCabecalho() {
        return pdvVendaCabecalho;
    }

    public void setPdvVendaCabecalho(PdvVendaCabecalho pdvVendaCabecalho) {
        this.pdvVendaCabecalho = pdvVendaCabecalho;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PdvVendaDetalhe)) return false;

        PdvVendaDetalhe that = (PdvVendaDetalhe) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
