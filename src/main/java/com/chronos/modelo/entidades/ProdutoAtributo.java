package com.chronos.modelo.entidades;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "produto_atributo")
@DynamicUpdate
public class ProdutoAtributo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "sigla")
    @NotBlank
    private String sigla;
    @Column(name = "nome")
    @NotBlank
    private String nome;
    @OneToMany(mappedBy = "produtoAtributo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoAtributoDetalhe> listaProdutoAtributoDetalhe;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<ProdutoAtributoDetalhe> getListaProdutoAtributoDetalhe() {
        return listaProdutoAtributoDetalhe;
    }

    public void setListaProdutoAtributoDetalhe(List<ProdutoAtributoDetalhe> listaProdutoAtributoDetalhe) {
        this.listaProdutoAtributoDetalhe = listaProdutoAtributoDetalhe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProdutoAtributo)) return false;

        ProdutoAtributo that = (ProdutoAtributo) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
