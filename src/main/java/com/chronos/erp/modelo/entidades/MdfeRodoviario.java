/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author john
 */
@Entity
@Table(name = "mdfe_rodoviario")
public class MdfeRodoviario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Column(length = 8)
    private String rntrc;
    @Column(name = "codigo_agendamento", length = 16)
    private String codigoAgendamento;
    @JoinColumn(name = "id_mdfe_cabecalho", referencedColumnName = "id", nullable = false)
    @OneToOne(optional = false)
    private MdfeCabecalho mdfeCabecalho;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "mdfeRodoviario", orphanRemoval = true)
    private Set<MdfeRodoviarioMotorista> listaMdfeRodoviarioMotorista;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "mdfeRodoviario", orphanRemoval = true)
    private Set<MdfeRodoviarioVeiculo> listaMdfeRodoviarioVeiculo;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "mdfeRodoviario", orphanRemoval = true)
    private Set<MdfeRodoviarioPedagio> listaMdfeRodoviarioPedagio;

    public MdfeRodoviario() {
        this.listaMdfeRodoviarioMotorista = new HashSet<>();
        this.listaMdfeRodoviarioVeiculo = new HashSet<>();
        this.listaMdfeRodoviarioPedagio = new HashSet<>();
    }

    public MdfeRodoviario(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRntrc() {
        return rntrc;
    }

    public void setRntrc(String rntrc) {
        this.rntrc = rntrc;
    }

    public String getCodigoAgendamento() {
        return codigoAgendamento;
    }

    public void setCodigoAgendamento(String codigoAgendamento) {
        this.codigoAgendamento = codigoAgendamento;
    }


    public MdfeCabecalho getMdfeCabecalho() {
        return mdfeCabecalho;
    }

    public void setMdfeCabecalho(MdfeCabecalho mdfeCabecalho) {
        this.mdfeCabecalho = mdfeCabecalho;
    }

    public Set<MdfeRodoviarioMotorista> getListaMdfeRodoviarioMotorista() {
        return listaMdfeRodoviarioMotorista;
    }

    public void setListaMdfeRodoviarioMotorista(Set<MdfeRodoviarioMotorista> listaMdfeRodoviarioMotorista) {
        this.listaMdfeRodoviarioMotorista = listaMdfeRodoviarioMotorista;
    }

    public Set<MdfeRodoviarioVeiculo> getListaMdfeRodoviarioVeiculo() {
        return listaMdfeRodoviarioVeiculo;
    }

    public void setListaMdfeRodoviarioVeiculo(Set<MdfeRodoviarioVeiculo> listaMdfeRodoviarioVeiculo) {
        this.listaMdfeRodoviarioVeiculo = listaMdfeRodoviarioVeiculo;
    }

    public Set<MdfeRodoviarioPedagio> getListaMdfeRodoviarioPedagio() {
        return listaMdfeRodoviarioPedagio;
    }

    public void setListaMdfeRodoviarioPedagio(Set<MdfeRodoviarioPedagio> listaMdfeRodoviarioPedagio) {
        this.listaMdfeRodoviarioPedagio = listaMdfeRodoviarioPedagio;
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
        if (!(object instanceof MdfeRodoviario)) {
            return false;
        }
        MdfeRodoviario other = (MdfeRodoviario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.MdfeRodoviario[ id=" + id + " ]";
    }

}
