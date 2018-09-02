package com.chronos.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "estoque_transferencia_detalhe")
public class EstoqueTransferenciaDetalhe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @JoinColumn(name = "id_produto", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    @NotNull
    private Produto produto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(precision = 18, scale = 6)
    private BigDecimal quantidade;
    @Column(length = 6)
    private String unidade;
    @Column(name = "valor_custo", precision = 18, scale = 6)
    private BigDecimal valorCusto;
    @Column(name = "valor_total", precision = 18, scale = 6)
    private BigDecimal valorTotal;
    @JoinColumn(name = "id_estoque_transferencia_cabecalho", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    @NotNull
    private EstoqueTransferenciaCabecalho estoqueTransferenciaCabecalho;

    public EstoqueTransferenciaDetalhe() {
    }

    public EstoqueTransferenciaDetalhe(Integer id) {
        this.id = id;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public BigDecimal getValorCusto() {
        return valorCusto;
    }

    public void setValorCusto(BigDecimal valorCusto) {
        this.valorCusto = valorCusto;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public EstoqueTransferenciaCabecalho getEstoqueTransferenciaCabecalho() {
        return estoqueTransferenciaCabecalho;
    }

    public void setEstoqueTransferenciaCabecalho(EstoqueTransferenciaCabecalho estoqueTransferenciaCabecalho) {
        this.estoqueTransferenciaCabecalho = estoqueTransferenciaCabecalho;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EstoqueTransferenciaDetalhe)) {
            return false;
        }
        EstoqueTransferenciaDetalhe other = (EstoqueTransferenciaDetalhe) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "model.EstoqueTransferenciaDetalhe[ id=" + id + " ]";
    }
}