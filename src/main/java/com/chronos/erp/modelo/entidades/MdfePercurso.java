/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author john
 */
@Entity
@Table(name = "mdfe_percurso")

public class MdfePercurso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "uf_percurso", nullable = false, length = 2)
    private String ufPercurso;
    @JoinColumn(name = "id_mdfe_cabecalho", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private MdfeCabecalho mdfeCabecalho;

    public MdfePercurso() {
    }

    public MdfePercurso(Integer id) {
        this.id = id;
    }

    public MdfePercurso(Integer id, String ufPercurso) {
        this.id = id;
        this.ufPercurso = ufPercurso;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUfPercurso() {
        return ufPercurso;
    }

    public void setUfPercurso(String ufPercurso) {
        this.ufPercurso = ufPercurso;
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
        if (!(object instanceof MdfePercurso)) {
            return false;
        }
        MdfePercurso other = (MdfePercurso) object;
        if ((this.ufPercurso == null && other.ufPercurso != null) || (this.ufPercurso != null && !this.ufPercurso.equals(other.ufPercurso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.MdfePercurso[ id=" + id + " ]";
    }

}
