
package com.chronos.modelo.entidades;


import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "ORCAMENTO_DETALHE")
public class OrcamentoDetalhe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "PERIODO")
    private String periodo;
    @Column(name = "VALOR_ORCADO")
    private BigDecimal valorOrcado;
    @Column(name = "VALOR_REALIZADO")
    private BigDecimal valorRealizado;
    @Column(name = "TAXA_VARIACAO")
    private BigDecimal taxaVariacao;
    @Column(name = "VALOR_VARIACAO")
    private BigDecimal valorVariacao;
    @JoinColumn(name = "ID_ORCAMENTO_EMPRESARIAL", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private OrcamentoEmpresarial orcamentoEmpresarial;
    @JoinColumn(name = "ID_NATUREZA_FINANCEIRA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NaturezaFinanceira naturezaFinanceira;

    public OrcamentoDetalhe() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public BigDecimal getValorOrcado() {
        return valorOrcado;
    }

    public void setValorOrcado(BigDecimal valorOrcado) {
        this.valorOrcado = valorOrcado;
    }

    public BigDecimal getValorRealizado() {
        return valorRealizado;
    }

    public void setValorRealizado(BigDecimal valorRealizado) {
        this.valorRealizado = valorRealizado;
    }

    public BigDecimal getTaxaVariacao() {
        return taxaVariacao;
    }

    public void setTaxaVariacao(BigDecimal taxaVariacao) {
        this.taxaVariacao = taxaVariacao;
    }

    public BigDecimal getValorVariacao() {
        return valorVariacao;
    }

    public void setValorVariacao(BigDecimal valorVariacao) {
        this.valorVariacao = valorVariacao;
    }

    public OrcamentoEmpresarial getOrcamentoEmpresarial() {
        return orcamentoEmpresarial;
    }

    public void setOrcamentoEmpresarial(OrcamentoEmpresarial orcamentoEmpresarial) {
        this.orcamentoEmpresarial = orcamentoEmpresarial;
    }

    public NaturezaFinanceira getNaturezaFinanceira() {
        return naturezaFinanceira;
    }

    public void setNaturezaFinanceira(NaturezaFinanceira naturezaFinanceira) {
        this.naturezaFinanceira = naturezaFinanceira;
    }

    @Override
    public String toString() {
        return "OrcamentoDetalhe{" + "id=" + id + '}';
    }



}
