
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "os_forma_pagamento")
public class OsFormaPagamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @JoinColumn(name = "id_os_abertura", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private OsAbertura osAbertura;
    @Embedded
    private FormaPagamento formaPagamento;

    public OsFormaPagamento() {
        this.formaPagamento = new FormaPagamento();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OsAbertura getOsAbertura() {
        return osAbertura;
    }

    public void setOsAbertura(OsAbertura osAbertura) {
        this.osAbertura = osAbertura;
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
        if (o == null || getClass() != o.getClass()) return false;
        OsFormaPagamento that = (OsFormaPagamento) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(osAbertura, that.osAbertura) &&
                Objects.equals(formaPagamento, that.formaPagamento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, osAbertura, formaPagamento);
    }
}
