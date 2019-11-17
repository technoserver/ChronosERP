package com.chronos.erp.dto;

import com.chronos.erp.modelo.entidades.Cliente;
import com.chronos.erp.modelo.entidades.CondicoesPagamento;
import com.chronos.erp.modelo.entidades.Empresa;
import com.chronos.erp.modelo.entidades.Fornecedor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by john on 09/09/17.
 */
public class LancamentoReceber {

    private int id;
    private String numDocumento;
    private BigDecimal valorTotal;
    private Date dataLancamento;
    private Cliente cliente;
    private Fornecedor fornecedor;
    private String codigoModulo;
    private CondicoesPagamento condicoesPagamento;
    private Empresa emrpesa;

    public LancamentoReceber() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public CondicoesPagamento getCondicoesPagamento() {
        return condicoesPagamento;
    }

    public void setCondicoesPagamento(CondicoesPagamento condicoesPagamento) {
        this.condicoesPagamento = condicoesPagamento;
    }

    public String getCodigoModulo() {
        return codigoModulo;
    }

    public void setCodigoModulo(String codigoModulo) {
        this.codigoModulo = codigoModulo;
    }

    public Empresa getEmrpesa() {
        return emrpesa;
    }

    public void setEmrpesa(Empresa emrpesa) {
        this.emrpesa = emrpesa;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
}
