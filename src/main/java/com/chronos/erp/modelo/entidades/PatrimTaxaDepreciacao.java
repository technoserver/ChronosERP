
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "PATRIM_TAXA_DEPRECIACAO")
public class PatrimTaxaDepreciacao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NCM")
    private String ncm;
    @Column(name = "BEM")
    private String bem;
    @Column(name = "VIDA")
    private BigDecimal vida;
    @Column(name = "TAXA")
    private BigDecimal taxa;

    public PatrimTaxaDepreciacao() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public String getBem() {
        return bem;
    }

    public void setBem(String bem) {
        this.bem = bem;
    }

    public BigDecimal getVida() {
        return vida;
    }

    public void setVida(BigDecimal vida) {
        this.vida = vida;
    }

    public BigDecimal getTaxa() {
        return taxa;
    }

    public void setTaxa(BigDecimal taxa) {
        this.taxa = taxa;
    }

    @Override
    public String toString() {
        return taxa.toString();
    }


}
