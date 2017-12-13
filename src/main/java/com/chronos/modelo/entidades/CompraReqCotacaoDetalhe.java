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
@Table(name = "COMPRA_REQ_COTACAO_DETALHE")
public class CompraReqCotacaoDetalhe implements Serializable {

    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @NotNull
    @Column(name = "QUANTIDADE_COTADA")
    @DecimalMin(value = "0.01", message = "O valor unitario do produto  deve ser maior que R$0,01")
    @DecimalMax(value = "9999999.99", message = "O valor unitario do produto  deve ser menor que R$9.999.999,99")
    private BigDecimal quantidadeCotada;
    @JoinColumn(name = "ID_COMPRA_REQUISICAO_DETALHE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CompraRequisicaoDetalhe compraRequisicaoDetalhe;
    @JoinColumn(name = "ID_COMPRA_COTACAO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private CompraCotacao compraCotacao;

    public CompraReqCotacaoDetalhe() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getQuantidadeCotada() {
        return quantidadeCotada;
    }

    public void setQuantidadeCotada(BigDecimal quantidadeCotada) {
        this.quantidadeCotada = quantidadeCotada;
    }

    public CompraRequisicaoDetalhe getCompraRequisicaoDetalhe() {
        return compraRequisicaoDetalhe;
    }

    public void setCompraRequisicaoDetalhe(CompraRequisicaoDetalhe compraRequisicaoDetalhe) {
        this.compraRequisicaoDetalhe = compraRequisicaoDetalhe;
    }

    public CompraCotacao getCompraCotacao() {
        return compraCotacao;
    }

    public void setCompraCotacao(CompraCotacao compraCotacao) {
        this.compraCotacao = compraCotacao;
    }

    @Override
    public String toString() {
        return "CompraReqCotacaoDetalhe{" + "id=" + id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final CompraReqCotacaoDetalhe other = (CompraReqCotacaoDetalhe) obj;
        return Objects.equals(this.id, other.id);
    }

    

}
