/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.modelo.entidades;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author john
 */
@Entity
@Table(name = "mdfe_rodoviario_motorista")
public class MdfeRodoviarioMotorista implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Column(length = 60)
    private String nome;
    @Column(length = 11)
    @NotBlank
    private String cpf;
    @JoinColumn(name = "id_mdfe_rodoviario", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private MdfeRodoviario mdfeRodoviario;

    public MdfeRodoviarioMotorista() {
    }

    public MdfeRodoviarioMotorista(Integer id) {
        this.id = id;
    }

    @PreUpdate
    @PrePersist
    private void prePersit() {
        cpf = cpf != null ? cpf.replaceAll("\\D", "") : "";
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public MdfeRodoviario getMdfeRodoviario() {
        return mdfeRodoviario;
    }

    public void setMdfeRodoviario(MdfeRodoviario mdfeRodoviario) {
        this.mdfeRodoviario = mdfeRodoviario;
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
        if (!(object instanceof MdfeRodoviarioMotorista)) {
            return false;
        }
        MdfeRodoviarioMotorista other = (MdfeRodoviarioMotorista) object;
        if ((this.cpf == null && other.cpf != null) || (this.cpf != null && !this.cpf.equals(other.cpf))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.MdfeRodoviarioMotorista[ id=" + id + " ]";
    }

}
