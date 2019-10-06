package com.chronos.modelo.entidades;

import com.chronos.modelo.anotacoes.TaxaMaior;
import com.chronos.util.Biblioteca;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "venda_consignada_detalhe")
public class VendaConsignadaDetalhe implements Serializable {

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
    @Column(name = "quantidade_vendida")
    private BigDecimal quantidadeVendida;
    @Column(name = "quantidade_devolvida")
    private BigDecimal quantidadeDevolvida;
    @Column(name = "un")
    private String un;
    @NotNull
    @Column(name = "VALOR_UNITARIO")
    @DecimalMin(value = "0.01", message = "O valor  deve ser maior que R$0,01")
    private BigDecimal valorUnitario;
    @Column(name = "VALOR_SUBTOTAL")
    private BigDecimal valorSubtotal;
    @Column(name = "TAXA_DESCONTO")
    @DecimalMax(value = "99.99", message = "O valor  deve ser menor que R$99,99")
    @TaxaMaior
    private BigDecimal taxaDesconto;
    @Column(name = "VALOR_DESCONTO")
    private BigDecimal valorDesconto;
    @Column(name = "VALOR_TOTAL")
    private BigDecimal valorTotal;
    @JoinColumn(name = "id_venda_consignada_cabecalho", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private VendaConsignadaCabecalho vendaConsignadaCabecalho;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Produto produto;

    private StatusConsignacao status;

    public VendaConsignadaDetalhe() {
        this.valorSubtotal = BigDecimal.ZERO;
        this.valorDesconto = BigDecimal.ZERO;
        this.quantidade = BigDecimal.ONE;
        this.quantidadeDevolvida = BigDecimal.ZERO;
        this.quantidadeVendida = BigDecimal.ZERO;
        this.taxaDesconto = BigDecimal.ZERO;
        this.valorDesconto = BigDecimal.ZERO;
        this.status = StatusConsignacao.EDICAO;
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

    public BigDecimal getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public void setQuantidadeVendida(BigDecimal quantidadeVendida) {
        this.quantidadeVendida = quantidadeVendida;
    }

    public BigDecimal getQuantidadeDevolvida() {
        return quantidadeDevolvida;
    }

    public void setQuantidadeDevolvida(BigDecimal quantidadeDevolvida) {
        this.quantidadeDevolvida = quantidadeDevolvida;
    }

    public String getUn() {
        return un;
    }

    public void setUn(String un) {
        this.un = un;
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

    public VendaConsignadaCabecalho getVendaConsignadaCabecalho() {
        return vendaConsignadaCabecalho;
    }

    public void setVendaConsignadaCabecalho(VendaConsignadaCabecalho vendaConsignadaCabecalho) {
        this.vendaConsignadaCabecalho = vendaConsignadaCabecalho;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public StatusConsignacao getStatus() {
        return status;
    }

    public void setStatus(StatusConsignacao status) {
        this.status = status;
    }

    public void calcularValorSubtotal() {
        valorSubtotal = Biblioteca.multiplica(quantidade, valorUnitario);
    }


    public void calcularValorTotal() {
        valorTotal = Biblioteca.subtrai(valorSubtotal, valorDesconto);
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
        final VendaConsignadaDetalhe other = (VendaConsignadaDetalhe) obj;
        return Objects.equals(this.produto, other.produto);
    }


}
