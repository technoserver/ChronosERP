/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author john
 */
@Entity
@Table(name = "converter_cfop")
public class ConverterCfop implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "cfop_origem")
    private Integer cfopOrigem;
    @Column(name = "cfop_destino")
    private Integer cfopDestino;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCfopOrigem() {
        return cfopOrigem;
    }

    public void setCfopOrigem(Integer cfopOrigem) {
        this.cfopOrigem = cfopOrigem;
    }

    public Integer getCfopDestino() {
        return cfopDestino;
    }

    public void setCfopDestino(Integer cfopDestino) {
        this.cfopDestino = cfopDestino;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
        final ConverterCfop other = (ConverterCfop) obj;
        return Objects.equals(this.id, other.id);
    }


}
