
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
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "NFE_DETALHE_IMPOSTO_PIS")
public class NfeDetalheImpostoPis implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CST_PIS")
    private String cstPis;
    @Column(name = "QUANTIDADE_VENDIDA")
    private BigDecimal quantidadeVendida;
    @Column(name = "VALOR_BASE_CALCULO_PIS")
    private BigDecimal valorBaseCalculoPis;
    @Column(name = "ALIQUOTA_PIS_PERCENTUAL")
    private BigDecimal aliquotaPisPercentual;
    @Column(name = "ALIQUOTA_PIS_REAIS")
    private BigDecimal aliquotaPisReais;
    @Column(name = "VALOR_PIS")
    private BigDecimal valorPis;
    @JoinColumn(name = "ID_NFE_DETALHE", referencedColumnName = "ID")
    @OneToOne(optional=false)
    private NfeDetalhe nfeDetalhe;

    public NfeDetalheImpostoPis() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCstPis() {
        return cstPis;
    }

    public void setCstPis(String cstPis) {
        this.cstPis = cstPis;
    }

    public BigDecimal getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public void setQuantidadeVendida(BigDecimal quantidadeVendida) {
        this.quantidadeVendida = quantidadeVendida;
    }

    public BigDecimal getValorBaseCalculoPis() {
        return valorBaseCalculoPis;
    }

    public void setValorBaseCalculoPis(BigDecimal valorBaseCalculoPis) {
        this.valorBaseCalculoPis = valorBaseCalculoPis;
    }

    public BigDecimal getAliquotaPisPercentual() {
        return aliquotaPisPercentual;
    }

    public void setAliquotaPisPercentual(BigDecimal aliquotaPisPercentual) {
        this.aliquotaPisPercentual = aliquotaPisPercentual;
    }

    public BigDecimal getAliquotaPisReais() {
        return aliquotaPisReais;
    }

    public void setAliquotaPisReais(BigDecimal aliquotaPisReais) {
        this.aliquotaPisReais = aliquotaPisReais;
    }

    public BigDecimal getValorPis() {
        return valorPis;
    }

    public void setValorPis(BigDecimal valorPis) {
        this.valorPis = valorPis;
    }

    public NfeDetalhe getNfeDetalhe() {
        return nfeDetalhe;
    }

    public void setNfeDetalhe(NfeDetalhe nfeDetalhe) {
        this.nfeDetalhe = nfeDetalhe;
    }


}
