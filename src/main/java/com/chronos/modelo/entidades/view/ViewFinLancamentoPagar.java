package com.chronos.modelo.entidades.view;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;

/**
 * Created by john on 01/11/17.
 */
@Entity
@Table(name = "view_fin_lancamento_pagar")
public class ViewFinLancamentoPagar implements Serializable {


    private static final long serialVersionUID = -3120961853982874126L;

    @Basic
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "id_lancamento_pagar")
    private Integer idLancamentoPagar;
    @Basic
    @Column(name = "id_parcela_pagar")
    private Integer idParcelaPagar;
    @Basic
    @Column(name = "id_conta_caixa")
    private Integer idContaCaixa;
    @Basic
    @Column(name = "id_status_parcela")
    private Integer idStatusParcela;
    @Basic
    @Column(name = "id_fornecedor")
    private Integer idFornecedor;
    @Basic
    @Column(name = "id_pessoa")
    private Integer idPessoa;
    @Basic
    @Column(name = "quantidade_parcela")
    private Integer quantidadeParcela;
    @Basic
    @Column(name = "valor_lancamento")
    private BigDecimal valorLancamento;
    @Basic
    @Column(name = "data_lancamento")
    private Date dataLancamento;
    @Basic
    @Column(name = "numero_documento")
    private String numeroDocumento;
    @Basic
    @Column(name = "numero_parcela")
    private Integer numeroParcela;
    @Basic
    @Column(name = "data_vencimento")
    private Date dataVencimento;
    @Basic
    @Column(name = "valor_parcela")
    private BigDecimal valorParcela;
    @Basic
    @Column(name = "taxa_juro")
    private BigDecimal taxaJuro;
    @Basic
    @Column(name = "valor_juro")
    private BigDecimal valorJuro;
    @Basic
    @Column(name = "taxa_multa")
    private BigDecimal taxaMulta;
    @Basic
    @Column(name = "valor_multa")
    private BigDecimal valorMulta;
    @Basic
    @Column(name = "taxa_desconto")
    private BigDecimal taxaDesconto;
    @Basic
    @Column(name = "valor_desconto")
    private BigDecimal valorDesconto;
    @Basic
    @Column(name = "sigla_documento")
    private String siglaDocumento;
    @Basic
    @Column(name = "nome_fornecedor")
    private String nomeFornecedor;
    @Basic
    @Column(name = "cpf_cnpj")
    private String cnpjCpf;
    @Basic
    @Column(name = "situacao_parcela")
    private String situacaoParcela;
    @Basic
    @Column(name = "descricao_situacao_parcela")
    private String descricaoSituacaoParcela;
    @Basic
    @Column(name = "nome_conta_caixa")
    private String nomeContaCaixa;
    @Basic
    @Column(name = "fornecedor_sofre_retencao")
    private String fornecedorSofreRetencao;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getIdLancamentoPagar() {
        return idLancamentoPagar;
    }

    public void setIdLancamentoPagar(Integer idLancamentoPagar) {
        this.idLancamentoPagar = idLancamentoPagar;
    }


    public Integer getIdParcelaPagar() {
        return idParcelaPagar;
    }

    public void setIdParcelaPagar(Integer idParcelaPagar) {
        this.idParcelaPagar = idParcelaPagar;
    }


    public Integer getIdContaCaixa() {
        return idContaCaixa;
    }

    public void setIdContaCaixa(Integer idContaCaixa) {
        this.idContaCaixa = idContaCaixa;
    }


    public Integer getIdStatusParcela() {
        return idStatusParcela;
    }

    public void setIdStatusParcela(Integer idStatusParcela) {
        this.idStatusParcela = idStatusParcela;
    }


    public Integer getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Integer idFornecedor) {
        this.idFornecedor = idFornecedor;
    }


    public Integer getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }


    public Integer getQuantidadeParcela() {
        return quantidadeParcela;
    }

    public void setQuantidadeParcela(Integer quantidadeParcela) {
        this.quantidadeParcela = quantidadeParcela;
    }


    public BigDecimal getValorLancamento() {
        return valorLancamento;
    }

    public void setValorLancamento(BigDecimal valorLancamento) {
        this.valorLancamento = valorLancamento;
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
        return taxaJuro;
    }

    public void setTaxaJuro(BigDecimal taxaJuro) {
        this.taxaJuro = taxaJuro;
    }


    public BigDecimal getValorJuro() {
        return valorJuro;
    }

    public void setValorJuro(BigDecimal valorJuro) {
        this.valorJuro = valorJuro;
    }


    public BigDecimal getTaxaMulta() {
        return taxaMulta;
    }

    public void setTaxaMulta(BigDecimal taxaMulta) {
        this.taxaMulta = taxaMulta;
    }


    public BigDecimal getValorMulta() {
        return valorMulta;
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


    public String getSiglaDocumento() {
        return siglaDocumento;
    }

    public void setSiglaDocumento(String siglaDocumento) {
        this.siglaDocumento = siglaDocumento;
    }


    public String getNomeFornecedor() {
        return nomeFornecedor;
    }

    public void setNomeFornecedor(String nomeFornecedor) {
        this.nomeFornecedor = nomeFornecedor;
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


    public String getNomeContaCaixa() {
        return nomeContaCaixa;
    }

    public void setNomeContaCaixa(String nomeContaCaixa) {
        this.nomeContaCaixa = nomeContaCaixa;
    }


    public String getFornecedorSofreRetencao() {
        return fornecedorSofreRetencao;
    }

    public void setFornecedorSofreRetencao(String fornecedorSofreRetencao) {
        this.fornecedorSofreRetencao = fornecedorSofreRetencao;
    }

    public String getCnpjCpf() {
        return cnpjCpf;
    }

    public void setCnpjCpf(String cnpjCpf) {
        this.cnpjCpf = cnpjCpf;
    }

    @Transient
    public Calendar getDataRecebimento() {
        Calendar dataRecebimento = Calendar.getInstance();
        dataRecebimento.setTime(new java.util.Date());
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
    public boolean isVencido() {
        return getDiasAtraso() > 0 && !this.situacaoParcela.equals("02");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ViewFinLancamentoPagar that = (ViewFinLancamentoPagar) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (idLancamentoPagar != null ? !idLancamentoPagar.equals(that.idLancamentoPagar) : that.idLancamentoPagar != null)
            return false;
        return idParcelaPagar != null ? idParcelaPagar.equals(that.idParcelaPagar) : that.idParcelaPagar == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (idLancamentoPagar != null ? idLancamentoPagar.hashCode() : 0);
        result = 31 * result + (idParcelaPagar != null ? idParcelaPagar.hashCode() : 0);

        return result;
    }
}
