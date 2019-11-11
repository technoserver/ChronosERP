
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name = "UNIDADE_CONVERSAO")
public class UnidadeConversao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "SIGLA")
    private String sigla;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "FATOR_CONVERSAO")
    private BigDecimal fatorConversao;
    @Column(name = "acao")
    private String acao;
    @JoinColumn(name = "ID_UNIDADE_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UnidadeProduto unidadeProduto;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private Produto produto;

    public UnidadeConversao() {
    }

    public UnidadeConversao(Integer id, String sigla, BigDecimal fatorConversao, String acao) {
        this.id = id;
        this.sigla = sigla;
        this.fatorConversao = fatorConversao;
        this.acao = acao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getFatorConversao() {
        return fatorConversao;
    }

    public void setFatorConversao(BigDecimal fatorConversao) {
        this.fatorConversao = fatorConversao;
    }

    public UnidadeProduto getUnidadeProduto() {
        return unidadeProduto;
    }

    public void setUnidadeProduto(UnidadeProduto unidadeProduto) {
        this.unidadeProduto = unidadeProduto;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnidadeConversao)) return false;
        UnidadeConversao that = (UnidadeConversao) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
