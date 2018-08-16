package com.chronos.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "operadora_cartao_taxa")
public class OperadoraCartaoTaxa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "intervalo_inicial")
    @NotNull
    private Integer intervaloInicial;
    @Column(name = "intervalo_final")
    @NotNull
    private Integer intervaloFinal;
    @Column(name = "taxa_adm")
    @NotNull
    @DecimalMin(value = "0.01", message = "O valor  deve ser maior que 0,01")
    @DecimalMax(value = "100.00", message = "O valor  deve ser menor que 100,00")
    private BigDecimal taxaAdm;
    @Column(name = "credito_em")
    @NotNull
    private Integer creditoEm;
    @NotNull
    @JoinColumn(name = "ID_OPERADORA_CARTAO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private OperadoraCartao operadoraCartao;

    public OperadoraCartaoTaxa() {
        this.taxaAdm = BigDecimal.ONE;
        this.intervaloFinal = 1;
        this.intervaloInicial = 1;
        this.creditoEm = 30;
    }

    public OperadoraCartaoTaxa(Integer id, BigDecimal taxaAdm) {
        this.id = id;
        this.taxaAdm = taxaAdm;
    }

    public OperadoraCartaoTaxa(Integer intervaloInicial, Integer intervaloFinal, BigDecimal taxaAdm) {
        this.intervaloInicial = intervaloInicial;
        this.intervaloFinal = intervaloFinal;
        this.taxaAdm = taxaAdm;
        this.creditoEm = 30;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIntervaloInicial() {
        return intervaloInicial;
    }

    public void setIntervaloInicial(Integer intervaloInicial) {
        this.intervaloInicial = intervaloInicial;
    }

    public Integer getIntervaloFinal() {
        return intervaloFinal;
    }

    public void setIntervaloFinal(Integer intervaloFinal) {
        this.intervaloFinal = intervaloFinal;
    }

    public BigDecimal getTaxaAdm() {
        return taxaAdm;
    }

    public void setTaxaAdm(BigDecimal taxaAdm) {
        this.taxaAdm = taxaAdm;
    }

    public Integer getCreditoEm() {
        return creditoEm;
    }

    public void setCreditoEm(Integer creditoEm) {
        this.creditoEm = creditoEm;
    }

    public OperadoraCartao getOperadoraCartao() {
        return operadoraCartao;
    }

    public void setOperadoraCartao(OperadoraCartao operadoraCartao) {
        this.operadoraCartao = operadoraCartao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OperadoraCartaoTaxa)) return false;
        OperadoraCartaoTaxa taxa = (OperadoraCartaoTaxa) o;
        return Objects.equals(id, taxa.id) &&
                Objects.equals(intervaloInicial, taxa.intervaloInicial) &&
                Objects.equals(intervaloFinal, taxa.intervaloFinal);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, intervaloInicial, intervaloFinal);
    }
}
