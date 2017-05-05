
package com.chronos.modelo.entidades;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
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
@Table(name = "ORCAMENTO_FLUXO_CAIXA_DETALHE")
public class OrcamentoFluxoCaixaDetalhe implements Serializable {

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
    @JoinColumn(name = "ID_ORCAMENTO_FLUXO_CAIXA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private OrcamentoFluxoCaixa orcamentoFluxoCaixa;
    @JoinColumn(name = "ID_NATUREZA_FINANCEIRA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NaturezaFinanceira naturezaFinanceira;

    public OrcamentoFluxoCaixaDetalhe() {
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

    public OrcamentoFluxoCaixa getOrcamentoFluxoCaixa() {
        return orcamentoFluxoCaixa;
    }

    public void setOrcamentoFluxoCaixa(OrcamentoFluxoCaixa orcamentoFluxoCaixa) {
        this.orcamentoFluxoCaixa = orcamentoFluxoCaixa;
    }

    public NaturezaFinanceira getNaturezaFinanceira() {
        return naturezaFinanceira;
    }

    public void setNaturezaFinanceira(NaturezaFinanceira naturezaFinanceira) {
        this.naturezaFinanceira = naturezaFinanceira;
    }

    @Override
    public String toString() {
        return "OrcamentoFluxoCaixaDetalhe{" + "id=" + id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
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
        final OrcamentoFluxoCaixaDetalhe other = (OrcamentoFluxoCaixaDetalhe) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

 
}
