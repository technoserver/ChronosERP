package com.chronos.erp.modelo.entidades;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "produto_atributo_detalhe")
@DynamicUpdate
public class ProdutoAtributoDetalhe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "nome")
    @NotBlank
    private String nome;
    @JoinColumn(name = "id_produto_atributo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @NotNull
    private ProdutoAtributo produtoAtributo;


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

    public ProdutoAtributo getProdutoAtributo() {
        return produtoAtributo;
    }

    public void setProdutoAtributo(ProdutoAtributo produtoAtributo) {
        this.produtoAtributo = produtoAtributo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProdutoAtributoDetalhe)) return false;

        ProdutoAtributoDetalhe that = (ProdutoAtributoDetalhe) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
