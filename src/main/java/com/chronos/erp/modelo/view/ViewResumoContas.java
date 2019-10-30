package com.chronos.erp.modelo.view;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by john on 20/12/17.
 */
@Embeddable
public class ViewResumoContas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "tipo")
    private String tipo;
    @Column(name = "valor_quitado")
    private BigDecimal valorQuitado;
    @Column(name = "total_quita")
    private BigDecimal totalQuita;
    @Column(name = "total_quita_7_dias")
    private BigDecimal totalQuita7Dias;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValorQuitado() {
        return valorQuitado;
    }

    public void setValorQuitado(BigDecimal valorQuitado) {
        this.valorQuitado = valorQuitado;
    }

    public BigDecimal getTotalQuita() {
        return totalQuita;
    }

    public void setTotalQuita(BigDecimal totalQuita) {
        this.totalQuita = totalQuita;
    }

    public BigDecimal getTotalQuita7Dias() {
        return totalQuita7Dias;
    }

    public void setTotalQuita7Dias(BigDecimal totalQuita7Dias) {
        this.totalQuita7Dias = totalQuita7Dias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewResumoContas)) return false;

        ViewResumoContas that = (ViewResumoContas) o;

        return getTipo() != null ? getTipo().equals(that.getTipo()) : that.getTipo() == null;
    }

    @Override
    public int hashCode() {
        return getTipo() != null ? getTipo().hashCode() : 0;
    }
}
