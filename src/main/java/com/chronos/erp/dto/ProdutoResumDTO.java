package com.chronos.erp.dto;


import com.chronos.erp.modelo.entidades.*;

import java.io.Serializable;
import java.math.BigDecimal;


public class ProdutoResumDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private String nome;
    private String descricaoPdv;
    private String ncm;
    private String cest;
    private String gtin;
    private BigDecimal verificado;
    private BigDecimal valorVenda;
    private BigDecimal custo;
    private BigDecimal encargos;
    private BigDecimal margemLucro;
    private UnidadeProduto unidadeProduto;
    private ProdutoSubGrupo subGrupo;
    private TributGrupoTributario grupoTributario;
    private ProdutoMarca marca;

    public Produto gerarProduto() {
        Produto prod = new Produto();

        prod.setNome(nome);
        prod.setDescricaoPdv(descricaoPdv);
        prod.setNcm(ncm);
        prod.setCest(cest);
        prod.setValorVenda(valorVenda);
        prod.setTipo("V");

        prod.setUnidadeProduto(unidadeProduto);
        prod.setProdutoSubGrupo(subGrupo);
        prod.setTributGrupoTributario(grupoTributario);
        prod.setProdutoMarca(marca);
        prod.setCustoUnitario(custo);
        prod.setEncargosVenda(encargos);
        prod.setGtin(gtin);

        return prod;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricaoPdv() {
        return descricaoPdv;
    }

    public void setDescricaoPdv(String descricaoPdv) {
        this.descricaoPdv = descricaoPdv;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public String getCest() {
        return cest;
    }

    public void setCest(String cest) {
        this.cest = cest;
    }

    public BigDecimal getVerificado() {
        return verificado;
    }

    public void setVerificado(BigDecimal verificado) {
        this.verificado = verificado;
    }

    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(BigDecimal valorVenda) {
        this.valorVenda = valorVenda;
    }

    public UnidadeProduto getUnidadeProduto() {
        return unidadeProduto;
    }

    public void setUnidadeProduto(UnidadeProduto unidadeProduto) {
        this.unidadeProduto = unidadeProduto;
    }

    public ProdutoSubGrupo getSubGrupo() {
        return subGrupo;
    }

    public void setSubGrupo(ProdutoSubGrupo subGrupo) {
        this.subGrupo = subGrupo;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public TributGrupoTributario getGrupoTributario() {
        return grupoTributario;
    }

    public void setGrupoTributario(TributGrupoTributario grupoTributario) {
        this.grupoTributario = grupoTributario;
    }

    public BigDecimal getMargemLucro() {
        return margemLucro;
    }

    public void setMargemLucro(BigDecimal margemLucro) {
        this.margemLucro = margemLucro;
    }

    public BigDecimal getCusto() {
        return custo;
    }

    public void setCusto(BigDecimal custo) {
        this.custo = custo;
    }

    public BigDecimal getEncargos() {
        return encargos;
    }

    public void setEncargos(BigDecimal encargos) {
        this.encargos = encargos;
    }

    public ProdutoMarca getMarca() {
        return marca;
    }

    public void setMarca(ProdutoMarca marca) {
        this.marca = marca;
    }
}
