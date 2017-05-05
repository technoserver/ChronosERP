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
@Table(name = "NFE_CANA_FORNECIMENTO_DIARIO")
public class NfeCanaFornecimentoDiario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "DIA")
    private String dia;
    @Column(name = "QUANTIDADE")
    private BigDecimal quantidade;
    @Column(name = "QUANTIDADE_TOTAL_MES")
    private BigDecimal quantidadeTotalMes;
    @Column(name = "QUANTIDADE_TOTAL_ANTERIOR")
    private BigDecimal quantidadeTotalAnterior;
    @Column(name = "QUANTIDADE_TOTAL_GERAL")
    private BigDecimal quantidadeTotalGeral;
    @JoinColumn(name = "ID_NFE_CANA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfeCana nfeCana;

    public NfeCanaFornecimentoDiario() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getQuantidadeTotalMes() {
        return quantidadeTotalMes;
    }

    public void setQuantidadeTotalMes(BigDecimal quantidadeTotalMes) {
        this.quantidadeTotalMes = quantidadeTotalMes;
    }

    public BigDecimal getQuantidadeTotalAnterior() {
        return quantidadeTotalAnterior;
    }

    public void setQuantidadeTotalAnterior(BigDecimal quantidadeTotalAnterior) {
        this.quantidadeTotalAnterior = quantidadeTotalAnterior;
    }

    public BigDecimal getQuantidadeTotalGeral() {
        return quantidadeTotalGeral;
    }

    public void setQuantidadeTotalGeral(BigDecimal quantidadeTotalGeral) {
        this.quantidadeTotalGeral = quantidadeTotalGeral;
    }

    public NfeCana getNfeCana() {
        return nfeCana;
    }

    public void setNfeCana(NfeCana nfeCana) {
        this.nfeCana = nfeCana;
    }

}
