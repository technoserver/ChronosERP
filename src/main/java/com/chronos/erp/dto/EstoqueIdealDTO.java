package com.chronos.erp.dto;

import com.chronos.erp.modelo.entidades.Produto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by john on 19/10/17.
 */
public class EstoqueIdealDTO implements Serializable {

    private Integer id;
    private String nome;
    private BigDecimal valorCompra;
    private BigDecimal controle;
    private BigDecimal estoqueIdeal;
    private Produto produto;


    public EstoqueIdealDTO(Integer id, String nome, BigDecimal valorCompra, BigDecimal controle, BigDecimal estoqueIdeal) {
        this.id = id;
        this.nome = nome;
        this.valorCompra = valorCompra;
        this.controle = controle;
        this.estoqueIdeal = estoqueIdeal;
        this.produto = new Produto(id, nome);
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

    public BigDecimal getValorCompra() {
        return valorCompra;
    }

    public void setValorCompra(BigDecimal valorCompra) {
        this.valorCompra = valorCompra;
    }

    public BigDecimal getControle() {
        return controle;
    }

    public void setControle(BigDecimal controle) {
        this.controle = controle;
    }

    public BigDecimal getEstoqueIdeal() {
        return estoqueIdeal;
    }

    public void setEstoqueIdeal(BigDecimal estoqueIdeal) {
        this.estoqueIdeal = estoqueIdeal;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
