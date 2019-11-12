
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name = "FICHA_TECNICA")
public class FichaTecnica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "ID_PRODUTO_FILHO")
    private Integer idProdutoFilho;
    @Column(name = "QUANTIDADE")
    private BigDecimal quantidade;
    @Column(name = "SEQUENCIA_PRODUCAO")
    private Integer sequenciaProducao;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Produto produto;

    public FichaTecnica() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getIdProdutoFilho() {
        return idProdutoFilho;
    }

    public void setIdProdutoFilho(Integer idProdutoFilho) {
        this.idProdutoFilho = idProdutoFilho;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public Integer getSequenciaProducao() {
        return sequenciaProducao;
    }

    public void setSequenciaProducao(Integer sequenciaProducao) {
        this.sequenciaProducao = sequenciaProducao;
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
        if (o == null || getClass() != o.getClass()) return false;
        FichaTecnica that = (FichaTecnica) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
