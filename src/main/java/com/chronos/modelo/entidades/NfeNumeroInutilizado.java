/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "nfe_numero_inutilizado")
public class NfeNumeroInutilizado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "serie", length = 3)
    private String serie;
    @Column(name = "numero")
    private Integer numero;
    @Column(name = "data_inutilizacao")
    @Temporal(TemporalType.DATE)
    private Date dataInutilizacao;
    @Column(name = "observacao", length = 2147483647)
    private String observacao;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;

    public NfeNumeroInutilizado() {
    }

    public NfeNumeroInutilizado(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Date getDataInutilizacao() {
        return dataInutilizacao;
    }

    public void setDataInutilizacao(Date dataInutilizacao) {
        this.dataInutilizacao = dataInutilizacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
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
        if (!(object instanceof NfeNumeroInutilizado)) {
            return false;
        }
        NfeNumeroInutilizado other = (NfeNumeroInutilizado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "teste.NfeNumeroInutilizado[ id=" + id + " ]";
    }

}
