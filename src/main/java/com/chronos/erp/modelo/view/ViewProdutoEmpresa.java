package com.chronos.erp.modelo.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by john on 12/09/17.
 */
@Entity
@Table(name = "view_produto_empresa")
public class ViewProdutoEmpresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "idempresa")
    private Integer idempresa;
    @Column(name = "idsubgrupo")
    private Integer idsubgrupo;
    @Column(name = "idgrupo")
    private Integer idgrupo;
    @Column(name = "idmarca")
    private Integer idmarca;
    @Column(name = "idunidade")
    private Integer idunidade;
    @Column(name = "gtin", length = 14)
    private String gtin;
    @Column(name = "nome", length = 100)
    private String nome;
    @Column(name = "ncm", length = 8)
    private String ncm;
    @Column(name = "cest", length = 7)
    private String cest;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "custo_unitario", precision = 18, scale = 6)
    private BigDecimal custoUnitario;
    @Column(name = "valor_venda", precision = 18, scale = 6)
    private BigDecimal valorVenda;
    @Column(name = "servico")
    private Character servico;
    @Column(name = "tipo")
    private Character tipo;
    @Column(name = "imagem")
    private String imagem;
    @Column(name = "quantidade", precision = 18, scale = 6)
    private BigDecimal quantidade;
    @Column(name = "estoque_verificado", precision = 18, scale = 6)
    private BigDecimal estoqueVerificado;
    @Column(name = "inativo")
    private String inativo;
    @Column(name = "excluido")
    private String excluido;
    @Column(name = "subgrupo", length = 100)
    private String subgrupo;
    @Column(name = "grupo", length = 100)
    private String grupo;
    @Column(name = "marca", length = 50)
    private String marca;
    @Column(name = "sigla", length = 10)
    private String sigla;

    public ViewProdutoEmpresa() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdempresa() {
        return idempresa;
    }

    public void setIdempresa(Integer idempresa) {
        this.idempresa = idempresa;
    }

    public Integer getIdsubgrupo() {
        return idsubgrupo;
    }

    public void setIdsubgrupo(Integer idsubgrupo) {
        this.idsubgrupo = idsubgrupo;
    }

    public Integer getIdgrupo() {
        return idgrupo;
    }

    public void setIdgrupo(Integer idgrupo) {
        this.idgrupo = idgrupo;
    }

    public Integer getIdmarca() {
        return idmarca;
    }

    public void setIdmarca(Integer idmarca) {
        this.idmarca = idmarca;
    }

    public Integer getIdunidade() {
        return idunidade;
    }

    public void setIdunidade(Integer idunidade) {
        this.idunidade = idunidade;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public BigDecimal getCustoUnitario() {
        return custoUnitario;
    }

    public void setCustoUnitario(BigDecimal custoUnitario) {
        this.custoUnitario = custoUnitario;
    }

    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(BigDecimal valorVenda) {
        this.valorVenda = valorVenda;
    }

    public Character getServico() {
        return servico;
    }

    public void setServico(Character servico) {
        this.servico = servico;
    }

    public Character getTipo() {
        return tipo;
    }

    public void setTipo(Character tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public String getInativo() {
        return inativo;
    }

    public void setInativo(String inativo) {
        this.inativo = inativo;
    }

    public String getSubgrupo() {
        return subgrupo;
    }

    public void setSubgrupo(String subgrupo) {
        this.subgrupo = subgrupo;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getExcluido() {
        return excluido;
    }

    public void setExcluido(String excluido) {
        this.excluido = excluido;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public BigDecimal getEstoqueVerificado() {
        return estoqueVerificado;
    }

    public void setEstoqueVerificado(BigDecimal estoqueVerificado) {
        this.estoqueVerificado = estoqueVerificado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewProdutoEmpresa)) return false;

        ViewProdutoEmpresa that = (ViewProdutoEmpresa) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
