package com.chronos.modelo.entidades;

import com.chronos.modelo.anotacoes.TaxaMaior;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "venda_consignada_cabecalho")
@NamedQuery(name = "VendaConsignadaCabecalho.UpdateSituacao", query = "UPDATE VendaConsignadaCabecalho v SET v.status = ?1 where v.id = ?2")
@DynamicUpdate
public class VendaConsignadaCabecalho implements Serializable {

    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "ID_VENDA_CABECALHO")
    private Integer idvendaCebecalho;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_SAIDA")
    private Date dataSaida;
    @Temporal(TemporalType.DATE)
    @Column(name = "data_devolucao")
    private Date dataDevolucao;
    @Column(name = "LOCAL_ENTREGA")
    private String localEntrega;
    @Column(name = "VALOR_SUBTOTAL")
    private BigDecimal valorSubtotal;
    @Column(name = "TAXA_COMISSAO")
    private BigDecimal taxaComissao;
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
    @JoinColumn(name = "ID_VENDEDOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Vendedor vendedor;
    private StatusConsignacao status;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;
    @OneToMany(mappedBy = "vendaConsignadaCabecalho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VendaConsignadaDetalhe> listaVendaConsignadaDetalhe;
    @Transient
    private boolean excludoItem;

    public VendaConsignadaCabecalho() {
        this.listaVendaConsignadaDetalhe = new ArrayList<>();
        this.valorDesconto = BigDecimal.ZERO;
        this.valorSubtotal = BigDecimal.ZERO;
        this.valorTotal = BigDecimal.ZERO;
        this.dataSaida = new Date();
        this.status = StatusConsignacao.EDICAO;
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

    public Integer getIdvendaCebecalho() {
        return idvendaCebecalho;
    }

    public void setIdvendaCebecalho(Integer idvendaCebecalho) {
        this.idvendaCebecalho = idvendaCebecalho;
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(Date dataSaida) {
        this.dataSaida = dataSaida;
    }

    public Date getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(Date dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
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

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public List<VendaConsignadaDetalhe> getListaVendaConsignadaDetalhe() {
        return listaVendaConsignadaDetalhe;
    }

    public void setListaVendaConsignadaDetalhe(List<VendaConsignadaDetalhe> listaVendaConsignadaDetalhe) {
        this.listaVendaConsignadaDetalhe = listaVendaConsignadaDetalhe;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public StatusConsignacao getStatus() {
        return status;
    }

    public void setStatus(StatusConsignacao status) {
        this.status = status;
    }

    public boolean isExcludoItem() {
        return excludoItem;
    }

    public void setExcludoItem(boolean excludoItem) {
        this.excludoItem = excludoItem;
    }


    public BigDecimal calcularTotalDesconto() {
        valorDesconto = getListaVendaConsignadaDetalhe().stream()
                .map(VendaConsignadaDetalhe::getValorDesconto)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return valorDesconto;
    }


    public BigDecimal calcularValorProdutos() {
        valorSubtotal = getListaVendaConsignadaDetalhe().stream()
                .map(VendaConsignadaDetalhe::getValorSubtotal)
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

    public boolean temProduto() {
        return getListaVendaConsignadaDetalhe().size() > 0;
    }


    public boolean isEncerrado() {
        return status == StatusConsignacao.ENCERRADO;
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
