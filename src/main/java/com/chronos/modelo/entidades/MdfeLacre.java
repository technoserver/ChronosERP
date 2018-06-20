/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author john
 */
@Entity
@Table(name = "mdfe_lacre")

public class MdfeLacre implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Column(name = "numero_lacre", length = 20)
    private String numeroLacre;
    @JoinColumn(name = "id_mdfe_cabecalho", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private MdfeCabecalho mdfeCabecalho;

    public MdfeLacre() {
    }

    public MdfeLacre(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroLacre() {
        return numeroLacre;
    }

    public void setNumeroLacre(String numeroLacre) {
        this.numeroLacre = numeroLacre;
    }

    public MdfeCabecalho getMdfeCabecalho() {
        return mdfeCabecalho;
    }

    public void setMdfeCabecalho(MdfeCabecalho mdfeCabecalho) {
        this.mdfeCabecalho = mdfeCabecalho;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MdfeLacre)) {
            return false;
        }
        MdfeLacre other = (MdfeLacre) object;
        if ((this.numeroLacre == null && other.numeroLacre != null) || (this.numeroLacre != null && !this.numeroLacre.equals(other.numeroLacre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.MdfeLacre[ id=" + id + " ]";
    }

}
