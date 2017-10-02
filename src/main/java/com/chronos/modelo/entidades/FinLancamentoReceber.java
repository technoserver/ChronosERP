
package com.chronos.modelo.entidades;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "FIN_LANCAMENTO_RECEBER")
public class FinLancamentoReceber implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "QUANTIDADE_PARCELA")
    @Min(1)
    @NotNull
    private Integer quantidadeParcela;
    @Column(name = "VALOR_TOTAL")
    @DecimalMin(value = "0.01", message = "O valor  deve ser maior que R$0,01")
    @DecimalMax(value = "9999999.99", message = "O valor  deve ser menor que R$9.999.999,99")
    @NotNull
    private BigDecimal valorTotal;
    @Column(name = "VALOR_DESCONTO_CONVENIO")
    private BigDecimal valorDescontoConvenio;
    @NotNull
    @Column(name = "VALOR_A_RECEBER")
    @DecimalMin(value = "0.01", message = "O valor  deve ser maior que R$0,01")
    @DecimalMax(value = "9999999.99", message = "O valor  deve ser menor que R$9.999.999,99")
    private BigDecimal valorAReceber;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_LANCAMENTO")
    private Date dataLancamento;
    @NotNull
    @NotBlank
    @Column(name = "NUMERO_DOCUMENTO")
    private String numeroDocumento;
    @Temporal(TemporalType.DATE)
    @Column(name = "PRIMEIRO_VENCIMENTO")
    @NotNull
    private Date primeiroVencimento;
    @Column(name = "TAXA_COMISSAO")
    @DecimalMax(value = "100.0", message = "O deve deve ser igual ou menor que 100")
    private BigDecimal taxaComissao;
    @Column(name = "VALOR_COMISSAO")
    private BigDecimal valorComissao;
    @Column(name = "INTERVALO_ENTRE_PARCELAS")
    private Integer intervaloEntreParcelas;
    @Column(name = "CODIGO_MODULO_LCTO")
    private String codigoModuloLcto;
    @Column(name = "MESCLADO_PARA")
    private Integer mescladoPara;
    @JoinColumn(name = "ID_FIN_DOCUMENTO_ORIGEM", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FinDocumentoOrigem finDocumentoOrigem;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Cliente cliente;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "finLancamentoReceber", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FinParcelaReceber> listaFinParcelaReceber;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "finLancamentoReceber", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FinLctoReceberNtFinanceira> listaFinLctoReceberNtFinanceira;

    public FinLancamentoReceber() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantidadeParcela() {
        return quantidadeParcela;
    }

    public void setQuantidadeParcela(Integer quantidadeParcela) {
        this.quantidadeParcela = quantidadeParcela;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorDescontoConvenio() {
        return valorDescontoConvenio;
    }

    public void setValorDescontoConvenio(BigDecimal valorDescontoConvenio) {
        this.valorDescontoConvenio = valorDescontoConvenio;
    }

    public BigDecimal getValorAReceber() {
        return valorAReceber;
    }

    public void setValorAReceber(BigDecimal valorAReceber) {
        this.valorAReceber = valorAReceber;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Date getPrimeiroVencimento() {
        return primeiroVencimento;
    }

    public void setPrimeiroVencimento(Date primeiroVencimento) {
        this.primeiroVencimento = primeiroVencimento;
    }

    public BigDecimal getTaxaComissao() {
        return taxaComissao;
    }

    public void setTaxaComissao(BigDecimal taxaComissao) {
        this.taxaComissao = taxaComissao;
    }

    public BigDecimal getValorComissao() {
        return valorComissao;
    }

    public void setValorComissao(BigDecimal valorComissao) {
        this.valorComissao = valorComissao;
    }

    public Integer getIntervaloEntreParcelas() {
        return intervaloEntreParcelas;
    }

    public void setIntervaloEntreParcelas(Integer intervaloEntreParcelas) {
        this.intervaloEntreParcelas = intervaloEntreParcelas;
    }

    public String getCodigoModuloLcto() {
        return codigoModuloLcto;
    }

    public void setCodigoModuloLcto(String codigoModuloLcto) {
        this.codigoModuloLcto = codigoModuloLcto;
    }

    public Integer getMescladoPara() {
        return mescladoPara;
    }

    public void setMescladoPara(Integer mescladoPara) {
        this.mescladoPara = mescladoPara;
    }

    public FinDocumentoOrigem getFinDocumentoOrigem() {
        return finDocumentoOrigem;
    }

    public void setFinDocumentoOrigem(FinDocumentoOrigem finDocumentoOrigem) {
        this.finDocumentoOrigem = finDocumentoOrigem;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Set<FinParcelaReceber> getListaFinParcelaReceber() {
        return listaFinParcelaReceber;
    }

    public void setListaFinParcelaReceber(Set<FinParcelaReceber> listaFinParcelaReceber) {
        this.listaFinParcelaReceber = listaFinParcelaReceber;
    }

    public Set<FinLctoReceberNtFinanceira> getListaFinLctoReceberNtFinanceira() {
        return listaFinLctoReceberNtFinanceira;
    }

    public void setListaFinLctoReceberNtFinanceira(Set<FinLctoReceberNtFinanceira> listaFinLctoReceberNtFinanceira) {
        this.listaFinLctoReceberNtFinanceira = listaFinLctoReceberNtFinanceira;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public String toString() {
        return numeroDocumento;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FinLancamentoReceber other = (FinLancamentoReceber) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    

}
