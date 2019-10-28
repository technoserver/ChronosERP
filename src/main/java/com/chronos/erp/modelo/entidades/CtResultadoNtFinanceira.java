
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "CT_RESULTADO_NT_FINANCEIRA")
public class CtResultadoNtFinanceira implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "PERCENTUAL_RATEIO")
    private BigDecimal percentualRateio;
    @JoinColumn(name = "ID_NATUREZA_FINANCEIRA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NaturezaFinanceira naturezaFinanceira;
    @JoinColumn(name = "ID_CENTRO_RESULTADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CentroResultado centroResultado;

    public CtResultadoNtFinanceira() {
    }


    public CtResultadoNtFinanceira(Integer id, Integer idnaturezaFinanceira, String naturezaFinanceira, Integer idcentroResultado, String centroResultado) {
        this.id = id;
        this.naturezaFinanceira = new NaturezaFinanceira(idnaturezaFinanceira, naturezaFinanceira);
        this.centroResultado = new CentroResultado(idcentroResultado, centroResultado);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPercentualRateio() {
        return percentualRateio;
    }

    public void setPercentualRateio(BigDecimal percentualRateio) {
        this.percentualRateio = percentualRateio;
    }

    public NaturezaFinanceira getNaturezaFinanceira() {
        return naturezaFinanceira;
    }

    public void setNaturezaFinanceira(NaturezaFinanceira naturezaFinanceira) {
        this.naturezaFinanceira = naturezaFinanceira;
    }

    public CentroResultado getCentroResultado() {
        return centroResultado;
    }

    public void setCentroResultado(CentroResultado centroResultado) {
        this.centroResultado = centroResultado;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CtResultadoNtFinanceira)) return false;

        CtResultadoNtFinanceira that = (CtResultadoNtFinanceira) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
