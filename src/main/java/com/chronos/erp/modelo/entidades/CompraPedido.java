/*
 * The MIT License
 *
 * Copyright: Copyright (C) 2014 chronosinfo.COM
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * The author may be contacted at: chronosinfo.com@gmail.com
 *
 * @author John Vanderson M Lima (chronosinfo.com)
 * @version 2.0
 */
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "COMPRA_PEDIDO")
public class CompraPedido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_PEDIDO")
    private Date dataPedido;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_PREVISTA_ENTREGA")
    private Date dataPrevistaEntrega;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_PREVISAO_PAGAMENTO")
    private Date dataPrevisaoPagamento;
    @Column(name = "LOCAL_ENTREGA")
    private String localEntrega;
    @Column(name = "LOCAL_COBRANCA")
    private String localCobranca;
    @Column(name = "CONTATO")
    private String contato;
    @Column(name = "VALOR_SUBTOTAL")
    private BigDecimal valorSubtotal;
    @Column(name = "TAXA_DESCONTO")
    @DecimalMax(value = "100.0", message = "O deve deve ser igual ou menor que 100")
    private BigDecimal taxaDesconto;
    @Column(name = "VALOR_DESCONTO")
    private BigDecimal valorDesconto;
    @Column(name = "VALOR_TOTAL_PEDIDO")
    private BigDecimal valorTotalPedido;
    @Column(name = "TIPO_FRETE")
    private String tipoFrete;
    @Column(name = "FORMA_PAGAMENTO")
    private String formaPagamento;
    @Column(name = "BASE_CALCULO_ICMS")
    private BigDecimal baseCalculoIcms;
    @Column(name = "VALOR_ICMS")
    private BigDecimal valorIcms;
    @Column(name = "BASE_CALCULO_ICMS_ST")
    private BigDecimal baseCalculoIcmsSt;
    @Column(name = "VALOR_ICMS_ST")
    private BigDecimal valorIcmsSt;
    @Column(name = "VALOR_TOTAL_PRODUTOS")
    private BigDecimal valorTotalProdutos;
    @Column(name = "VALOR_FRETE")
    private BigDecimal valorFrete;
    @Column(name = "VALOR_SEGURO")
    private BigDecimal valorSeguro;
    @Column(name = "VALOR_OUTRAS_DESPESAS")
    private BigDecimal valorOutrasDespesas;
    @Column(name = "VALOR_IPI")
    private BigDecimal valorIpi;
    @Column(name = "VALOR_TOTAL_NF")
    private BigDecimal valorTotalNf;
    @Column(name = "QUANTIDADE_PARCELAS")
    private Integer quantidadeParcelas;
    @Column(name = "DIAS_PRIMEIRO_VENCIMENTO")
    private Integer diasPrimeiroVencimento;
    @Column(name = "DIAS_INTERVALO")
    private Integer diasIntervalo;
    @JoinColumn(name = "ID_COMPRA_TIPO_PEDIDO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private CompraTipoPedido compraTipoPedido;
    @JoinColumn(name = "ID_FORNECEDOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Fornecedor fornecedor;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "compraPedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CompraPedidoDetalhe> listaCompraPedidoDetalhe;

    public CompraPedido() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }

    public Date getDataPrevistaEntrega() {
        return dataPrevistaEntrega;
    }

    public void setDataPrevistaEntrega(Date dataPrevistaEntrega) {
        this.dataPrevistaEntrega = dataPrevistaEntrega;
    }

    public Date getDataPrevisaoPagamento() {
        return dataPrevisaoPagamento;
    }

    public void setDataPrevisaoPagamento(Date dataPrevisaoPagamento) {
        this.dataPrevisaoPagamento = dataPrevisaoPagamento;
    }

    public String getLocalEntrega() {
        return localEntrega;
    }

    public void setLocalEntrega(String localEntrega) {
        this.localEntrega = localEntrega;
    }

    public String getLocalCobranca() {
        return localCobranca;
    }

    public void setLocalCobranca(String localCobranca) {
        this.localCobranca = localCobranca;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public BigDecimal getValorSubtotal() {
        return valorSubtotal;
    }

    public void setValorSubtotal(BigDecimal valorSubtotal) {
        this.valorSubtotal = valorSubtotal;
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

    public BigDecimal getValorTotalPedido() {
        return valorTotalPedido;
    }

    public void setValorTotalPedido(BigDecimal valorTotalPedido) {
        this.valorTotalPedido = valorTotalPedido;
    }

    public String getTipoFrete() {
        return tipoFrete;
    }

    public void setTipoFrete(String tipoFrete) {
        this.tipoFrete = tipoFrete;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public BigDecimal getBaseCalculoIcms() {
        return baseCalculoIcms;
    }

    public void setBaseCalculoIcms(BigDecimal baseCalculoIcms) {
        this.baseCalculoIcms = baseCalculoIcms;
    }

    public BigDecimal getValorIcms() {
        return valorIcms;
    }

    public void setValorIcms(BigDecimal valorIcms) {
        this.valorIcms = valorIcms;
    }

    public BigDecimal getBaseCalculoIcmsSt() {
        return baseCalculoIcmsSt;
    }

    public void setBaseCalculoIcmsSt(BigDecimal baseCalculoIcmsSt) {
        this.baseCalculoIcmsSt = baseCalculoIcmsSt;
    }

    public BigDecimal getValorIcmsSt() {
        return valorIcmsSt;
    }

    public void setValorIcmsSt(BigDecimal valorIcmsSt) {
        this.valorIcmsSt = valorIcmsSt;
    }

    public BigDecimal getValorTotalProdutos() {
        return valorTotalProdutos;
    }

    public void setValorTotalProdutos(BigDecimal valorTotalProdutos) {
        this.valorTotalProdutos = valorTotalProdutos;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }

    public BigDecimal getValorSeguro() {
        return valorSeguro;
    }

    public void setValorSeguro(BigDecimal valorSeguro) {
        this.valorSeguro = valorSeguro;
    }

    public BigDecimal getValorOutrasDespesas() {
        return valorOutrasDespesas;
    }

    public void setValorOutrasDespesas(BigDecimal valorOutrasDespesas) {
        this.valorOutrasDespesas = valorOutrasDespesas;
    }

    public BigDecimal getValorIpi() {
        return valorIpi;
    }

    public void setValorIpi(BigDecimal valorIpi) {
        this.valorIpi = valorIpi;
    }

    public BigDecimal getValorTotalNf() {
        return valorTotalNf;
    }

    public void setValorTotalNf(BigDecimal valorTotalNf) {
        this.valorTotalNf = valorTotalNf;
    }

    public Integer getQuantidadeParcelas() {
        return quantidadeParcelas;
    }

    public void setQuantidadeParcelas(Integer quantidadeParcelas) {
        this.quantidadeParcelas = quantidadeParcelas;
    }

    public Integer getDiasPrimeiroVencimento() {
        return diasPrimeiroVencimento;
    }

    public void setDiasPrimeiroVencimento(Integer diasPrimeiroVencimento) {
        this.diasPrimeiroVencimento = diasPrimeiroVencimento;
    }

    public Integer getDiasIntervalo() {
        return diasIntervalo;
    }

    public void setDiasIntervalo(Integer diasIntervalo) {
        this.diasIntervalo = diasIntervalo;
    }

    public CompraTipoPedido getCompraTipoPedido() {
        return compraTipoPedido;
    }

    public void setCompraTipoPedido(CompraTipoPedido compraTipoPedido) {
        this.compraTipoPedido = compraTipoPedido;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Set<CompraPedidoDetalhe> getListaCompraPedidoDetalhe() {
        return listaCompraPedidoDetalhe;
    }

    public void setListaCompraPedidoDetalhe(Set<CompraPedidoDetalhe> listaCompraPedidoDetalhe) {
        this.listaCompraPedidoDetalhe = listaCompraPedidoDetalhe;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public String toString() {
        return "CompraPedido{" + "id=" + id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final CompraPedido other = (CompraPedido) obj;
        return Objects.equals(this.id, other.id);
    }


}
