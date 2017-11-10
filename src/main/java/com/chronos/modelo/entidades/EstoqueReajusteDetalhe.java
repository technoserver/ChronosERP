package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "ESTOQUE_REAJUSTE_DETALHE")
public class EstoqueReajusteDetalhe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;


    @Column(name = "quantidade_original")
    private BigDecimal quantidadeOriginal;

    @Column(name = "quantidade_reajuste")
    private BigDecimal quantidadeReajuste;

    @Column(name = "VALOR_ORIGINAL")
    private BigDecimal valorOriginal;
    @Column(name = "VALOR_REAJUSTE")
    private BigDecimal valorReajuste;
    @JoinColumn(name = "ID_ESTOQUE_REAJUSTE_CABECALHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EstoqueReajusteCabecalho estoqueReajusteCabecalho;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Produto produto;

    public EstoqueReajusteDetalhe() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getValorOriginal() {
        return valorOriginal;
    }

    public void setValorOriginal(BigDecimal valorOriginal) {
        this.valorOriginal = valorOriginal;
    }

    public BigDecimal getValorReajuste() {
        return valorReajuste;
    }

    public void setValorReajuste(BigDecimal valorReajuste) {
        this.valorReajuste = valorReajuste;
    }

    public EstoqueReajusteCabecalho getEstoqueReajusteCabecalho() {
        return estoqueReajusteCabecalho;
    }

    public void setEstoqueReajusteCabecalho(EstoqueReajusteCabecalho estoqueReajusteCabecalho) {
        this.estoqueReajusteCabecalho = estoqueReajusteCabecalho;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getQuantidadeOriginal() {
        return quantidadeOriginal;
    }

    public void setQuantidadeOriginal(BigDecimal quantidadeOriginal) {
        this.quantidadeOriginal = quantidadeOriginal;
    }

    public BigDecimal getQuantidadeReajuste() {
        return quantidadeReajuste;
    }

    public void setQuantidadeReajuste(BigDecimal quantidadeReajuste) {
        this.quantidadeReajuste = quantidadeReajuste;
    }

    @Override
    public String toString() {
        return "EstoqueReajusteDetalhe{" + "id=" + id + '}';
    }

}
