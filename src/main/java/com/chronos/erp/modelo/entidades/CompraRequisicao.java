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
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "COMPRA_REQUISICAO")
public class CompraRequisicao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_REQUISICAO")
    private Date dataRequisicao;
    @Column(name = "OBSERVACAO")
    private String observacao;
    @JoinColumn(name = "ID_COMPRA_TIPO_REQUISICAO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CompraTipoRequisicao compraTipoRequisicao;
    @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Colaborador colaborador;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "compraRequisicao", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CompraRequisicaoDetalhe> listaCompraRequisicaoDetalhe;

    public CompraRequisicao() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataRequisicao() {
        return dataRequisicao;
    }

    public void setDataRequisicao(Date dataRequisicao) {
        this.dataRequisicao = dataRequisicao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public CompraTipoRequisicao getCompraTipoRequisicao() {
        return compraTipoRequisicao;
    }

    public void setCompraTipoRequisicao(CompraTipoRequisicao compraTipoRequisicao) {
        this.compraTipoRequisicao = compraTipoRequisicao;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Set<CompraRequisicaoDetalhe> getListaCompraRequisicaoDetalhe() {
        return listaCompraRequisicaoDetalhe;
    }

    public void setListaCompraRequisicaoDetalhe(Set<CompraRequisicaoDetalhe> listaCompraRequisicaoDetalhe) {
        this.listaCompraRequisicaoDetalhe = listaCompraRequisicaoDetalhe;
    }

    @Override
    public String toString() {
        return "CompraRequisicao{" + "id=" + id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final CompraRequisicao other = (CompraRequisicao) obj;
        return Objects.equals(this.id, other.id);
    }


}
