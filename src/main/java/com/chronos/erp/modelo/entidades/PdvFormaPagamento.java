
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "pdv_forma_pagamento")
public class PdvFormaPagamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @JoinColumn(name = "ID_PDV_VENDA_CABECALHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PdvVendaCabecalho pdvVendaCabecalho;
    @Embedded
    private FormaPagamento formaPagamento;

    public PdvFormaPagamento() {
        this.formaPagamento = new FormaPagamento();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PdvVendaCabecalho getPdvVendaCabecalho() {
        return pdvVendaCabecalho;
    }

    public void setPdvVendaCabecalho(PdvVendaCabecalho pdvVendaCabecalho) {
        this.pdvVendaCabecalho = pdvVendaCabecalho;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PdvFormaPagamento)) return false;

        PdvFormaPagamento that = (PdvFormaPagamento) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
