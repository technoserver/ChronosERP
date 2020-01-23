
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "orcamento_forma_pagamento")
public class OrcamentoFormaPagamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @JoinColumn(name = "id_orcamento_cabecalho", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private OrcamentoCabecalho orcamentoCabecalho;
    @Embedded
    private FormaPagamento formaPagamento;

    public OrcamentoFormaPagamento() {
        formaPagamento = new FormaPagamento();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OrcamentoCabecalho getOrcamentoCabecalho() {
        return orcamentoCabecalho;
    }

    public void setOrcamentoCabecalho(OrcamentoCabecalho orcamentoCabecalho) {
        this.orcamentoCabecalho = orcamentoCabecalho;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

}
