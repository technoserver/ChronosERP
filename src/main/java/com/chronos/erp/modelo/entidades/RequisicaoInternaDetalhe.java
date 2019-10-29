
package com.chronos.erp.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "REQUISICAO_INTERNA_DETALHE")
public class RequisicaoInternaDetalhe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "QUANTIDADE")
    private BigDecimal quantidade;
    @JoinColumn(name = "ID_REQ_INTERNA_CABECALHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private RequisicaoInternaCabecalho requisicaoInternaCabecalho;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Produto produto;

    public RequisicaoInternaDetalhe() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public RequisicaoInternaCabecalho getRequisicaoInternaCabecalho() {
        return requisicaoInternaCabecalho;
    }

    public void setRequisicaoInternaCabecalho(RequisicaoInternaCabecalho requisicaoInternaCabecalho) {
        this.requisicaoInternaCabecalho = requisicaoInternaCabecalho;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public String toString() {
        return "RequisicaoInternaDetalhe{" + "id=" + id + '}';
    }


}
