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
 * @author John Vanderson M Lima(chronosinfo.com)
 * @version 2.0
 */
package com.chronos.erp.modelo.entidades;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "COMPRA_REQUISICAO_DETALHE")
public class CompraRequisicaoDetalhe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "QUANTIDADE")
    private BigDecimal quantidade;
    @Column(name = "QUANTIDADE_COTADA")
    private BigDecimal quantidadeCotada;
    @Column(name = "ITEM_COTADO")
    private String itemCotado;
    @JoinColumn(name = "ID_COMPRA_REQUISICAO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CompraRequisicao compraRequisicao;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Produto produto;
    @Transient
    private BigDecimal saldo;


    public CompraRequisicaoDetalhe() {
    }

    @PostConstruct
    public void updateSaldo() {
        setSaldo(quantidade.subtract(quantidadeCotada));
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

    public BigDecimal getQuantidadeCotada() {
        return quantidadeCotada;
    }

    public void setQuantidadeCotada(BigDecimal quantidadeCotada) {
        this.quantidadeCotada = quantidadeCotada;
    }

    public String getItemCotado() {
        return itemCotado;
    }

    public void setItemCotado(String itemCotado) {
        this.itemCotado = itemCotado;
    }

    public CompraRequisicao getCompraRequisicao() {
        return compraRequisicao;
    }

    public void setCompraRequisicao(CompraRequisicao compraRequisicao) {
        this.compraRequisicao = compraRequisicao;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "CompraRequisicaoDetalhe{" + "id=" + id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.compraRequisicao);
        hash = 89 * hash + Objects.hashCode(this.produto);
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
        final CompraRequisicaoDetalhe other = (CompraRequisicaoDetalhe) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.compraRequisicao, other.compraRequisicao)) {
            return false;
        }
        return Objects.equals(this.produto, other.produto);
    }

}
