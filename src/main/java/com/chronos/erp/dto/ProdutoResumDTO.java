package com.chronos.erp.dto;


import com.chronos.erp.modelo.entidades.Produto;
import com.chronos.erp.modelo.entidades.ProdutoSubGrupo;
import com.chronos.erp.modelo.entidades.TributGrupoTributario;
import com.chronos.erp.modelo.entidades.UnidadeProduto;

import java.io.Serializable;
import java.math.BigDecimal;


public class ProdutoResumDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private String nome;
    private String descricaoPdv;
    private String ncm;
    private String cest;
    private BigDecimal verificado;
    private BigDecimal valorVenda;
    private UnidadeProduto unidadeProduto;
    private ProdutoSubGrupo subGrupo;
    private TributGrupoTributario grupoTributario;

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

    public TributGrupoTributario getGrupoTributario() {
        return grupoTributario;
    }

    public void setGrupoTributario(TributGrupoTributario grupoTributario) {
        this.grupoTributario = grupoTributario;
    }
}
