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
@Table(name = "mdfe_municipio_carregamento")
public class MdfeMunicipioCarregamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Column(name = "nome_municipio", length = 60)
    private String nomeMunicipio;
    @Column(name = "codigo_municipio", length = 7)
    private String codigoMunicipio;
    @JoinColumn(name = "id_mdfe_cabecalho", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private MdfeCabecalho mdfeCabecalho;

    public MdfeMunicipioCarregamento() {
    }

    public MdfeMunicipioCarregamento(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeMunicipio() {
        return nomeMunicipio;
    }

    public void setNomeMunicipio(String nomeMunicipio) {
        this.nomeMunicipio = nomeMunicipio;
    }

    public String getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(String codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
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
        if (!(object instanceof MdfeMunicipioCarregamento)) {
            return false;
        }
        MdfeMunicipioCarregamento other = (MdfeMunicipioCarregamento) object;
        if ((this.codigoMunicipio == null && other.codigoMunicipio != null) || (this.codigoMunicipio != null && !this.codigoMunicipio.equals(other.codigoMunicipio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.MdfeMunicipioCarregamento[ id=" + id + " ]";
    }

}
