package com.chronos.dto;

import java.math.BigDecimal;

/**
 * Created by john on 19/01/18.
 */
public class ProdutoVendaDTO {

    private int id;
    private BigDecimal quantidade;


    public ProdutoVendaDTO() {
    }

    public ProdutoVendaDTO(int id, BigDecimal quantidade) {
        this.id = id;
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }
}
