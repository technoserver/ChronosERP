package com.chronos.erp.modelo.entidades;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produto_grade")
@DynamicUpdate
public class ProdutoGrade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "nome")
    @NotBlank
    private String nome;
    @OneToMany(mappedBy = "produtoGrade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoGradeDetalhe> listaProdutoGradeDetalhe;

    public ProdutoGrade() {
        this.listaProdutoGradeDetalhe = new ArrayList<>();
    }

    public ProdutoGrade(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<ProdutoGradeDetalhe> getListaProdutoGradeDetalhe() {
        return listaProdutoGradeDetalhe;
    }

    public void setListaProdutoGradeDetalhe(List<ProdutoGradeDetalhe> listaProdutoGradeDetalhe) {
        this.listaProdutoGradeDetalhe = listaProdutoGradeDetalhe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProdutoGrade)) return false;

        ProdutoGrade that = (ProdutoGrade) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
