package com.chronos.modelo.entidades;

import com.chronos.modelo.anotacoes.TaxaMaior;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Entity
@Table(name = "venda_consiguinada_cabecalho")
@NamedQuery(name = "VendaConsignadaCabecalho.UpdateSituacao", query = "UPDATE VendaCabecalho v SET v.situacao = ?1 where v.id = ?2")
@DynamicUpdate
public class VendaConsignadaCabecalho implements Serializable {

    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_SAIDA")
    private Date dataSaida;
    @Column(name = "LOCAL_ENTREGA")
    private String localEntrega;
    @Column(name = "VALOR_SUBTOTAL")
    private BigDecimal valorSubtotal;
    @Column(name = "TAXA_COMISSAO")
    private BigDecimal taxaComissao;
    @Column(name = "VALOR_COMISSAO")
    private BigDecimal valorComissao;
    @Column(name = "TAXA_DESCONTO")
    @TaxaMaior()
    private BigDecimal taxaDesconto;
    @Column(name = "VALOR_DESCONTO")
    private BigDecimal valorDesconto;
    @Column(name = "VALOR_TOTAL")
    private BigDecimal valorTotal;
    @JoinColumn(name = "id_condicao_pagamento", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private VendaCondicoesPagamento condicoesPagamento;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Cliente cliente;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;
    @OneToMany(mappedBy = "vendaCabecalho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VendaDetalhe> listaVendaDetalhe;
    @Transient
    private boolean excludoItem;

    public VendaConsignadaCabecalho() {
        this.listaVendaDetalhe = new ArrayList<>();
        this.valorComissao = BigDecimal.ZERO;
        this.valorDesconto = BigDecimal.ZERO;
        this.valorSubtotal = BigDecimal.ZERO;
        this.valorTotal = BigDecimal.ZERO;
    }

    public VendaConsignadaCabecalho(Integer id) {
        this.id = id;
    }

    public VendaConsignadaCabecalho(Integer id, BigDecimal valorTotal, String situacao, String cliente) {
        this.id = id;
        this.valorTotal = valorTotal;
        this.cliente = new Cliente(0, cliente);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(Date dataSaida) {
        this.dataSaida = dataSaida;
    }

    public String getLocalEntrega() {
        return localEntrega;
    }

    public void setLocalEntrega(String localEntrega) {
        this.localEntrega = localEntrega;
    }

    public BigDecimal getValorSubtotal() {
        return valorSubtotal;
    }

    public void setValorSubtotal(BigDecimal valorSubtotal) {
        this.valorSubtotal = valorSubtotal;
    }

    public BigDecimal getTaxaComissao() {
        return Optional.ofNullable(taxaComissao).orElse(BigDecimal.ZERO);
    }

    public void setTaxaComissao(BigDecimal taxaComissao) {
        this.taxaComissao = taxaComissao;
    }

    public BigDecimal getValorComissao() {
        valorComissao = getTaxaComissao().multiply(getValorTotal()).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
        return valorComissao;
    }

    public void setValorComissao(BigDecimal valorComissao) {
        this.valorComissao = valorComissao;
    }

    public BigDecimal getTaxaDesconto() {
        return taxaDesconto;
    }

    public void setTaxaDesconto(BigDecimal taxaDesconto) {
        this.taxaDesconto = taxaDesconto;
    }

    public BigDecimal getValorDesconto() {
        return Optional.ofNullable(valorDesconto).orElse(BigDecimal.ZERO);
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public VendaCondicoesPagamento getCondicoesPagamento() {
        return condicoesPagamento;
    }

    public void setCondicoesPagamento(VendaCondicoesPagamento condicoesPagamento) {
        this.condicoesPagamento = condicoesPagamento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<VendaDetalhe> getListaVendaDetalhe() {
        return Optional.ofNullable(listaVendaDetalhe).orElse(new ArrayList<>());
    }

    public void setListaVendaDetalhe(List<VendaDetalhe> listaVendaDetalhe) {
        this.listaVendaDetalhe = listaVendaDetalhe;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public boolean isExcludoItem() {
        return excludoItem;
    }

    public void setExcludoItem(boolean excludoItem) {
        this.excludoItem = excludoItem;
    }


    public BigDecimal calcularTotalDesconto() {
        valorDesconto = getListaVendaDetalhe().stream()
                .map(VendaDetalhe::getValorDesconto)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return valorDesconto;
    }


    public BigDecimal calcularValorProdutos() {
        valorSubtotal = getListaVendaDetalhe().stream()
                .map(VendaDetalhe::getValorSubtotal)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        return valorSubtotal;
    }

    public BigDecimal calcularValorTotal() {
        valorTotal = calcularValorProdutos();
        valorTotal = valorTotal
                .subtract(calcularTotalDesconto());
        return valorTotal;
    }

    public BigDecimal calcularValorDevolucao() {

        return getListaVendaDetalhe().stream()
                .map(VendaDetalhe::getValorTotalDevolvido)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }


    public boolean temProduto() {
        return getListaVendaDetalhe().size() > 0;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VendaConsignadaCabecalho other = (VendaConsignadaCabecalho) obj;
        return Objects.equals(this.id, other.id);
    }

}
