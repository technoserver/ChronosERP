package com.chronos.modelo.entidades;

import com.chronos.modelo.enuns.SituacaoVenda;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by john on 18/01/18.
 */
@Entity
@Table(name = "pdv_venda_cabecalho")
@DynamicUpdate
public class PdvVendaCabecalho implements Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "id_nfe_cabecalho")
    private Integer idnfe;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_hora_venda")
    private Date dataHoraVenda;
    @Column(name = "VALOR_SUBTOTAL")
    private BigDecimal valorSubtotal;
    @Column(name = "TAXA_COMISSAO")
    private BigDecimal taxaComissao;
    @Column(name = "VALOR_COMISSAO")
    private BigDecimal valorComissao;
    @Column(name = "TAXA_DESCONTO")
    private BigDecimal taxaDesconto;
    @Column(name = "VALOR_DESCONTO")
    private BigDecimal valorDesconto;
    @Column(name = "VALOR_TOTAL")
    private BigDecimal valorTotal;
    @Column(name = "troco")
    private BigDecimal troco;
    @Column(name = "nome_cliente")
    private String nomeCliente;
    @Column(name = "cpf_cnpj_cliente")
    private String cpfCnpjCliente;
    @Column(name = "sicronizado")
    private String sicronizado;
    @Column(name = "status_venda")
    private String statusVenda;

    @JoinColumn(name = "ID_PDV_MOVIMENTO", referencedColumnName = "ID")
    @ManyToOne
    private PdvMovimento pdvMovimento;

    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne
    private Cliente cliente;

    @JoinColumn(name = "ID_VENDEDOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Vendedor vendedor;

    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;

    @OneToMany(mappedBy = "pdvVendaCabecalho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PdvVendaDetalhe> listaPdvVendaDetalhe;
    @OneToMany(mappedBy = "pdvVendaCabecalho", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PdvFormaPagamento> listaFormaPagamento;
    @Transient
    private String caixa;

    public PdvVendaCabecalho() {
        this.dataHoraVenda = new Date();
        this.sicronizado = "N";
        this.statusVenda = SituacaoVenda.Digitacao.getCodigo();
    }

    public PdvVendaCabecalho(Integer id, Date dataHoraVenda, BigDecimal valorSubtotal, BigDecimal valorDesconto, BigDecimal valorTotal, String nomeCliente, String statusVenda, String caixa) {
        this.id = id;
        this.dataHoraVenda = dataHoraVenda;
        this.valorSubtotal = valorSubtotal;
        this.valorDesconto = valorDesconto;
        this.valorTotal = valorTotal;
        this.nomeCliente = nomeCliente;
        this.statusVenda = statusVenda;
        this.caixa = caixa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdnfe() {
        return idnfe;
    }

    public void setIdnfe(Integer idnfe) {
        this.idnfe = idnfe;
    }

    public Date getDataHoraVenda() {
        return dataHoraVenda;
    }

    public void setDataHoraVenda(Date dataHoraVenda) {
        this.dataHoraVenda = dataHoraVenda;
    }

    public BigDecimal getValorSubtotal() {
        return valorSubtotal;
    }

    public void setValorSubtotal(BigDecimal valorSubtotal) {
        this.valorSubtotal = valorSubtotal;
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

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getTroco() {
        return troco;
    }

    public void setTroco(BigDecimal troco) {
        this.troco = troco;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getCpfCnpjCliente() {
        return cpfCnpjCliente;
    }

    public void setCpfCnpjCliente(String cpfCnpjCliente) {
        this.cpfCnpjCliente = cpfCnpjCliente;
    }

    public String getStatusVenda() {
        return statusVenda;
    }

    public void setStatusVenda(String statusVenda) {
        this.statusVenda = statusVenda;
    }

    public String getSicronizado() {
        return sicronizado;
    }

    public void setSicronizado(String sicronizado) {
        this.sicronizado = sicronizado;
    }

    public PdvMovimento getPdvMovimento() {
        return pdvMovimento;
    }

    public void setPdvMovimento(PdvMovimento pdvMovimento) {
        this.pdvMovimento = pdvMovimento;
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

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<PdvVendaDetalhe> getListaPdvVendaDetalhe() {
        return listaPdvVendaDetalhe;
    }

    public void setListaPdvVendaDetalhe(List<PdvVendaDetalhe> listaPdvVendaDetalhe) {
        this.listaPdvVendaDetalhe = listaPdvVendaDetalhe;
    }

    public List<PdvFormaPagamento> getListaFormaPagamento() {
        return listaFormaPagamento;
    }

    public void setListaFormaPagamento(List<PdvFormaPagamento> listaFormaPagamento) {
        this.listaFormaPagamento = listaFormaPagamento;
    }

    public String getCaixa() {
        return caixa;
    }

    public void setCaixa(String caixa) {
        this.caixa = caixa;
    }

    public BigDecimal calcularTotalDesconto() {
        valorDesconto = getListaPdvVendaDetalhe().stream()
                .map(PdvVendaDetalhe::getValorDesconto)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return valorDesconto;
    }


    public BigDecimal calcularValorProdutos() {
        valorSubtotal = getListaPdvVendaDetalhe().stream()
                .map(PdvVendaDetalhe::getValorSubtotal)
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

    public String valorSubTotalFormatado() {
        return formatarValor(calcularValorProdutos());
    }

    public String valorDescontoFormatado() {
        return formatarValor(calcularTotalDesconto());
    }

    public String valorTotalFormatado() {
        return formatarValor(calcularValorTotal());
    }

    private String formatarValor(BigDecimal valor) {
        DecimalFormatSymbols simboloDecimal = DecimalFormatSymbols.getInstance();
        simboloDecimal.setDecimalSeparator('.');
        DecimalFormat formatar = new DecimalFormat("0.00", simboloDecimal);

        return formatar.format(Optional.ofNullable(valor).orElse(BigDecimal.ZERO));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PdvVendaCabecalho)) return false;

        PdvVendaCabecalho that = (PdvVendaCabecalho) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
