package com.chronos.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "produto_promocao")
public class ProdutoPromocao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "data_inicio")
    @Temporal(TemporalType.DATE)
    private Date dataInicio;
    @Column(name = "data_fim")
    @Temporal(TemporalType.DATE)
    private Date dataFim;
    @Column(name = "quantidade_em_promocao")
    @DecimalMin(value = "0.01", message = "A quantidade deve ser maior que R0,01")
    @DecimalMax(value = "9999999.99", message = "A quantidade deve ser menor que 9.999.999,99")
    @NotNull
    private BigDecimal quantidadeEmPromocao;
    @Column(name = "quantidade_maxima_cliente")
    private BigDecimal quantidadeMaximaCliente;
    @Column(name = "valor")
    @DecimalMin(value = "0.01", message = "O valor  deve ser maior que R$0,01")
    @DecimalMax(value = "9999999.99", message = "O valor  deve ser menor que R$9.999.999,99")
    @NotNull
    private BigDecimal valor;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public BigDecimal getQuantidadeEmPromocao() {
        return quantidadeEmPromocao;
    }

    public void setQuantidadeEmPromocao(BigDecimal quantidadeEmPromocao) {
        this.quantidadeEmPromocao = quantidadeEmPromocao;
    }

    public BigDecimal getQuantidadeMaximaCliente() {
        return quantidadeMaximaCliente;
    }

    public void setQuantidadeMaximaCliente(BigDecimal quantidadeMaximaCliente) {
        this.quantidadeMaximaCliente = quantidadeMaximaCliente;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProdutoPromocao)) return false;
        ProdutoPromocao that = (ProdutoPromocao) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
