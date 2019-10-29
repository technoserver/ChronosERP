package com.chronos.erp.modelo.entidades;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by john on 01/02/18.
 */
@Entity
@Table(name = "conta_pessoa")
@DynamicUpdate
public class ContaPessoa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "saldo")
    private BigDecimal saldo;
    @Column(name = "classificacao_contabil_conta")
    private String classificacaoContabilConta;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "ID")
    @OneToOne
    private Pessoa pessoa;

    public ContaPessoa() {
    }

    public ContaPessoa(Integer id, String cliente, BigDecimal saldo, String classificacaoContabilConta) {
        this.id = id;
        this.saldo = saldo;
        this.classificacaoContabilConta = classificacaoContabilConta;
        this.pessoa = new Pessoa(cliente);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getClassificacaoContabilConta() {
        return classificacaoContabilConta;
    }

    public void setClassificacaoContabilConta(String classificacaoContabilConta) {
        this.classificacaoContabilConta = classificacaoContabilConta;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContaPessoa)) return false;

        ContaPessoa that = (ContaPessoa) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
