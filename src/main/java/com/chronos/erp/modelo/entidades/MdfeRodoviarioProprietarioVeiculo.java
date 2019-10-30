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
@Table(name = "mdfe_rodoviario_proprietario_veiculo")
public class MdfeRodoviarioProprietarioVeiculo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Column(name = "cpf_cnpj", length = 14)
    private String cpfCnpj;
    @Column(name = "rntrc", length = 8)
    private String rntrc;
    @Column(name = "nome", length = 60)
    private String nome;
    @Column(name = "ie", length = 14)
    private String ie;
    @Column(name = "uf", length = 2)
    private String uf;
    @Column(name = "tipo")
    private Integer tipo;
    @JoinColumn(name = "id_mdfe_rodoviario_veiculo", referencedColumnName = "id", nullable = false)
    @OneToOne(optional = false)
    private MdfeRodoviarioVeiculo mdfeRodoviarioVeiculo;

    public MdfeRodoviarioProprietarioVeiculo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getRntrc() {
        return rntrc;
    }

    public void setRntrc(String rntrc) {
        this.rntrc = rntrc;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIe() {
        return ie;
    }

    public void setIe(String ie) {
        this.ie = ie;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public MdfeRodoviarioVeiculo getMdfeRodoviarioVeiculo() {
        return mdfeRodoviarioVeiculo;
    }

    public void setMdfeRodoviarioVeiculo(MdfeRodoviarioVeiculo mdfeRodoviarioVeiculo) {
        this.mdfeRodoviarioVeiculo = mdfeRodoviarioVeiculo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MdfeRodoviarioProprietarioVeiculo)) return false;

        MdfeRodoviarioProprietarioVeiculo that = (MdfeRodoviarioProprietarioVeiculo) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "javaapplication1.MdfeRodoviarioVeiculo[ id=" + id + " ]";
    }
}
