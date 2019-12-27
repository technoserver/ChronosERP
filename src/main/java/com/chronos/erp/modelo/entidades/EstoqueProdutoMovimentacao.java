package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
    @Column(name = "data_movimento")
    @Temporal(TemporalType.DATE)
    private Date dataMovimento;
    @Column(name = "QUANTIDADE_ANTERIOR")
    private BigDecimal quantidadeAnterior;


    @Column(name = "tipo")
    private String tipo;

    @Column(name = "ENTRADA_SAIDA")
    private String entradaSaida;

    @JoinColumn(name = "ID_EMPRESA_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EmpresaProduto empresaProduto;

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

    public Date getDataMovimento() {
        return dataMovimento;
    }

    public void setDataMovimento(Date dataMovimento) {
        this.dataMovimento = dataMovimento;
    }

    public BigDecimal getQuantidadeAnterior() {
        return quantidadeAnterior;
    }

    public void setQuantidadeAnterior(BigDecimal quantidadeAnterior) {
        this.quantidadeAnterior = quantidadeAnterior;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEntradaSaida() {
        return entradaSaida;
    }

    public void setEntradaSaida(String entradaSaida) {
        this.entradaSaida = entradaSaida;
    }

    public EmpresaProduto getEmpresaProduto() {
        return empresaProduto;
    }

    public void setEmpresaProduto(EmpresaProduto empresaProduto) {
        this.empresaProduto = empresaProduto;
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
