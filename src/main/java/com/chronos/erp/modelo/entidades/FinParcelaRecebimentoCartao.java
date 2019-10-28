package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "fin_parcela_recebimento_cartao")
public class FinParcelaRecebimentoCartao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "data_lancamento")
    @NotNull
    private Date dataLancamento;
    @Column(name = "data_previsao")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date dataPrevisao;
    @Column(name = "DATA_RECEBIMENTO")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date dataRecebimento;
    @Column(name = "valor_bruto")
    @NotNull
    private BigDecimal valorBruto;
    @Column(name = "valor_encargos")
    @NotNull
    private BigDecimal valorEncargos;
    @Column(name = "valor_liquido")
    @NotNull
    private BigDecimal valorLiquido;
    @JoinColumn(name = "id_fin_parcela_receber_cartao", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private FinParcelaReceberCartao finParcelaReceberCartao;
    @JoinColumn(name = "ID_CONTA_CAIXA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private ContaCaixa contaCaixa;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public Date getDataPrevisao() {
        return dataPrevisao;
    }

    public void setDataPrevisao(Date dataPrevisao) {
        this.dataPrevisao = dataPrevisao;
    }

    public Date getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(Date dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public BigDecimal getValorBruto() {
        return valorBruto;
    }

    public void setValorBruto(BigDecimal valorBruto) {
        this.valorBruto = valorBruto;
    }

    public BigDecimal getValorEncargos() {
        return valorEncargos;
    }

    public void setValorEncargos(BigDecimal valorEncargos) {
        this.valorEncargos = valorEncargos;
    }

    public BigDecimal getValorLiquido() {
        return valorLiquido;
    }

    public void setValorLiquido(BigDecimal valorLiquido) {
        this.valorLiquido = valorLiquido;
    }

    public FinParcelaReceberCartao getFinParcelaReceberCartao() {
        return finParcelaReceberCartao;
    }

    public void setFinParcelaReceberCartao(FinParcelaReceberCartao finParcelaReceberCartao) {
        this.finParcelaReceberCartao = finParcelaReceberCartao;
    }

    public ContaCaixa getContaCaixa() {
        return contaCaixa;
    }

    public void setContaCaixa(ContaCaixa contaCaixa) {
        this.contaCaixa = contaCaixa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FinParcelaRecebimentoCartao)) return false;
        FinParcelaRecebimentoCartao that = (FinParcelaRecebimentoCartao) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
