package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by john on 18/12/17.
 */
@Entity
@Table(name = "AGENDA_CATEGORIA_COMPROMISSO")
public class AgendaCategoriaCompromisso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "COR")
    private String cor;

    public AgendaCategoriaCompromisso() {
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

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AgendaCategoriaCompromisso)) return false;

        AgendaCategoriaCompromisso that = (AgendaCategoriaCompromisso) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
