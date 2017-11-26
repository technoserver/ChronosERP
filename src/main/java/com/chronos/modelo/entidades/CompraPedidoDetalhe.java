/*
* The MIT License
* 
* Copyright: Copyright (C) 2014 chronosinfo.COM
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
* 
* The author may be contacted at: chronosinfo.com@gmail.com
*
* @author John Vanderson M Lima (chronosinfo.com)
* @version 2.0
*/
package com.chronos.modelo.entidades;


import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name = "COMPRA_PEDIDO_DETALHE")
public class CompraPedidoDetalhe implements Serializable {

    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @NotNull
    @Column(name = "QUANTIDADE")
    @DecimalMin(value = "0.01", message = "O valor  deve ser maior que 0,01")
    private BigDecimal quantidade;
    @NotNull
    @Column(name = "VALOR_UNITARIO")
    @DecimalMin(value = "0.01", message = "O valor unitario do produto  deve ser maior que R$0,01")
    @DecimalMax(value = "9999999.99", message = "O valor unitario do produto  deve ser menor que R$9.999.999,99")
    private BigDecimal valorUnitario;
    @Column(name = "VALOR_SUBTOTAL")
    private BigDecimal valorSubtotal;
    @Column(name = "TAXA_DESCONTO")
    @DecimalMax(value = "100.0", message = "O deve deve ser igual ou menor que 100")
    private BigDecimal taxaDesconto;
    @Column(name = "VALOR_DESCONTO")
    private BigDecimal valorDesconto;
    @Column(name = "VALOR_TOTAL")
    private BigDecimal valorTotal;
    @Column(name = "CST_CSOSN")
    private String cstCsosn;
    @Column(name = "CFOP")
    private Integer cfop;
    @Column(name = "BASE_CALCULO_ICMS")
    private BigDecimal baseCalculoIcms;
    @Column(name = "VALOR_ICMS")
    private BigDecimal valorIcms;
    @Column(name = "VALOR_IPI")
    private BigDecimal valorIpi;
    @Column(name = "ALIQUOTA_ICMS")
    @DecimalMax(value = "100.0", message = "O deve deve ser igual ou menor que 100")
    private BigDecimal aliquotaIcms;
    @Column(name = "ALIQUOTA_IPI")
    @DecimalMax(value = "100.0", message = "O deve deve ser igual ou menor que 100")
    private BigDecimal aliquotaIpi;
    @JoinColumn(name = "ID_COMPRA_PEDIDO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private CompraPedido compraPedido;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Produto produto;

    public CompraPedidoDetalhe() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getValorSubtotal() {
        return valorSubtotal;
    }

    public void setValorSubtotal(BigDecimal valorSubtotal) {
        this.valorSubtotal = valorSubtotal;
    }

    public BigDecimal getTaxaDesconto() {
        return taxaDesconto;
    }

    public void setTaxaDesconto(BigDecimal taxaDesconto) {
        this.taxaDesconto = taxaDesconto;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getCstCsosn() {
        return cstCsosn;
    }

    public void setCstCsosn(String cstCsosn) {
        this.cstCsosn = cstCsosn;
    }

    public Integer getCfop() {
        return cfop;
    }

    public void setCfop(Integer cfop) {
        this.cfop = cfop;
    }

    public BigDecimal getBaseCalculoIcms() {
        return baseCalculoIcms;
    }

    public void setBaseCalculoIcms(BigDecimal baseCalculoIcms) {
        this.baseCalculoIcms = baseCalculoIcms;
    }

    public BigDecimal getValorIcms() {
        return valorIcms;
    }

    public void setValorIcms(BigDecimal valorIcms) {
        this.valorIcms = valorIcms;
    }

    public BigDecimal getValorIpi() {
        return valorIpi;
    }

    public void setValorIpi(BigDecimal valorIpi) {
        this.valorIpi = valorIpi;
    }

    public BigDecimal getAliquotaIcms() {
        return aliquotaIcms;
    }

    public void setAliquotaIcms(BigDecimal aliquotaIcms) {
        this.aliquotaIcms = aliquotaIcms;
    }

    public BigDecimal getAliquotaIpi() {
        return aliquotaIpi;
    }

    public void setAliquotaIpi(BigDecimal aliquotaIpi) {
        this.aliquotaIpi = aliquotaIpi;
    }

    public CompraPedido getCompraPedido() {
        return compraPedido;
    }

    public void setCompraPedido(CompraPedido compraPedido) {
        this.compraPedido = compraPedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public String toString() {
        return "CompraPedidoDetalhe{" + "id=" + id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CompraPedidoDetalhe other = (CompraPedidoDetalhe) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }

        return true;
    }

   

    

}
