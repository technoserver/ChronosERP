/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.dto;


import com.chronos.util.CurrencyWriter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author john
 */
public class ReciboPagamentoDTO {

    private Integer idcliente;
    private Integer idtipoRecebimento;
    private BigDecimal valorPago;
    private List<Integer> idsrecebimento;

    public Integer getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(Integer idcliente) {
        this.idcliente = idcliente;
    }

    public Integer getIdtipoRecebimento() {
        return idtipoRecebimento;
    }

    public void setIdtipoRecebimento(Integer idtipoRecebimento) {
        this.idtipoRecebimento = idtipoRecebimento;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public List<Integer> getIdsrecebimento() {
        return idsrecebimento;
    }

    public void setIdsrecebimento(List<Integer> idsrecebimento) {
        this.idsrecebimento = idsrecebimento;
    }


    public String getValorExtenso() {
        return CurrencyWriter.getInstance().write(Optional.ofNullable(valorPago).orElse(BigDecimal.ZERO));
    }

}
