package com.chronos.dto;

import com.chronos.modelo.entidades.Produto;
import com.chronos.modelo.entidades.TributGrupoTributario;
import com.chronos.modelo.entidades.TributIcmsCustomCab;
import com.chronos.modelo.entidades.UnidadeProduto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by john on 18/10/17.
 */
public class ProdutoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private String descricaoPdv;
    private BigDecimal valorVenda;
    private BigDecimal quantidadeEstoque;
    private BigDecimal estoqueVerificado;
    private String ncm;
    private String imagem;
    private Integer idgrupotributario;
    private Integer idicms;
    private String unidade;
    private String servico;


    private Produto produto;

    public ProdutoDTO() {
    }

    public ProdutoDTO(Integer id, String nome,String descricaoPdv, String servico, String codigoLst, BigDecimal valorVenda, BigDecimal quantidadeEstoque, BigDecimal estoqueVerificado, String ncm, String imagem, Integer idgrupotributario, Integer idicms, String unidade, String podeFracionar) {
        this.id = id;
        this.nome = nome;
        this.descricaoPdv = descricaoPdv;
        this.valorVenda = valorVenda;
        this.quantidadeEstoque = quantidadeEstoque;
        this.estoqueVerificado = estoqueVerificado;
        this.unidade = unidade;
        this.ncm = ncm;
        this.imagem = imagem;
        this.idgrupotributario = idgrupotributario;
        this.idicms = idicms;
        this.servico = servico;
        this.produto = new Produto(id, nome, servico, valorVenda, quantidadeEstoque, ncm, new UnidadeProduto(0, unidade));
        this.produto.setImagem(imagem);
        this.produto.getUnidadeProduto().setPodeFracionar(podeFracionar);
        this.produto.setCodigoLst(codigoLst);
        this.produto.setControle(estoqueVerificado);
        this.produto.setDescricaoPdv(this.descricaoPdv);
        if (idgrupotributario != null) {
            produto.setTributGrupoTributario(new TributGrupoTributario(idgrupotributario));
        } else {
            produto.setTributIcmsCustomCab(new TributIcmsCustomCab(idicms));
        }
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

    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(BigDecimal valorVenda) {
        this.valorVenda = valorVenda;
    }

    public BigDecimal getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(BigDecimal quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public BigDecimal getEstoqueVerificado() {
        return estoqueVerificado;
    }

    public void setEstoqueVerificado(BigDecimal estoqueVerificado) {
        this.estoqueVerificado = estoqueVerificado;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public Integer getIdgrupotributario() {
        return idgrupotributario;
    }

    public void setIdgrupotributario(Integer idgrupotributario) {
        this.idgrupotributario = idgrupotributario;
    }

    public Integer getIdicms() {
        return idicms;
    }

    public void setIdicms(Integer idicms) {
        this.idicms = idicms;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }


    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public String getDescricaoPdv() {
        return descricaoPdv;
    }

    public void setDescricaoPdv(String descricaoPdv) {
        this.descricaoPdv = descricaoPdv;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }
}
