package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "venda_devolucao_item")
public class VendaDevolucaoItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "quantidade")
    @NotNull
    private BigDecimal quantidade;

    @Column(name = "valor")
    @NotNull
    private BigDecimal valor;

    @JoinColumn(name = "id_venda_devolucao", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @NotNull
    private VendaDevolucao vendaDevolucao;

    @JoinColumn(name = "id_produto", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @NotNull
    private Produto produto;
    @Transient
    private BigDecimal quantidadeVenda;

    public VendaDevolucaoItem() {
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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public VendaDevolucao getVendaDevolucao() {
        return vendaDevolucao;
    }

    public void setVendaDevolucao(VendaDevolucao vendaDevolucao) {
        this.vendaDevolucao = vendaDevolucao;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getQuantidadeVenda() {
        return quantidadeVenda;
    }

    public void setQuantidadeVenda(BigDecimal quantidadeVenda) {
        this.quantidadeVenda = quantidadeVenda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VendaDevolucaoItem that = (VendaDevolucaoItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
