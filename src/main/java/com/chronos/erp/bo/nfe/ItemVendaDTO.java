package com.chronos.erp.bo.nfe;

import com.chronos.erp.modelo.entidades.OsProdutoServico;
import com.chronos.erp.modelo.entidades.PdvVendaDetalhe;
import com.chronos.erp.modelo.entidades.Produto;
import com.chronos.erp.modelo.entidades.VendaDetalhe;

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

    public ItemVendaDTO(OsProdutoServico itemOs) {
        this.setDesconto(itemOs.getValorDesconto());
        this.setProduto(itemOs.getProduto());
        this.setQuantidade(itemOs.getQuantidade());
        this.setValor(itemOs.getValorUnitario());
        this.setSubtotal(itemOs.getValorSubtotal());
        this.setTotal(itemOs.getValorTotal());
    }

    public ItemVendaDTO(VendaDetalhe itemVenda) {
        this.setDesconto(itemVenda.getValorDesconto());
        this.setProduto(itemVenda.getProduto());
        this.setQuantidade(itemVenda.getQuantidade());
        this.setValor(itemVenda.getValorUnitario());
        this.setSubtotal(itemVenda.getValorSubtotal());
        this.setTotal(itemVenda.getValorTotal());
    }

    public ItemVendaDTO(PdvVendaDetalhe itemPdv) {
        this.setDesconto(itemPdv.getValorDesconto());
        this.setProduto(itemPdv.getProduto());
        this.setQuantidade(itemPdv.getQuantidade());
        this.setValor(itemPdv.getValorUnitario());
        this.setSubtotal(itemPdv.getValorSubtotal());
        this.setTotal(itemPdv.getValorTotal());
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
