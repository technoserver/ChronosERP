
package com.chronos.erp.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "FIN_LCTO_RECEBER_NT_FINANCEIRA")
public class FinLctoReceberNtFinanceira implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_INCLUSAO")
    private Date dataInclusao;
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Column(name = "PERCENTUAL")
    private BigDecimal percentual;
    @JoinColumn(name = "ID_FIN_LANCAMENTO_RECEBER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FinLancamentoReceber finLancamentoReceber;
    @JoinColumn(name = "ID_NATUREZA_FINANCEIRA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NaturezaFinanceira naturezaFinanceira;


    public FinLctoReceberNtFinanceira() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getPercentual() {
        return percentual;
    }

    public void setPercentual(BigDecimal percentual) {
        this.percentual = percentual;
    }

    public FinLancamentoReceber getFinLancamentoReceber() {
        return finLancamentoReceber;
    }

    public void setFinLancamentoReceber(FinLancamentoReceber finLancamentoReceber) {
        this.finLancamentoReceber = finLancamentoReceber;
    }

    public NaturezaFinanceira getNaturezaFinanceira() {
        return naturezaFinanceira;
    }

    public void setNaturezaFinanceira(NaturezaFinanceira naturezaFinanceira) {
        this.naturezaFinanceira = naturezaFinanceira;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final FinLctoReceberNtFinanceira other = (FinLctoReceberNtFinanceira) obj;
        return Objects.equals(this.id, other.id);
    }


}
