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
@Table(name = "MDFE_XML")
public class MdfeXml implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic
    @Column(name = "xml")
    private byte[] xml;
    @JoinColumn(name = "ID_MDFE_CABECALHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private MdfeCabecalho mdfeCabecalho;

    public MdfeXml() {
    }

    public MdfeXml(Integer id) {
        this.id = id;
    }

    public MdfeXml(Integer id, byte[] xml) {
        this.id = id;
        this.xml = xml;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getXml() {
        return xml;
    }

    public void setXml(byte[] xml) {
        this.xml = xml;
    }

    public MdfeCabecalho getMdfeCabecalho() {
        return mdfeCabecalho;
    }

    public void setMdfeCabecalho(MdfeCabecalho mdfeCabecalho) {
        this.mdfeCabecalho = mdfeCabecalho;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final MdfeXml other = (MdfeXml) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
