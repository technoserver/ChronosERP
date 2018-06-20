/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @author john
 */
@Entity
@Table(name = "mdfe_municipio_descarregamento")
public class MdfeMunicipioDescarregamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nome_municipio", nullable = false, length = 60)
    private String nomeMunicipio;
    @Basic(optional = false)
    @Column(name = "codigo_municipio", nullable = false, length = 7)
    private String codigoMunicipio;
    @JoinColumn(name = "id_mdfe_cabecalho", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    @NotNull
    private MdfeCabecalho mdfeCabecalho;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "mdfeMunicipioDescarregamento", orphanRemoval = true)
    private Set<MdfeInformacaoNfe> listaMdfeInformacaoNfe;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "mdfeMunicipioDescarregamento", orphanRemoval = true)
    private Set<MdfeInformacaoCte> listaMdfeInformacaoCte;

    public MdfeMunicipioDescarregamento() {
    }

    public MdfeMunicipioDescarregamento(Integer id) {
        this.id = id;
    }

    public MdfeMunicipioDescarregamento(Integer id, String nomeMunicipio, String codigoMinicipio) {
        this.id = id;
        this.nomeMunicipio = nomeMunicipio;
        this.codigoMunicipio = codigoMinicipio;
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

    public Set<MdfeInformacaoNfe> getListaMdfeInformacaoNfe() {
        return listaMdfeInformacaoNfe;
    }

    public void setListaMdfeInformacaoNfe(Set<MdfeInformacaoNfe> listaMdfeInformacaoNfe) {
        this.listaMdfeInformacaoNfe = listaMdfeInformacaoNfe;
    }

    public Set<MdfeInformacaoCte> getListaMdfeInformacaoCte() {
        return listaMdfeInformacaoCte;
    }

    public void setListaMdfeInformacaoCte(Set<MdfeInformacaoCte> listaMdfeInformacaoCte) {
        this.listaMdfeInformacaoCte = listaMdfeInformacaoCte;
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
        if (!(object instanceof MdfeMunicipioDescarregamento)) {
            return false;
        }
        MdfeMunicipioDescarregamento other = (MdfeMunicipioDescarregamento) object;
        if ((this.codigoMunicipio == null && other.codigoMunicipio != null) || (this.codigoMunicipio != null && !this.codigoMunicipio.equals(other.codigoMunicipio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.MdfeMunicipioDescarregamento[ id=" + id + " ]";
    }

}
