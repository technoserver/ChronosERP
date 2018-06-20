/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author john
 */
@Entity
@Table(name = "mdfe_rodoviario_pedagio")
public class MdfeRodoviarioPedagio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Column(name = "cnpj_fornecedor", length = 14)
    private String cnpjFornecedor;
    @Column(name = "cnpj_cpf_responsavel", length = 14)
    private String cnpjCpfResponsavel;
    @Column(name = "numero_comprovante", length = 20)
    private String numeroComprovante;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Column(precision = 13, scale = 2)
    private BigDecimal valor;
    @JoinColumn(name = "id_mdfe_rodoviario", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private MdfeRodoviario mdfeRodoviario;

    public MdfeRodoviarioPedagio() {
    }

    public MdfeRodoviarioPedagio(Integer id) {
        this.id = id;
    }

    @PrePersist
    @PreUpdate
    private void prePersist() {
        cnpjFornecedor = cnpjFornecedor != null ? cnpjFornecedor.replaceAll("\\D", "") : "";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCnpjFornecedor() {
        return cnpjFornecedor;
    }

    public void setCnpjFornecedor(String cnpjFornecedor) {
        this.cnpjFornecedor = cnpjFornecedor;
    }

    public String getCnpjCpfResponsavel() {
        return cnpjCpfResponsavel;
    }

    public void setCnpjCpfResponsavel(String cnpjCpfResponsavel) {
        this.cnpjCpfResponsavel = cnpjCpfResponsavel;
    }

    public String getNumeroComprovante() {
        return numeroComprovante;
    }

    public void setNumeroComprovante(String numeroComprovante) {
        this.numeroComprovante = numeroComprovante;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
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
        if (!(object instanceof MdfeRodoviarioPedagio)) {
            return false;
        }
        MdfeRodoviarioPedagio other = (MdfeRodoviarioPedagio) object;
        if ((this.numeroComprovante == null && other.numeroComprovante != null) || (this.numeroComprovante != null && !this.numeroComprovante.equals(other.numeroComprovante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.MdfeRodoviarioPedagio[ id=" + id + " ]";
    }

}
