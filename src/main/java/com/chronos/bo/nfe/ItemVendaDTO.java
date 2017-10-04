package com.chronos.bo.nfe;

import com.chronos.modelo.entidades.Produto;

import java.math.BigDecimal;

/**
 * Created by john on 03/10/17.
 */
public class ItemVendaDTO {

    private Produto produto;
    private BigDecimal quantidade;
    private BigDecimal valor;
    private BigDecimal subtotal;
    private BigDecimal desconto;
    private BigDecimal total;


    public ItemVendaDTO() {
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }


}
