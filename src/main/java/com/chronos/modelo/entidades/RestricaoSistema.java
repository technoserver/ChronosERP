
package com.chronos.modelo.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RESTRICAO_SISTEMA")
public class RestricaoSistema implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "TAXA_MAIOR")
    private BigDecimal taxaMaior;
    @Column(name = "TAXA_MENOR")
    private BigDecimal taxaMenor;
    @Column(name = "DATA_MAIOR")
    private Integer dataMaior;
    @Column(name = "DATA_MENOR")
    private Integer dataMenor;

    public BigDecimal getTaxaMaior() {
        return taxaMaior;
    }

    public void setTaxaMaior(BigDecimal taxaMaior) {
        this.taxaMaior = taxaMaior;
    }

    public BigDecimal getTaxaMenor() {
        return taxaMenor;
    }

    public void setTaxaMenor(BigDecimal taxaMenor) {
        this.taxaMenor = taxaMenor;
    }

    public Integer getDataMaior() {
        return dataMaior;
    }

    public void setDataMaior(Integer dataMaior) {
        this.dataMaior = dataMaior;
    }

    public Integer getDataMenor() {
        return dataMenor;
    }

    public void setDataMenor(Integer dataMenor) {
        this.dataMenor = dataMenor;
    }



}
