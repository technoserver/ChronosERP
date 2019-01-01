package com.chronos.modelo.entidades;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "produto_grade_detalhe")
@DynamicUpdate
public class ProdutoGradeDetalhe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;

    @JoinColumn(name = "id_produto_atributo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @NotNull
    private ProdutoAtributo produtoAtributo;

    @JoinColumn(name = "id_produto_grade", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @NotNull
    private ProdutoGrade produtoGrade;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProdutoAtributo getProdutoAtributo() {
        return produtoAtributo;
    }

    public void setProdutoAtributo(ProdutoAtributo produtoAtributo) {
        this.produtoAtributo = produtoAtributo;
    }

    public ProdutoGrade getProdutoGrade() {
        return produtoGrade;
    }

    public void setProdutoGrade(ProdutoGrade produtoGrade) {
        this.produtoGrade = produtoGrade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProdutoGradeDetalhe)) return false;

        ProdutoGradeDetalhe that = (ProdutoGradeDetalhe) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
