package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by john on 12/12/17.
 */
@Entity
@Table(name = "estoque_produto_movimentacao")
public class EstoqueProdutoMovimentacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "DOCUMENTO")
    private String documento;

    @Column(name = "QUANTIDADE")
    private BigDecimal quantidade;

    @Column(name = "VALOR_CUSTO")
    private BigDecimal valorCusto;

    @Column(name = "SALDO_FISICO")
    private BigDecimal saldoFisico;

    @Column(name = "SALDO_FINANCEIRO")
    private BigDecimal saldoFinanceiro;

    @Column(name = "ENTRADA_SAIDA")
    private String entradaSaida;

    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Produto produto;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorCusto() {
        return valorCusto;
    }

    public void setValorCusto(BigDecimal valorCusto) {
        this.valorCusto = valorCusto;
    }

    public BigDecimal getSaldoFisico() {
        return saldoFisico;
    }

    public void setSaldoFisico(BigDecimal saldoFisico) {
        this.saldoFisico = saldoFisico;
    }

    public BigDecimal getSaldoFinanceiro() {
        return saldoFinanceiro;
    }

    public void setSaldoFinanceiro(BigDecimal saldoFinanceiro) {
        this.saldoFinanceiro = saldoFinanceiro;
    }

    public String getEntradaSaida() {
        return entradaSaida;
    }

    public void setEntradaSaida(String entradaSaida) {
        this.entradaSaida = entradaSaida;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EstoqueProdutoMovimentacao)) return false;

        EstoqueProdutoMovimentacao that = (EstoqueProdutoMovimentacao) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
