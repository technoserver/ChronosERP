/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author john
 */
@Entity
@Table(name = "empresa_produto")
@NamedQuery(name = "EmpresaProduto.atualizarEstoque"
        , query = "UPDATE EmpresaProduto o SET o.quantidadeEstoque = ?1 ,o.estoqueVerificado = ?1 where o.id =?2")
public class EmpresaProduto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantidade_estoque", precision = 18, scale = 6)
    private BigDecimal quantidadeEstoque;
    @Column(name = "estoque_verificado", precision = 18, scale = 6)
    private BigDecimal estoqueVerificado;
    @JoinColumn(name = "id_empresa", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Empresa empresa;
    @JoinColumn(name = "id_produto", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Produto produto;
    @Transient
    private BigDecimal valorVenda;

    public EmpresaProduto() {
        this.estoqueVerificado = BigDecimal.ZERO;
        this.quantidadeEstoque = BigDecimal.ZERO;
    }

    public EmpresaProduto(Integer id, Integer idproduto, String nome) {
        this.id = id;
        this.produto = new Produto(idproduto, nome);
    }

    public EmpresaProduto(Integer id, Integer idproduto, String nome, BigDecimal valorVenda) {
        this.id = id;
        this.produto = new Produto(idproduto, nome, valorVenda);
    }


    public EmpresaProduto(Integer id, Integer idempresa, String nomeEmpresa, BigDecimal quantidadeEstoque, BigDecimal estoqueVerificado) {
        this.id = id;
        this.empresa = new Empresa(idempresa, nomeEmpresa);
        this.quantidadeEstoque = quantidadeEstoque;
        this.estoqueVerificado = estoqueVerificado;
    }

    public EmpresaProduto(Integer id, BigDecimal quantidadeEstoque, Integer idproduto, String nome, BigDecimal valorVenda) {
        this.id = id;
        this.quantidadeEstoque = quantidadeEstoque;
        this.produto = new Produto(idproduto, nome, valorVenda);
    }

    public EmpresaProduto(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(BigDecimal quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getEstoqueVerificado() {
        return estoqueVerificado;
    }

    public void setEstoqueVerificado(BigDecimal estoqueVerificado) {
        this.estoqueVerificado = estoqueVerificado;
    }

    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(BigDecimal valorVenda) {
        this.valorVenda = valorVenda;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmpresaProduto)) {
            return false;
        }
        EmpresaProduto other = (EmpresaProduto) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "javaapplication1.EmpresaProduto[ id=" + id + " ]";
    }
    
}
