/*
 * The MIT License
 *
 * Copyright: Copyright (C) 2017 T2Ti.COM
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
 * The author may be contacted at: t2ti.com@gmail.com
 *
 * @author Claudio de Barros (T2Ti.com)
 *
 */
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "TABELA_PRECO")
public class TabelaPreco implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "COEFICIENTE")
    private BigDecimal coeficiente;
    @Column(name = "PRINCIPAL")
    private String principal;
    @Column(name = "BASE_CUSTO")
    private String baseCusto;
    @Column(name = "METODO_UTILIZACAO")
    private String metodoUtilizacao;
    @Column(name = "COMISSAO_VENDEDOR")
    private BigDecimal comissaoVendedor;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tabelaPreco", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TabelaPrecoProduto> listaProduto;

    public TabelaPreco() {
        this.listaProduto = new ArrayList<>();
        this.comissaoVendedor = BigDecimal.ZERO;
        this.coeficiente = BigDecimal.ZERO;

    }

    public TabelaPreco(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getCoeficiente() {
        return coeficiente;
    }

    public void setCoeficiente(BigDecimal coeficiente) {
        this.coeficiente = coeficiente;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getBaseCusto() {
        return baseCusto;
    }

    public void setBaseCusto(String baseCusto) {
        this.baseCusto = baseCusto;
    }

    public String getMetodoUtilizacao() {
        return metodoUtilizacao;
    }

    public void setMetodoUtilizacao(String metodoUtilizacao) {
        this.metodoUtilizacao = metodoUtilizacao;
    }

    public BigDecimal getComissaoVendedor() {
        return comissaoVendedor;
    }

    public void setComissaoVendedor(BigDecimal comissaoVendedor) {
        this.comissaoVendedor = comissaoVendedor;
    }

    public List<TabelaPrecoProduto> getListaProduto() {
        return listaProduto;
    }

    public void setListaProduto(List<TabelaPrecoProduto> listaProduto) {
        this.listaProduto = listaProduto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabelaPreco)) return false;

        TabelaPreco that = (TabelaPreco) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
