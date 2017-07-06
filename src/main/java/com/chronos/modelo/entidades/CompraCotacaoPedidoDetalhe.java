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
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name = "COMPRA_COTACAO_PEDIDO_DETALHE")
public class CompraCotacaoPedidoDetalhe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "QUANTIDADE_PEDIDA")
    private BigDecimal quantidadePedida;
    @JoinColumn(name = "ID_COMPRA_COTACAO_DETALHE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CompraCotacaoDetalhe compraCotacaoDetalhe;
    @JoinColumn(name = "ID_COMPRA_PEDIDO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CompraPedido compraPedido;

    public CompraCotacaoPedidoDetalhe() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getQuantidadePedida() {
        return quantidadePedida;
    }

    public void setQuantidadePedida(BigDecimal quantidadePedida) {
        this.quantidadePedida = quantidadePedida;
    }

    public CompraCotacaoDetalhe getCompraCotacaoDetalhe() {
        return compraCotacaoDetalhe;
    }

    public void setCompraCotacaoDetalhe(CompraCotacaoDetalhe compraCotacaoDetalhe) {
        this.compraCotacaoDetalhe = compraCotacaoDetalhe;
    }

    public CompraPedido getCompraPedido() {
        return compraPedido;
    }

    public void setCompraPedido(CompraPedido compraPedido) {
        this.compraPedido = compraPedido;
    }

    @Override
    public String toString() {
        return "CompraCotacaoPedidoDetalhe{" + "id=" + id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final CompraCotacaoPedidoDetalhe other = (CompraCotacaoPedidoDetalhe) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    

}
