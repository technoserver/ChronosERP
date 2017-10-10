
package com.chronos.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "FISCAL_APURACAO_ICMS")
public class FiscalApuracaoIcms implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "COMPETENCIA")
    private String competencia;
    @Column(name = "VALOR_TOTAL_DEBITO")
    private BigDecimal valorTotalDebito;
    @Column(name = "VALOR_AJUSTE_DEBITO")
    private BigDecimal valorAjusteDebito;
    @Column(name = "VALOR_TOTAL_AJUSTE_DEBITO")
    private BigDecimal valorTotalAjusteDebito;
    @Column(name = "VALOR_ESTORNO_CREDITO")
    private BigDecimal valorEstornoCredito;
    @Column(name = "VALOR_TOTAL_CREDITO")
    private BigDecimal valorTotalCredito;
    @Column(name = "VALOR_AJUSTE_CREDITO")
    private BigDecimal valorAjusteCredito;
    @Column(name = "VALOR_TOTAL_AJUSTE_CREDITO")
    private BigDecimal valorTotalAjusteCredito;
    @Column(name = "VALOR_ESTORNO_DEBITO")
    private BigDecimal valorEstornoDebito;
    @Column(name = "VALOR_SALDO_CREDOR_ANTERIOR")
    private BigDecimal valorSaldoCredorAnterior;
    @Column(name = "VALOR_SALDO_APURADO")
    private BigDecimal valorSaldoApurado;
    @Column(name = "VALOR_TOTAL_DEDUCAO")
    private BigDecimal valorTotalDeducao;
    @Column(name = "VALOR_ICMS_RECOLHER")
    private BigDecimal valorIcmsRecolher;
    @Column(name = "VALOR_SALDO_CREDOR_TRANSP")
    private BigDecimal valorSaldoCredorTransp;
    @Column(name = "VALOR_DEBITO_ESPECIAL")
    private BigDecimal valorDebitoEspecial;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;

    public FiscalApuracaoIcms() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

    public BigDecimal getValorTotalDebito() {
        return valorTotalDebito;
    }

    public void setValorTotalDebito(BigDecimal valorTotalDebito) {
        this.valorTotalDebito = valorTotalDebito;
    }

    public BigDecimal getValorAjusteDebito() {
        return valorAjusteDebito;
    }

    public void setValorAjusteDebito(BigDecimal valorAjusteDebito) {
        this.valorAjusteDebito = valorAjusteDebito;
    }

    public BigDecimal getValorTotalAjusteDebito() {
        return valorTotalAjusteDebito;
    }

    public void setValorTotalAjusteDebito(BigDecimal valorTotalAjusteDebito) {
        this.valorTotalAjusteDebito = valorTotalAjusteDebito;
    }

    public BigDecimal getValorEstornoCredito() {
        return valorEstornoCredito;
    }

    public void setValorEstornoCredito(BigDecimal valorEstornoCredito) {
        this.valorEstornoCredito = valorEstornoCredito;
    }

    public BigDecimal getValorTotalCredito() {
        return valorTotalCredito;
    }

    public void setValorTotalCredito(BigDecimal valorTotalCredito) {
        this.valorTotalCredito = valorTotalCredito;
    }

    public BigDecimal getValorAjusteCredito() {
        return valorAjusteCredito;
    }

    public void setValorAjusteCredito(BigDecimal valorAjusteCredito) {
        this.valorAjusteCredito = valorAjusteCredito;
    }

    public BigDecimal getValorTotalAjusteCredito() {
        return valorTotalAjusteCredito;
    }

    public void setValorTotalAjusteCredito(BigDecimal valorTotalAjusteCredito) {
        this.valorTotalAjusteCredito = valorTotalAjusteCredito;
    }

    public BigDecimal getValorEstornoDebito() {
        return valorEstornoDebito;
    }

    public void setValorEstornoDebito(BigDecimal valorEstornoDebito) {
        this.valorEstornoDebito = valorEstornoDebito;
    }

    public BigDecimal getValorSaldoCredorAnterior() {
        return valorSaldoCredorAnterior;
    }

    public void setValorSaldoCredorAnterior(BigDecimal valorSaldoCredorAnterior) {
        this.valorSaldoCredorAnterior = valorSaldoCredorAnterior;
    }

    public BigDecimal getValorSaldoApurado() {
        return valorSaldoApurado;
    }

    public void setValorSaldoApurado(BigDecimal valorSaldoApurado) {
        this.valorSaldoApurado = valorSaldoApurado;
    }

    public BigDecimal getValorTotalDeducao() {
        return valorTotalDeducao;
    }

    public void setValorTotalDeducao(BigDecimal valorTotalDeducao) {
        this.valorTotalDeducao = valorTotalDeducao;
    }

    public BigDecimal getValorIcmsRecolher() {
        return valorIcmsRecolher;
    }

    public void setValorIcmsRecolher(BigDecimal valorIcmsRecolher) {
        this.valorIcmsRecolher = valorIcmsRecolher;
    }

    public BigDecimal getValorSaldoCredorTransp() {
        return valorSaldoCredorTransp;
    }

    public void setValorSaldoCredorTransp(BigDecimal valorSaldoCredorTransp) {
        this.valorSaldoCredorTransp = valorSaldoCredorTransp;
    }

    public BigDecimal getValorDebitoEspecial() {
        return valorDebitoEspecial;
    }

    public void setValorDebitoEspecial(BigDecimal valorDebitoEspecial) {
        this.valorDebitoEspecial = valorDebitoEspecial;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }


}
