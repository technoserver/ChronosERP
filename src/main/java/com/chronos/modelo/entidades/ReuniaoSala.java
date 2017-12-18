package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by john on 18/12/17.
 */
@Entity
@Table(name = "REUNIAO_SALA")
public class ReuniaoSala implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "PREDIO")
    private String predio;
    @Column(name = "ANDAR")
    private String andar;
    @Column(name = "NUMERO")
    private String numero;

    public ReuniaoSala() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPredio() {
        return predio;
    }

    public void setPredio(String predio) {
        this.predio = predio;
    }

    public String getAndar() {
        return andar;
    }

    public void setAndar(String andar) {
        this.andar = andar;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReuniaoSala)) return false;

        ReuniaoSala that = (ReuniaoSala) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
