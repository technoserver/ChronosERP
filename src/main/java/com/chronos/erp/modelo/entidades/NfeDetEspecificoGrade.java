
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name = "NFE_DET_ESPECIFICO_GRADE")
public class NfeDetEspecificoGrade implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "QUANTIDADE")
    private BigDecimal quantidade;
    @JoinColumn(name = "ID_NFE_DETALHE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfeDetalhe nfeDetalhe;
    @JoinColumn(name = "ID_ESTOQUE_GRADE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EstoqueGrade estoqueGrade;

    public NfeDetEspecificoGrade() {
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

    public NfeDetalhe getNfeDetalhe() {
        return nfeDetalhe;
    }

    public void setNfeDetalhe(NfeDetalhe nfeDetalhe) {
        this.nfeDetalhe = nfeDetalhe;
    }

    public EstoqueGrade getEstoqueGrade() {
        return estoqueGrade;
    }

    public void setEstoqueGrade(EstoqueGrade estoqueGrade) {
        this.estoqueGrade = estoqueGrade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NfeDetEspecificoGrade that = (NfeDetEspecificoGrade) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(estoqueGrade, that.estoqueGrade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, estoqueGrade);
    }
}
