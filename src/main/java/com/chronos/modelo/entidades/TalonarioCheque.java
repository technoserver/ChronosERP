
package com.chronos.modelo.entidades;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "TALONARIO_CHEQUE")
public class TalonarioCheque implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "TALAO")
    @NotEmpty(message = "Talão Obrigatório")
    private String talao;
    @Column(name = "NUMERO")
    private Integer numero;
    @Column(name = "STATUS_TALAO")
    private String statusTalao;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @Valid
    @NotNull(message = "Empresa Obrigatória")
    private Empresa empresa;
    @JoinColumn(name = "ID_CONTA_CAIXA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @Valid
    @NotNull(message = "Conta Obrigatoria")
    private ContaCaixa contaCaixa;

    public TalonarioCheque() {
    }

    public TalonarioCheque(Integer id, String talao) {
        this.id = id;
        this.talao = talao;
    }
    
    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTalao() {
        return talao;
    }

    public void setTalao(String talao) {
        this.talao = talao;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getStatusTalao() {
        return statusTalao;
    }

    public void setStatusTalao(String statusTalao) {
        this.statusTalao = statusTalao;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public ContaCaixa getContaCaixa() {
        return contaCaixa;
    }

    public void setContaCaixa(ContaCaixa contaCaixa) {
        this.contaCaixa = contaCaixa;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.id);
        hash = 83 * hash + Objects.hashCode(this.talao);
        hash = 83 * hash + Objects.hashCode(this.numero);
        hash = 83 * hash + Objects.hashCode(this.statusTalao);
        hash = 83 * hash + Objects.hashCode(this.empresa);
        hash = 83 * hash + Objects.hashCode(this.contaCaixa);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TalonarioCheque other = (TalonarioCheque) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.talao, other.talao)) {
            return false;
        }
        if (!Objects.equals(this.numero, other.numero)) {
            return false;
        }
        if (!Objects.equals(this.statusTalao, other.statusTalao)) {
            return false;
        }
        if (!Objects.equals(this.empresa, other.empresa)) {
            return false;
        }
        if (!Objects.equals(this.contaCaixa, other.contaCaixa)) {
            return false;
        }
        return true;
    }

    
    
    
    @Override
    public String toString() {
        return talao;
    }

}
