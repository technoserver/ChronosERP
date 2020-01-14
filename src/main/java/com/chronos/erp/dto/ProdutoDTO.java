package com.chronos.erp.dto;

import com.chronos.erp.modelo.entidades.Produto;
import com.chronos.erp.modelo.entidades.TributGrupoTributario;
import com.chronos.erp.modelo.entidades.UnidadeProduto;
import com.chronos.erp.modelo.enuns.PrecoPrioritario;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by john on 18/10/17.
 */
public class ProdutoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer idgrade;
    private String codigoGrade;
    private String nome;
    private String descricaoPdv;
    private BigDecimal valorVenda;
    private BigDecimal custoUnitario;
    private BigDecimal quantidadeEstoque;
    private BigDecimal estoqueVerificado;
    private String ncm;
    private String cest;
    private String imagem;
    private Integer idgrupotributario;
    private String unidade;
    private String servico;
    private BigDecimal valorVendaAtacado;
    private BigDecimal quantidadeVendaAtacado;
    private BigDecimal quantidadeVenda;
    private BigDecimal precoTabela;
    private BigDecimal precoPromocao;
    private PrecoPrioritario precoPrioritario;
    private Boolean possuiGrade;


    private Produto produto;

    public ProdutoDTO() {

    }

    public ProdutoDTO(Integer id, String nome, BigDecimal custoUnitario, BigDecimal quantidadeEstoque, BigDecimal estoqueVerificado, String ncm, String unidade) {
        this.id = id;
        this.nome = nome;
        this.custoUnitario = custoUnitario;
        this.quantidadeEstoque = quantidadeEstoque;
        this.estoqueVerificado = estoqueVerificado;
        this.unidade = unidade;
        this.ncm = ncm;
        this.produto = new Produto(id, nome);
        this.produto.setControle(estoqueVerificado);
        this.produto.setCustoUnitario(custoUnitario);
        this.produto.setQuantidadeEstoque(quantidadeEstoque);
        this.produto.setNcm(ncm);
        this.produto.setUnidadeProduto(new UnidadeProduto(0, unidade));

    }


    public ProdutoDTO(Integer id, Integer idgrade, String nome, String descricaoPdv, String servico, String codigoLst, BigDecimal valorVenda,
                      BigDecimal quantidadeEstoque, BigDecimal estoqueVerificado, String ncm, String cest, String imagem,
                      Integer idgrupotributario, String unidade, String podeFracionar, BigDecimal precoPromocao,
                      PrecoPrioritario precoPrioritario, BigDecimal quantidadeVendaAtacado, BigDecimal valorVendaAtacado, boolean possuiGrade) {
        this.id = id;
        this.idgrade = idgrade;
        this.nome = nome;
        this.descricaoPdv = descricaoPdv;
        this.valorVenda = valorVenda;
        this.precoPromocao = precoPromocao;
        this.precoTabela = BigDecimal.ZERO;
        this.precoPrioritario = precoPrioritario;
        this.valorVendaAtacado = valorVendaAtacado;
        this.quantidadeVendaAtacado = quantidadeVendaAtacado;

        this.quantidadeEstoque = quantidadeEstoque;
        this.estoqueVerificado = estoqueVerificado;
        this.unidade = unidade;
        this.ncm = ncm;
        this.cest = cest;
        this.imagem = imagem;
        this.idgrupotributario = idgrupotributario;
        this.possuiGrade = possuiGrade;

        this.servico = servico;
        this.produto = new Produto(id, nome, servico, valorVenda, quantidadeEstoque, ncm, new UnidadeProduto(0, unidade));
        this.produto.setImagem(imagem);
        this.produto.setCest(cest);
        this.produto.getUnidadeProduto().setPodeFracionar(podeFracionar);
        this.produto.setCodigoLst(codigoLst);
        this.produto.setControle(estoqueVerificado);
        this.produto.setDescricaoPdv(this.descricaoPdv);
        this.produto.setPossuiGrade(this.possuiGrade);

        produto.setTributGrupoTributario(new TributGrupoTributario(idgrupotributario));

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdgrade() {
        return idgrade;
    }

    public void setIdgrade(Integer idgrade) {
        this.idgrade = idgrade;
    }

    public String getCodigoGrade() {
        return codigoGrade;
    }

    public void setCodigoGrade(String codigoGrade) {
        this.codigoGrade = codigoGrade;
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

    public BigDecimal getCustoUnitario() {
        return custoUnitario;
    }

    public void setCustoUnitario(BigDecimal custoUnitario) {
        this.custoUnitario = custoUnitario;
    }

    public BigDecimal getValorVendaAtacado() {
        return valorVendaAtacado;
    }

    public void setValorVendaAtacado(BigDecimal valorVendaAtacado) {
        this.valorVendaAtacado = valorVendaAtacado;
    }

    public BigDecimal getQuantidadeVendaAtacado() {
        return quantidadeVendaAtacado;
    }

    public void setQuantidadeVendaAtacado(BigDecimal quantidadeVendaAtacado) {
        this.quantidadeVendaAtacado = quantidadeVendaAtacado;
    }

    public BigDecimal getPrecoTabela() {
        return precoTabela;
    }

    public void setPrecoTabela(BigDecimal precoTabela) {
        this.precoTabela = precoTabela;
    }

    public BigDecimal getPrecoPromocao() {
        return precoPromocao;
    }

    public void setPrecoPromocao(BigDecimal precoPromocao) {
        this.precoPromocao = precoPromocao;
    }

    public PrecoPrioritario getPrecoPrioritario() {
        return precoPrioritario;
    }

    public void setPrecoPrioritario(PrecoPrioritario precoPrioritario) {
        this.precoPrioritario = precoPrioritario;
    }

    public BigDecimal getQuantidadeVenda() {
        return quantidadeVenda;
    }

    public void setQuantidadeVenda(BigDecimal quantidadeVenda) {
        this.quantidadeVenda = quantidadeVenda;
    }

    public Boolean getPossuiGrade() {
        return possuiGrade;
    }

    public void setPossuiGrade(Boolean possuiGrade) {
        this.possuiGrade = possuiGrade;
    }
}
