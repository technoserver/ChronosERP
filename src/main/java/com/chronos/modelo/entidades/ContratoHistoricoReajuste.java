
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(name = "CONTRATO_HISTORICO_REAJUSTE")
public class ContratoHistoricoReajuste implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "INDICE")
    private BigDecimal indice;
    @Column(name = "VALOR_ANTERIOR")
    private BigDecimal valorAnterior;
    @Column(name = "VALOR_ATUAL")
    private BigDecimal valorAtual;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_REAJUSTE")
    private Date dataReajuste;
    @Column(name = "OBSERVACAO")
    private String observacao;
    @JoinColumn(name = "ID_CONTRATO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Contrato contrato;

    public ContratoHistoricoReajuste() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getIndice() {
        return indice;
    }

    public void setIndice(BigDecimal indice) {
        this.indice = indice;
    }

    public BigDecimal getValorAnterior() {
        return valorAnterior;
    }

    public void setValorAnterior(BigDecimal valorAnterior) {
        this.valorAnterior = valorAnterior;
    }

    public BigDecimal getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(BigDecimal valorAtual) {
        this.valorAtual = valorAtual;
    }

    public Date getDataReajuste() {
        return dataReajuste;
    }

    public void setDataReajuste(Date dataReajuste) {
        this.dataReajuste = dataReajuste;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

   @Override
   public String toString() {
      return "ContratoHistoricoReajuste{" + "id=" + id + '}';
   }

   
}
