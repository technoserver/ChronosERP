package com.chronos.erp.modelo.entidades;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "SITUACAO_FOR_CLI")
public class SituacaoForCli implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @NotEmpty(message = "Nome obrigatório")
    @Column(name = "NOME")
    private String nome;
    @Column(name = "DESCRICAO")
    private String descricao;
    @NotEmpty()
    @Column(name = "bloquear")
    private String bloquear;

    public SituacaoForCli() {
    }

    public SituacaoForCli(Integer id) {
        this.id = id;
    }

    public SituacaoForCli(Integer id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public SituacaoForCli(String nome, String bloquear) {
        this.id = id;
        this.nome = nome;
        this.bloquear = bloquear;

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

    public String getBloquear() {
        return bloquear;
    }

    public void setBloquear(String bloquear) {
        this.bloquear = bloquear;
    }

    public String getDescBloquear() {
        return this.bloquear != null && this.bloquear.equals("S") ? "SIM" : "NAO";
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SituacaoForCli other = (SituacaoForCli) obj;
        return Objects.equals(this.id, other.id);
    }


}
