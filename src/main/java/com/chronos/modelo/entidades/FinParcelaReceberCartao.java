package com.chronos.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by john on 15/03/18.
 */
@Entity
@Table(name = "fin_parcela_receber_cartao")
@NamedQuery(name = "FinParcelaReceberCartao.UpdatePagamento"
        , query = "UPDATE FinParcelaReceberCartao o SET o.pago = true,o.dataRecebimento=?1 where o.id = ?2")
public class FinParcelaReceberCartao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NUMERO_PARCELA")
    private Integer numeroParcela;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_EMISSAO")
    private Date dataEmissao;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_VENCIMENTO")
    private Date dataVencimento;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_RECEBIMENTO")
    private Date dataRecebimento;


    @Column(name = "valor_bruto")
    @DecimalMin(value = "0.01", message = "O valor  deve ser maior que R$0,01")
    @DecimalMax(value = "9999999.99", message = "O valor  deve ser menor que R$9.999.999,99")
    @NotNull
    private BigDecimal valorBruto;
    @Column(name = "taxa_aplicada")
    @DecimalMin(value = "0.01", message = "O valor  deve ser maior que R$0,01")
    @DecimalMax(value = "9999999.99", message = "O valor  deve ser menor que R$9.999.999,99")
    @NotNull
    private BigDecimal taxaAplicada;
    @Column(name = "valor_encargos")
    @DecimalMin(value = "0.01", message = "O valor  deve ser maior que R$0,01")
    @DecimalMax(value = "9999999.99", message = "O valor  deve ser menor que R$9.999.999,99")
    @NotNull
    private BigDecimal valorEncargos;
    @Column(name = "valor_liquido")
    @DecimalMin(value = "0.01", message = "O valor  deve ser maior que R$0,01")
    @DecimalMax(value = "9999999.99", message = "O valor  deve ser menor que R$9.999.999,99")
    @NotNull
    private BigDecimal valorLiquido;
    @Column(name = "pago")
    private Boolean pago;

    @JoinColumn(name = "ID_CONTA_CAIXA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ContaCaixa contaCaixa;

    @JoinColumn(name = "ID_FIN_LANCAMENTO_RECEBER_CARTAO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FinLancamentoReceberCartao finLancamentoReceberCartao;


    public FinParcelaReceberCartao() {
    }

    public FinParcelaReceberCartao(Integer id, Integer numeroParcela, Date dataEmissao, Date dataVencimento, Date dataRecebimento, BigDecimal valorBruto, BigDecimal taxaAplicada, BigDecimal valorEncargos, BigDecimal valorLiquido, Boolean pago, Integer idcontaCaixa, Integer idfinLancamentoReceberCartao) {
        this.id = id;
        this.numeroParcela = numeroParcela;
        this.dataEmissao = dataEmissao;
        this.dataVencimento = dataVencimento;
        this.dataRecebimento = dataRecebimento;
        this.valorBruto = valorBruto;
        this.taxaAplicada = taxaAplicada;
        this.valorEncargos = valorEncargos;
        this.valorLiquido = valorLiquido;
        this.pago = pago;
        this.contaCaixa = new ContaCaixa(idcontaCaixa);
        this.finLancamentoReceberCartao = new FinLancamentoReceberCartao(idfinLancamentoReceberCartao);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(Integer numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
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

    public BigDecimal getTaxaAplicada() {
        return taxaAplicada;
    }

    public void setTaxaAplicada(BigDecimal taxaAplicada) {
        this.taxaAplicada = taxaAplicada;
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

    public Boolean getPago() {
        return pago;
    }

    public void setPago(Boolean pago) {
        this.pago = pago;
    }

    public ContaCaixa getContaCaixa() {
        return contaCaixa;
    }

    public void setContaCaixa(ContaCaixa contaCaixa) {
        this.contaCaixa = contaCaixa;
    }

    public FinLancamentoReceberCartao getFinLancamentoReceberCartao() {
        return finLancamentoReceberCartao;
    }

    public void setFinLancamentoReceberCartao(FinLancamentoReceberCartao finLancamentoReceberCartao) {
        this.finLancamentoReceberCartao = finLancamentoReceberCartao;
    }

    public boolean isVencido() {
        Date hoje = new Date();
        return dataVencimento.before(hoje);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FinParcelaReceberCartao)) return false;

        FinParcelaReceberCartao that = (FinParcelaReceberCartao) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
