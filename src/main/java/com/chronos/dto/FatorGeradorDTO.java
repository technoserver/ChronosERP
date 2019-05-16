package com.chronos.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class FatorGeradorDTO {

    private Integer id;
    private Date dataVenda;
    private BigDecimal valor;
    private String vendedor;
    private String numNota;
    private Date dataNota;
    private String doc;
    private List<ItemFatorGerador> itens;

    public FatorGeradorDTO() {
    }

    public FatorGeradorDTO(Integer id, Date dataVenda, BigDecimal valor, String vendedor, String numNota, Date dataNota) {
        this.id = id;
        this.dataVenda = dataVenda;
        this.valor = valor;
        this.vendedor = vendedor;
        this.numNota = numNota;
        this.dataNota = dataNota;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getNumNota() {
        return numNota;
    }

    public void setNumNota(String numNota) {
        this.numNota = numNota;
    }

    public Date getDataNota() {
        return dataNota;
    }

    public void setDataNota(Date dataNota) {
        this.dataNota = dataNota;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public List<ItemFatorGerador> getItens() {
        return itens;
    }

    public void setItens(List<ItemFatorGerador> itens) {
        this.itens = itens;
    }

    public void addItem(String produto, BigDecimal quantidade, BigDecimal valor) {
        this.itens.add(new ItemFatorGerador(produto, quantidade, valor));
    }

    public class ItemFatorGerador {

        private String produto;
        private BigDecimal quantidade;
        private BigDecimal valor;

        public ItemFatorGerador(String produto, BigDecimal quantidade, BigDecimal valor) {
            this.produto = produto;
            this.quantidade = quantidade;
            this.valor = valor;
        }

        public String getProduto() {
            return produto;
        }

        public void setProduto(String produto) {
            this.produto = produto;
        }

        public BigDecimal getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(BigDecimal quantidade) {
            this.quantidade = quantidade;
        }

        public BigDecimal getValor() {
            return valor;
        }

        public void setValor(BigDecimal valor) {
            this.valor = valor;
        }
    }
}
