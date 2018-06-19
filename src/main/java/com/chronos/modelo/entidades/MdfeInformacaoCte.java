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
@Table(name = "mdfe_informacao_cte")
public class MdfeInformacaoCte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "chave_cte", nullable = false, length = 44)
    private String chaveCte;
    @Column(name = "segundo_codigo_barra", length = 36)
    private String segundoCodigoBarras;
    @Column(name = "indicador_reentrega")
    private Integer indicadorReentrega;
    @JoinColumn(name = "id_mdfe_municipio_descarregamento", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private MdfeMunicipioDescarregamento mdfeMunicipioDescarregamento;

    public MdfeInformacaoCte() {
    }

    public MdfeInformacaoCte(Integer id) {
        this.id = id;
    }

    public MdfeInformacaoCte(Integer id, String chaveCte) {
        this.id = id;
        this.chaveCte = chaveCte;
    }

    public MdfeInformacaoCte(String chaveCte, Integer indicadorReentrega) {
        this.indicadorReentrega = indicadorReentrega;
        this.chaveCte = chaveCte;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChaveCte() {
        return chaveCte;
    }

    public void setChaveCte(String chaveCte) {
        this.chaveCte = chaveCte;
    }

    public String getSegundoCodigoBarras() {
        return segundoCodigoBarras;
    }

    public void setSegundoCodigoBarras(String segundoCodigoBarras) {
        this.segundoCodigoBarras = segundoCodigoBarras;
    }

    public Integer getIndicadorReentrega() {
        return indicadorReentrega;
    }

    public void setIndicadorReentrega(Integer indicadorReentrega) {
        this.indicadorReentrega = indicadorReentrega;
    }

    public MdfeMunicipioDescarregamento getMdfeMunicipioDescarregamento() {
        return mdfeMunicipioDescarregamento;
    }

    public void setMdfeMunicipioDescarregamento(MdfeMunicipioDescarregamento mdfeMunicipioDescarregamento) {
        this.mdfeMunicipioDescarregamento = mdfeMunicipioDescarregamento;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof MdfeInformacaoCte)) {
            return false;
        }
        MdfeInformacaoCte other = (MdfeInformacaoCte) object;
        if ((this.chaveCte == null && other.chaveCte != null)
                || (this.chaveCte != null && !this.chaveCte.equals(other.chaveCte))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.MdfeInformacaoCte[ id=" + id + " ]";
    }

}
