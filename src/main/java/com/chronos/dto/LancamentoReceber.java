package com.chronos.dto;

import com.chronos.modelo.entidades.Cliente;
import com.chronos.modelo.entidades.VendaCondicoesPagamento;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by john on 09/09/17.
 */
public class LancamentoReceber {

    public String numDocumento;
    private BigDecimal valorTotal;
    private Date dataLancamento;
    private Cliente cliente;
    private String codigoModulo;
    private VendaCondicoesPagamento condicoesPagamento;

    public LancamentoReceber() {

    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public VendaCondicoesPagamento getCondicoesPagamento() {
        return condicoesPagamento;
    }

    public void setCondicoesPagamento(VendaCondicoesPagamento condicoesPagamento) {
        this.condicoesPagamento = condicoesPagamento;
    }

    public String getCodigoModulo() {
        return codigoModulo;
    }

    public void setCodigoModulo(String codigoModulo) {
        this.codigoModulo = codigoModulo;
    }
}
