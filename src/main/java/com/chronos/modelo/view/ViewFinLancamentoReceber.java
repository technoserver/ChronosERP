package com.chronos.modelo.view;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

/**
 * Created by john on 11/10/17.
 */
@Entity
@Table(name = "view_fin_lancamento_receber")
public class ViewFinLancamentoReceber implements Serializable, Comparable<ViewFinLancamentoReceber> {


    private static final long serialVersionUID = 1L;
    @Column(name = "id")
    private Integer id;
    @Column(name = "id_cliente")
    private Integer idCliente;
    @Column(name = "nome", length = 150)
    private String nome;
    @Column(name = "cpf_cnpj", length = 2147483647)
    private String cpfCnpj;
    @Column(name = "data_lancamento")
    @Temporal(TemporalType.DATE)
    private Date dataLancamento;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_lancamento", precision = 18, scale = 6)
    private BigDecimal valorLancamento;
    @Column(name = "quantidade_parcela")
    private Integer quantidadeParcela;
    @Column(name = "numero_documento", length = 50)
    private String numeroDocumento;
    @Column(name = "numero_parcela")
    private Integer numeroParcela;
    @Column(name = "data_vencimento")
    @Temporal(TemporalType.DATE)
    private Date dataVencimento;
    @Column(name = "valor_parcela", precision = 18, scale = 6)
    private BigDecimal valorParcela;
    @Column(name = "taxa_juro", precision = 18, scale = 6)
    private BigDecimal taxaJuro;
    @Column(name = "valor_juro", precision = 18, scale = 6)
    private BigDecimal valorJuro;
    @Column(name = "taxa_multa", precision = 18, scale = 6)
    private BigDecimal taxaMulta;
    @Column(name = "valor_multa", precision = 18, scale = 6)
    private BigDecimal valorMulta;
    @Column(name = "taxa_desconto", precision = 18, scale = 6)
    private BigDecimal taxaDesconto;
    @Column(name = "valor_desconto", precision = 18, scale = 6)
    private BigDecimal valorDesconto;


    @Column(name = "multa_recebido")
    private BigDecimal multaRecebida;
    @Column(name = "juros_recebido")
    private BigDecimal jurosRecebido;
    @Column(name = "valor_recebido")
    private BigDecimal valorRecebido;

    @Transient
    private BigDecimal saldo;
    @Column(name = "id_status_parcela")
    private Integer idStatusParcela;
    @Column(name = "situacao_parcela", length = 2)
    private String situacaoParcela;
    @Column(name = "descricao_situacao_parcela", length = 30)
    private String descricaoSituacaoParcela;
    @Column(name = "id_conta_caixa")
    private Integer idContaCaixa;
    @Column(name = "nome_conta_caixa", length = 50)
    private String nomeContaCaixa;
    @Column(name = "limite_cobranca_juro")
    private Integer limiteCobrancaJuro;
    @Id
    @Column(name = "id_parcela_receber")
    private Integer idParcelaReceber;
    @Column(name = "sigla_documento", length = 10)
    private String siglaDocumento;
    @Transient
    private BigDecimal valorAPagar;
    @Transient
    private BigDecimal valorTotal;
    @Transient
    private BigDecimal valorAtualizado;

    public ViewFinLancamentoReceber() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public BigDecimal getValorLancamento() {
        return valorLancamento;
    }

    public void setValorLancamento(BigDecimal valorLancamento) {
        this.valorLancamento = valorLancamento;
    }

    public Integer getQuantidadeParcela() {
        return quantidadeParcela;
    }

