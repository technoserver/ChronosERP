/*
 * The MIT License
 * 
 * Copyright: Copyright (C) 2014 Chronosinfo.COM
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

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "COMPRA_COTACAO")
public class CompraCotacao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_COTACAO")
    private Date dataCotacao;
    @NotNull
    @NotBlank
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "SITUACAO")
    private String situacao;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "compraCotacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CompraFornecedorCotacao> listaCompraFornecedorCotacao;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "compraCotacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CompraReqCotacaoDetalhe> listaCompraReqCotacaoDetalhe;

    public CompraCotacao() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataCotacao() {
        return dataCotacao;
    }

    public void setDataCotacao(Date dataCotacao) {
        this.dataCotacao = dataCotacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Set<CompraFornecedorCotacao> getListaCompraFornecedorCotacao() {
        return listaCompraFornecedorCotacao;
    }

    public void setListaCompraFornecedorCotacao(Set<CompraFornecedorCotacao> listaCompraFornecedorCotacao) {
        this.listaCompraFornecedorCotacao = listaCompraFornecedorCotacao;
    }

    public Set<CompraReqCotacaoDetalhe> getListaCompraReqCotacaoDetalhe() {
        return listaCompraReqCotacaoDetalhe;
    }

    public void setListaCompraReqCotacaoDetalhe(Set<CompraReqCotacaoDetalhe> listaCompraReqCotacaoDetalhe) {
        this.listaCompraReqCotacaoDetalhe = listaCompraReqCotacaoDetalhe;
    }

    @Override
    public String toString() {
        return "CompraCotacao{" + "id=" + id + '}';
    }
    
    

}
