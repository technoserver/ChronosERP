/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author john
 */
@Entity
@Table(name = "converter_cst")
public class ConverterCst implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "tipo_cst")
    private Integer tipoCst;
    @Column(name = "cst_origem")
    private String cstOrigem;
    @Column(name = "cst_destino")
    private String cstDestino;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTipoCst() {
        return tipoCst;
    }

    public void setTipoCst(Integer tipoCst) {
        this.tipoCst = tipoCst;
    }

    public String getCstOrigem() {
        return cstOrigem;
    }

    public void setCstOrigem(String cstOrigem) {
        this.cstOrigem = cstOrigem;
    }

    public String getCstDestino() {
        return cstDestino;
    }

    public void setCstDestino(String cstDestino) {
        this.cstDestino = cstDestino;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final ConverterCst other = (ConverterCst) obj;
        return Objects.equals(this.id, other.id);
    }


}