    public void setQuantidadeParcela(Integer quantidadeParcela) {
        this.quantidadeParcela = quantidadeParcela;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Integer getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(Integer numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public BigDecimal getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(BigDecimal valorParcela) {
        this.valorParcela = valorParcela;
    }

    public BigDecimal getTaxaJuro() {
        return Optional.ofNullable(taxaJuro).orElse(BigDecimal.ZERO);
    }

    public void setTaxaJuro(BigDecimal taxaJuro) {
        this.taxaJuro = taxaJuro;
    }

    public BigDecimal getValorJuro() {
        return Optional.ofNullable(valorJuro).orElse(BigDecimal.ZERO);

    }

    public void setValorJuro(BigDecimal valorJuro) {
        this.valorJuro = valorJuro;
    }

    public BigDecimal getTaxaMulta() {
        return Optional.ofNullable(this.taxaMulta).orElse(BigDecimal.ZERO);
    }

    public void setTaxaMulta(BigDecimal taxaMulta) {
        this.taxaMulta = taxaMulta;
    }

    public BigDecimal getValorMulta() {
        return Optional.ofNullable(valorMulta).orElse(BigDecimal.ZERO);
    }

    public void setValorMulta(BigDecimal valorMulta) {
        this.valorMulta = valorMulta;
    }

    public BigDecimal getTaxaDesconto() {
        return taxaDesconto;
    }

    public void setTaxaDesconto(BigDecimal taxaDesconto) {
        this.taxaDesconto = taxaDesconto;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getSaldo() {
        saldo = valorParcela
                .subtract(getValorRecebido());
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Integer getIdStatusParcela() {
        return idStatusParcela;
    }

    public void setIdStatusParcela(Integer idStatusParcela) {
        this.idStatusParcela = idStatusParcela;
    }

    public String getSituacaoParcela() {
        return situacaoParcela;
    }

    public void setSituacaoParcela(String situacaoParcela) {
        this.situacaoParcela = situacaoParcela;
    }

    public String getDescricaoSituacaoParcela() {
        return descricaoSituacaoParcela;
    }

    public void setDescricaoSituacaoParcela(String descricaoSituacaoParcela) {
        this.descricaoSituacaoParcela = descricaoSituacaoParcela;
    }

    public Integer getIdContaCaixa() {
        return idContaCaixa;
    }

    public void setIdContaCaixa(Integer idContaCaixa) {
        this.idContaCaixa = idContaCaixa;
    }

    public String getNomeContaCaixa() {
        return nomeContaCaixa;
    }

    public void setNomeContaCaixa(String nomeContaCaixa) {
        this.nomeContaCaixa = nomeContaCaixa;
    }

    public Integer getIdParcelaReceber() {
        return idParcelaReceber;
    }

    public void setIdParcelaReceber(Integer idParcelaReceber) {
        this.idParcelaReceber = idParcelaReceber;
    }

    public String getSiglaDocumento() {
        return siglaDocumento;
    }

    public void setSiglaDocumento(String siglaDocumento) {
        this.siglaDocumento = siglaDocumento;
    }

    public Integer getLimiteCobrancaJuro() {
        return Optional.ofNullable(limiteCobrancaJuro).orElse(0);
    }

    public void setLimiteCobrancaJuro(Integer limiteCobrancaJuro) {
        this.limiteCobrancaJuro = limiteCobrancaJuro;
    }


    public BigDecimal getValorAPagar() {
        valorAPagar = getSaldo();
        return Optional.ofNullable(valorAPagar).orElse(BigDecimal.ZERO);
    }

    public void setValorAPagar(BigDecimal valorAPagar) {
        this.valorAPagar = valorAPagar;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getMultaRecebida() {
        return Optional.ofNullable(multaRecebida).orElse(BigDecimal.ZERO);
    }

    public void setMultaRecebida(BigDecimal multaRecebida) {
        this.multaRecebida = multaRecebida;
    }

    public BigDecimal getJurosRecebido() {
        return Optional.ofNullable(jurosRecebido).orElse(BigDecimal.ZERO);
    }

    public void setJurosRecebido(BigDecimal jurosRecebido) {
        this.jurosRecebido = jurosRecebido;
    }

    public BigDecimal getValorRecebido() {
        return Optional.ofNullable(valorRecebido).orElse(BigDecimal.ZERO);
    }

    public void setValorRecebido(BigDecimal valorRecebido) {
        this.valorRecebido = valorRecebido;
    }


    public BigDecimal getValorAtualizado() {
        valorAtualizado = valorParcela.add(getValorJuro());
        return valorAtualizado;
    }

    public void setValorAtualizado(BigDecimal valorAtualizado) {
        this.valorAtualizado = valorAtualizado;
    }

    @Transient
    public Calendar getDataRecebimento() {
        Calendar dataRecebimento = Calendar.getInstance();
        dataRecebimento.setTime(new Date());
        return dataRecebimento;
    }

    @Transient
    public Calendar getVencimento() {
        Calendar vencimento = Calendar.getInstance();
        vencimento.setTime(dataVencimento);
        return vencimento;
    }

    @Transient
    public Long getDiasAtraso() {
        long diasAtraso = (getDataRecebimento().getTimeInMillis()
                - getVencimento().getTimeInMillis()) / 86400000l;
        return diasAtraso;
    }

    @Transient
    public BigDecimal calcularValorJuros() {

        valorJuro = temJuros()
                ? getSaldo().multiply(getTaxaJuro().divide(BigDecimal.valueOf(100)))
                .multiply(BigDecimal.valueOf(getDiasAtraso()))
                : BigDecimal.ZERO;


        return valorJuro;
    }

    @Transient
    public BigDecimal calcularValorTotal() {
        valorAPagar = BigDecimal.ZERO;
        valorAPagar = getSaldo().add(Optional.ofNullable(getValorJuro()).orElse(BigDecimal.ZERO));
        return valorAPagar;
    }


    @Transient
    public boolean temMulta() {
        return getTaxaMulta().compareTo(BigDecimal.ZERO) > 0 && getDiasAtraso() > 0 && !this.situacaoParcela.equals("02");
    }

    @Transient
    public boolean temJuros() {
        return (getTaxaJuro().compareTo(BigDecimal.ZERO) > 0) && (getDiasAtraso() > getLimiteCobrancaJuro()) && (!this.situacaoParcela.equals("02"));
    }

    @Transient
    public boolean isVencido() {
        return getDiasAtraso() > 0 && !this.situacaoParcela.equals("02");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewFinLancamentoReceber)) return false;

        ViewFinLancamentoReceber that = (ViewFinLancamentoReceber) o;

        return getIdParcelaReceber().equals(that.getIdParcelaReceber());
    }

    @Override
    public int hashCode() {
        return getIdParcelaReceber().hashCode();
    }


    @Override
    public String toString() {
        return this.idParcelaReceber.toString();
    }

    @Override
    public int compareTo(ViewFinLancamentoReceber outro) {

        if (this.dataVencimento.before(outro.dataVencimento)) {
            return -1;
        }
        if (!this.dataVencimento.before(outro.dataVencimento)) {
            return 1;
        }

        return 0;
    }
}
