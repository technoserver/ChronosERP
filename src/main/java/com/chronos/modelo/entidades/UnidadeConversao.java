
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


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
    @JoinColumn(name = "ID_UNIDADE_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UnidadeProduto unidadeProduto;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private Produto produto;

    public UnidadeConversao() {
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

    @Override
    public String toString() {
        return "com.t2tierp.model.bean.cadastros.UnidadeConversao[id=" + id + "]";
    }

}
