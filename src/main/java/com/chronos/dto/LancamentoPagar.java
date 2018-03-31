package com.chronos.dto;

import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.Fornecedor;
import com.chronos.modelo.entidades.NfeDuplicata;
import com.chronos.modelo.entidades.VendaCondicoesPagamento;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * Created by john on 09/09/17.
 */
public class LancamentoPagar {

    private int id;
    private BigDecimal valorTotal;
    private Date dataLancamento;
    private Fornecedor fornecedor;
    private String codigoModulo;
    private VendaCondicoesPagamento condicoesPagamento;
    private Set<NfeDuplicata> duplicatas;
    private Empresa empresa;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Set<NfeDuplicata> getDuplicatas() {
        return duplicatas;
    }

    public void setDuplicatas(Set<NfeDuplicata> duplicatas) {
        this.duplicatas = duplicatas;
    }
}
