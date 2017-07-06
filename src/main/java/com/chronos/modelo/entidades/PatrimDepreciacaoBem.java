
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(name = "PATRIM_DEPRECIACAO_BEM")
public class PatrimDepreciacaoBem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_DEPRECIACAO")
    private Date dataDepreciacao;
    @Column(name = "DIAS")
    private Integer dias;
    @Column(name = "TAXA")
    private BigDecimal taxa;
    @Column(name = "INDICE")
    private BigDecimal indice;
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Column(name = "DEPRECIACAO_ACUMULADA")
    private BigDecimal depreciacaoAcumulada;
    @JoinColumn(name = "ID_PATRIM_BEM", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PatrimBem patrimBem;

    public PatrimDepreciacaoBem() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataDepreciacao() {
        return dataDepreciacao;
    }

    public void setDataDepreciacao(Date dataDepreciacao) {
        this.dataDepreciacao = dataDepreciacao;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public BigDecimal getTaxa() {
        return taxa;
    }

    public void setTaxa(BigDecimal taxa) {
        this.taxa = taxa;
    }

    public BigDecimal getIndice() {
        return indice;
    }

    public void setIndice(BigDecimal indice) {
        this.indice = indice;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getDepreciacaoAcumulada() {
        return depreciacaoAcumulada;
    }

    public void setDepreciacaoAcumulada(BigDecimal depreciacaoAcumulada) {
        this.depreciacaoAcumulada = depreciacaoAcumulada;
    }

    public PatrimBem getPatrimBem() {
        return patrimBem;
    }

    public void setPatrimBem(PatrimBem patrimBem) {
        this.patrimBem = patrimBem;
    }

   @Override
   public String toString() {
      return "PatrimDepreciacaoBem{" + "id=" + id + '}';
   }

   

}
