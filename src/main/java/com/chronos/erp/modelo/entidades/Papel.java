package com.chronos.erp.modelo.entidades;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "PAPEL")
public class Papel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "ACESSO_COMPLETO")
    @NotBlank
    private String acessoCompleto;
    @OneToMany(mappedBy = "papel", fetch = FetchType.LAZY)
    private List<PapelFuncao> listaPapelFuncao;

    public Papel() {
    }

    public Papel(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Papel(Integer id, String nome, String acessoCompleto) {
        this.id = id;
        this.nome = nome;
        this.acessoCompleto = acessoCompleto;
    }

    public Papel(Integer id) {
        this.id = id;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAcessoCompleto() {
        return acessoCompleto;
    }

    public void setAcessoCompleto(String acessoCompleto) {
        this.acessoCompleto = acessoCompleto;
    }

    public List<PapelFuncao> getListaPapelFuncao() {
        return listaPapelFuncao;
    }

    public void setListaPapelFuncao(List<PapelFuncao> listaPapelFuncao) {
        this.listaPapelFuncao = listaPapelFuncao;
    }


    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final Papel other = (Papel) obj;
        return Objects.equals(this.id, other.id);
    }


}
