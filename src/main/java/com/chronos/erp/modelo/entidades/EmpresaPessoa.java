
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "EMPRESA_PESSOA")
public class EmpresaPessoa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "RESPONSAVEL_LEGAL")
    private String responsavelLegal;
    @Column(name = "EMPRESA_PRINCIPAL")
    private String empresaPrincipal;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Pessoa pessoa;

    public EmpresaPessoa() {
    }

    public EmpresaPessoa(Integer id, Integer idempresa, String razaoSocial) {
        this.empresa = new Empresa(idempresa, razaoSocial);
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResponsavelLegal() {
        return responsavelLegal;
    }

    public void setResponsavelLegal(String responsavelLegal) {
        this.responsavelLegal = responsavelLegal;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getEmpresaPrincipal() {
        return empresaPrincipal;
    }

    public void setEmpresaPrincipal(String empresaPrincipal) {
        this.empresaPrincipal = empresaPrincipal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmpresaPessoa)) return false;

        EmpresaPessoa that = (EmpresaPessoa) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
