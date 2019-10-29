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
@Table(name = "mdfe_informacao_seguro")

public class MdfeInformacaoSeguro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(nullable = false)
    private int responsavel;
    @Column(name = "cnpj_cpf", length = 14)
    private String cnpjCpf;
    @Column(length = 11)
    private String seguradora;
    @Column(name = "cnpj_seguradora", length = 14)
    private String cnpjSeguradora;
    @Column(length = 20)
    private String apolice;
    @Column(length = 40)
    private String averbacao;
    @JoinColumn(name = "id_mdfe_cabecalho", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private MdfeCabecalho mdfeCabecalho;

    public MdfeInformacaoSeguro() {
    }

    public MdfeInformacaoSeguro(Integer id) {
        this.id = id;
    }

    public MdfeInformacaoSeguro(Integer id, int responsavel) {
        this.id = id;
        this.responsavel = responsavel;
    }

    @PrePersist
    @PreUpdate
    private void prePersit() {
        cnpjSeguradora = cnpjSeguradora != null ? cnpjSeguradora.replaceAll("\\D", "") : "";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(int responsavel) {
        this.responsavel = responsavel;
    }

    public String getCnpjCpf() {
        return cnpjCpf;
    }

    public void setCnpjCpf(String cnpjCpf) {
        this.cnpjCpf = cnpjCpf;
    }

    public String getSeguradora() {
        return seguradora;
    }

    public void setSeguradora(String seguradora) {
        this.seguradora = seguradora;
    }

    public String getCnpjSeguradora() {
        return cnpjSeguradora;
    }

    public void setCnpjSeguradora(String cnpjSeguradora) {
        this.cnpjSeguradora = cnpjSeguradora;
    }

    public String getApolice() {
        return apolice;
    }

    public void setApolice(String apolice) {
        this.apolice = apolice;
    }

    public String getAverbacao() {
        return averbacao;
    }

    public void setAverbacao(String averbacao) {
        this.averbacao = averbacao;
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
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof MdfeInformacaoSeguro)) {
            return false;
        }
        MdfeInformacaoSeguro other = (MdfeInformacaoSeguro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.MdfeInformacoesSeguro[ id=" + id + " ]";
    }

}
