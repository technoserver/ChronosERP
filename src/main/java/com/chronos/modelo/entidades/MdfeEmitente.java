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
@Table(name = "mdfe_emitente")

public class MdfeEmitente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Column(length = 60)
    private String nome;
    @Column(length = 60)
    private String fantasia;
    @Column(length = 14)
    private String cnpj;
    @Basic(optional = false)
    @Column(nullable = false)
    private String ie;
    @Column(length = 60)
    private String logradouro;
    @Column(length = 60)
    private String numero;
    @Column(length = 60)
    private String complemento;
    @Column(length = 60)
    private String bairro;
    @Column(name = "codigo_municipio", length = 7)
    private String codigoMunicipio;
    @Column(name = "nome_municipio", length = 60)
    private String nomeMunicipio;
    @Column(length = 8)
    private String cep;
    @Column(length = 2)
    private String uf;
    @Column(length = 12)
    private String telefone;
    @Column(length = 60)
    private String email;
    @JoinColumn(name = "id_mdfe_cabecalho", referencedColumnName = "id", nullable = false)
    @OneToOne(optional = false)
    private MdfeCabecalho mdfeCabecalho;

    public MdfeEmitente() {
    }

    public MdfeEmitente(Integer id) {
        this.id = id;
    }

    public MdfeEmitente(Integer id, String ie) {
        this.id = id;
        this.ie = ie;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getIe() {
        return ie;
    }

    public void setIe(String ie) {
        this.ie = ie;
    }


    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(String codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public String getNomeMunicipio() {
        return nomeMunicipio;
    }

    public void setNomeMunicipio(String nomeMunicipio) {
        this.nomeMunicipio = nomeMunicipio;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        if (!(object instanceof MdfeEmitente)) {
            return false;
        }
        MdfeEmitente other = (MdfeEmitente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.MdfeEmitente[ id=" + id + " ]";
    }

}
